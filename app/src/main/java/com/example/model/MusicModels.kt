package com.example.model

data class LyricLine(
    val timeMs: Long,
    val text: String
)

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val genre: String,
    val releaseYear: Int,
    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = false,
    val primaryColorHex: Long = 0xFF7B2CBFL,
    val secondaryColorHex: Long = 0xFF00F5D4L,
    val lyrics: List<LyricLine> = emptyList(),
    val bitRateKbps: Int = 320,
    val synthPattern: String = "synthwave",
    val baseFrequencyHz: Double = 220.0
)

data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val year: Int,
    val primaryColorHex: Long,
    val secondaryColorHex: Long,
    val songs: List<Song> = emptyList()
)

data class Artist(
    val id: String,
    val name: String,
    val bio: String,
    val monthlyListeners: String,
    val primaryColorHex: Long,
    val topSongs: List<Song> = emptyList()
)

data class Playlist(
    val id: String,
    val name: String,
    val description: String,
    val songIds: List<String>,
    val isCustom: Boolean = true,
    val coverGradientStart: Long = 0xFF7B2CBFL,
    val coverGradientEnd: Long = 0xFF00F5D4L,
    val createdAt: Long = System.currentTimeMillis()
)

enum class RepeatMode {
    OFF, ALL, ONE
}

enum class AudioQuality(val label: String, val bitRate: String, val badge: String) {
    STANDARD("Standard", "128 kbps", "MP3 128K"),
    HIGH("High Quality", "256 kbps", "AAC 256K"),
    EXTREME("Extreme", "320 kbps", "HQ 320K"),
    LOSSLESS("Hi-Res Lossless", "1411 kbps", "FLAC 24-BIT")
}

data class EqualizerState(
    val presetName: String = "Normal",
    val band60Hz: Float = 0f,      // -10f to +10f dB
    val band230Hz: Float = 0f,
    val band910Hz: Float = 0f,
    val band3600Hz: Float = 0f,
    val band14000Hz: Float = 0f,
    val bassBoost: Float = 0.4f,    // 0f to 1f
    val virtualizer: Float = 0.3f,  // 0f to 1f
    val isEnabled: Boolean = true
)

data class SleepTimerInfo(
    val isActive: Boolean = false,
    val totalSeconds: Int = 0,
    val remainingSeconds: Int = 0,
    val targetTimeMs: Long = 0L
)
