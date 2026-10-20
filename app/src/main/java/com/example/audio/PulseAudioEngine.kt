package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.model.EqualizerState
import com.example.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

class PulseAudioEngine(
    private val onPositionUpdated: (Long) -> Unit,
    private val onCompletion: () -> Unit,
    private val onVisualizerUpdated: (List<Float>) -> Unit
) {
    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    private val sampleRate = 44100
    private var isPlaying = false
    private var currentPositionMs = 0L
    private var currentSong: Song? = null
    private var equalizerState = EqualizerState()

    fun setEqualizer(eq: EqualizerState) {
        this.equalizerState = eq
    }

    fun playSong(song: Song, startPositionMs: Long = 0L) {
        stop()
        currentSong = song
        currentPositionMs = startPositionMs
        isPlaying = true
        startAudioStream()
    }

    fun pause() {
        isPlaying = false
        audioTrack?.pause()
    }

    fun resume() {
        if (currentSong == null) return
        isPlaying = true
        if (audioTrack == null || audioTrack?.state != AudioTrack.STATE_INITIALIZED) {
            startAudioStream()
        } else {
            audioTrack?.play()
            startLoop()
        }
    }

    fun seekTo(positionMs: Long) {
        val song = currentSong ?: return
        currentPositionMs = positionMs.coerceIn(0L, song.durationMs)
        onPositionUpdated(currentPositionMs)
    }

    fun stop() {
        isPlaying = false
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
        onVisualizerUpdated(listOf(0.1f, 0.1f, 0.1f, 0.1f, 0.1f))
    }

    private fun startAudioStream() {
        playbackJob?.cancel()
        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val bufferSize = minBufferSize.coerceAtLeast(sampleRate / 4)

        try {
            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack?.play()
            startLoop()
        } catch (_: Exception) {
            // AudioTrack fallback
        }
    }

    private fun startLoop() {
        playbackJob = scope.launch {
            val chunkDurationMs = 50L
            val samplesPerChunk = (sampleRate * (chunkDurationMs / 1000.0)).toInt()
            val buffer = ShortArray(samplesPerChunk * 2) // Stereo (L, R)

            var phasePad1 = 0.0
            var phasePad2 = 0.0
            var phaseBass = 0.0
            var phaseArp = 0.0

            while (isActive && isPlaying) {
                val song = currentSong ?: break
                if (currentPositionMs >= song.durationMs) {
                    onPositionUpdated(song.durationMs)
                    onCompletion()
                    break
                }

                val baseFreq = song.baseFrequencyHz
                // Harmonic intervals based on time (chords changing every 4 seconds)
                val chordIndex = ((currentPositionMs / 4000) % 4).toInt()
                val chordInterval = when (chordIndex) {
                    0 -> 1.0       // Root (e.g. A)
                    1 -> 1.2599    // Major 3rd / Minor 3rd
                    2 -> 1.4983    // 5th
                    else -> 0.8909 // 7th / sub
                }

                // Bass boost & EQ factors
                val bassMult = 1.0 + (equalizerState.bassBoost * 0.8) + (equalizerState.band60Hz / 15.0)
                val midMult = 1.0 + (equalizerState.band910Hz / 15.0)
                val trebleMult = 1.0 + (equalizerState.band14000Hz / 15.0)
                val stereoWidth = 0.5 + (equalizerState.virtualizer * 0.5)

                val freqPad1 = baseFreq * chordInterval
                val freqPad2 = baseFreq * chordInterval * 1.5
                val freqBass = (baseFreq / 2.0) * chordInterval
                val arpStep = ((currentPositionMs / 250) % 4).toInt()
                val freqArp = baseFreq * 2.0 * when (arpStep) {
                    0 -> 1.0
                    1 -> 1.2
                    2 -> 1.5
                    else -> 1.8
                }

                var peakSample = 0.0
                for (i in 0 until samplesPerChunk) {
                    phasePad1 += 2.0 * PI * freqPad1 / sampleRate
                    phasePad2 += 2.0 * PI * freqPad2 / sampleRate
                    phaseBass += 2.0 * PI * freqBass / sampleRate
                    phaseArp += 2.0 * PI * freqArp / sampleRate

                    if (phasePad1 > 2.0 * PI) phasePad1 -= 2.0 * PI
                    if (phasePad2 > 2.0 * PI) phasePad2 -= 2.0 * PI
                    if (phaseBass > 2.0 * PI) phaseBass -= 2.0 * PI
                    if (phaseArp > 2.0 * PI) phaseArp -= 2.0 * PI

                    // Soft synth voices
                    val pad = (sin(phasePad1) + 0.5 * sin(phasePad2)) * 0.25 * midMult
                    val bass = (sin(phaseBass) + 0.3 * sin(phaseBass * 2.0)) * 0.35 * bassMult
                    val arp = (sin(phaseArp)) * 0.15 * trebleMult

                    val mixed = (pad + bass + arp).coerceIn(-1.0, 1.0)
                    val sampleLeft = (mixed * (1.0 - stereoWidth * 0.2) * 26000).toInt().toShort()
                    val sampleRight = (mixed * (1.0 + stereoWidth * 0.2) * 26000).toInt().toShort()

                    buffer[i * 2] = sampleLeft
                    buffer[i * 2 + 1] = sampleRight

                    if (Math.abs(mixed) > peakSample) {
                        peakSample = Math.abs(mixed)
                    }
                }

                audioTrack?.write(buffer, 0, buffer.size)

                currentPositionMs += chunkDurationMs
                onPositionUpdated(currentPositionMs)

                // Produce dynamic visualizer frequency bars
                val v1 = (0.2 + (peakSample * 0.7 * bassMult)).toFloat().coerceIn(0.1f, 1.0f)
                val v2 = (0.3 + (sin(currentPositionMs / 120.0) * 0.4 * midMult)).toFloat().coerceIn(0.15f, 1.0f)
                val v3 = (0.25 + (peakSample * 0.8)).toFloat().coerceIn(0.1f, 1.0f)
                val v4 = (0.2 + (sin(currentPositionMs / 90.0) * 0.5 * trebleMult)).toFloat().coerceIn(0.12f, 1.0f)
                val v5 = (0.15 + (peakSample * 0.6)).toFloat().coerceIn(0.1f, 0.95f)

                onVisualizerUpdated(listOf(v1, v2, v3, v4, v5))
            }
        }
    }
}
