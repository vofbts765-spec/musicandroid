package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.PulseAudioEngine
import com.example.data.MockMusicCatalog
import com.example.model.Album
import com.example.model.Artist
import com.example.model.AudioQuality
import com.example.model.EqualizerState
import com.example.model.Playlist
import com.example.model.RepeatMode
import com.example.model.SleepTimerInfo
import com.example.model.Song
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

enum class NavigationTab {
    HOME,
    SONGS,
    PLAYLISTS,
    ARTISTS,
    DOWNLOADS,
    SETTINGS
}

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    // Audio Engine
    private val audioEngine: PulseAudioEngine = PulseAudioEngine(
        onPositionUpdated = { pos -> _currentPositionMs.value = pos },
        onCompletion = { handleSongCompleted() },
        onVisualizerUpdated = { bars -> _visualizerBars.value = bars }
    )

    // Library Data
    private val _allSongs = MutableStateFlow(MockMusicCatalog.sampleSongs)
    val allSongs: StateFlow<List<Song>> = _allSongs.asStateFlow()

    private val _playlists = MutableStateFlow(MockMusicCatalog.samplePlaylists)
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    private val _albums = MutableStateFlow(MockMusicCatalog.sampleAlbums)
    val albums: StateFlow<List<Album>> = _albums.asStateFlow()

    private val _artists = MutableStateFlow(MockMusicCatalog.sampleArtists)
    val artists: StateFlow<List<Artist>> = _artists.asStateFlow()

    private val _favorites = MutableStateFlow(
        MockMusicCatalog.sampleSongs.filter { it.isFavorite }.map { it.id }.toSet()
    )
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private val _downloadedSongIds = MutableStateFlow(
        MockMusicCatalog.sampleSongs.filter { it.isDownloaded }.map { it.id }.toSet()
    )
    val downloadedSongIds: StateFlow<Set<String>> = _downloadedSongIds.asStateFlow()

    private val _downloadProgressMap = MutableStateFlow<Map<String, Float>>(emptyMap())
    val downloadProgressMap: StateFlow<Map<String, Float>> = _downloadProgressMap.asStateFlow()

    // Playback State
    private val _currentSong = MutableStateFlow<Song?>(MockMusicCatalog.sampleSongs.firstOrNull())
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _playbackQueue = MutableStateFlow<List<Song>>(MockMusicCatalog.sampleSongs)
    val playbackQueue: StateFlow<List<Song>> = _playbackQueue.asStateFlow()

    private val _queueIndex = MutableStateFlow(0)
    val queueIndex: StateFlow<Int> = _queueIndex.asStateFlow()

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()

    private val _repeatMode = MutableStateFlow(RepeatMode.ALL)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()

    private val _visualizerBars = MutableStateFlow(listOf(0.2f, 0.4f, 0.3f, 0.5f, 0.25f))
    val visualizerBars: StateFlow<List<Float>> = _visualizerBars.asStateFlow()

    // Filtering & Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGenre = MutableStateFlow<String?>("All")
    val selectedGenre: StateFlow<String?> = _selectedGenre.asStateFlow()

    private val _offlineOnlyMode = MutableStateFlow(false)
    val offlineOnlyMode: StateFlow<Boolean> = _offlineOnlyMode.asStateFlow()

    // Equalizer
    private val _equalizerState = MutableStateFlow(EqualizerState())
    val equalizerState: StateFlow<EqualizerState> = _equalizerState.asStateFlow()

    // Sleep Timer
    private val _sleepTimer = MutableStateFlow(SleepTimerInfo())
    val sleepTimer: StateFlow<SleepTimerInfo> = _sleepTimer.asStateFlow()
    private var sleepTimerJob: Job? = null

    // Audio Quality
    private val _audioQuality = MutableStateFlow(AudioQuality.EXTREME)
    val audioQuality: StateFlow<AudioQuality> = _audioQuality.asStateFlow()

    // Sync & Notifications
    private val _crossDeviceSyncStatus = MutableStateFlow("Synced across Phone, Tablet, & Desktop")
    val crossDeviceSyncStatus: StateFlow<String> = _crossDeviceSyncStatus.asStateFlow()

    private val _pushNotificationsEnabled = MutableStateFlow(true)
    val pushNotificationsEnabled: StateFlow<Boolean> = _pushNotificationsEnabled.asStateFlow()

    // Navigation & Sheet States
    private val _currentTab = MutableStateFlow(NavigationTab.HOME)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _isNowPlayingExpanded = MutableStateFlow(false)
    val isNowPlayingExpanded: StateFlow<Boolean> = _isNowPlayingExpanded.asStateFlow()

    private val _showLyricsSheet = MutableStateFlow(false)
    val showLyricsSheet: StateFlow<Boolean> = _showLyricsSheet.asStateFlow()

    private val _showQueueSheet = MutableStateFlow(false)
    val showQueueSheet: StateFlow<Boolean> = _showQueueSheet.asStateFlow()

    private val _showEqualizerSheet = MutableStateFlow(false)
    val showEqualizerSheet: StateFlow<Boolean> = _showEqualizerSheet.asStateFlow()

    private val _showSleepTimerDialog = MutableStateFlow(false)
    val showSleepTimerDialog: StateFlow<Boolean> = _showSleepTimerDialog.asStateFlow()

    private val _showCreatePlaylistDialog = MutableStateFlow(false)
    val showCreatePlaylistDialog: StateFlow<Boolean> = _showCreatePlaylistDialog.asStateFlow()

    private val _songToAddToPlaylist = MutableStateFlow<Song?>(null)
    val songToAddToPlaylist: StateFlow<Song?> = _songToAddToPlaylist.asStateFlow()

    private val _showPersonalizedGeneratorDialog = MutableStateFlow(false)
    val showPersonalizedGeneratorDialog: StateFlow<Boolean> = _showPersonalizedGeneratorDialog.asStateFlow()

    private val _selectedPlaylistDetail = MutableStateFlow<Playlist?>(null)
    val selectedPlaylistDetail: StateFlow<Playlist?> = _selectedPlaylistDetail.asStateFlow()

    private val _selectedArtistDetail = MutableStateFlow<Artist?>(null)
    val selectedArtistDetail: StateFlow<Artist?> = _selectedArtistDetail.asStateFlow()

    private val _selectedAlbumDetail = MutableStateFlow<Album?>(null)
    val selectedAlbumDetail: StateFlow<Album?> = _selectedAlbumDetail.asStateFlow()

    // Status message toast/banner
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun showToast(msg: String) {
        _statusMessage.value = msg
        viewModelScope.launch {
            delay(3000)
            if (_statusMessage.value == msg) {
                _statusMessage.value = null
            }
        }
    }

    // Playback Controls
    fun playSong(song: Song, queue: List<Song> = _allSongs.value) {
        _currentSong.value = song
        _playbackQueue.value = queue
        _queueIndex.value = queue.indexOfFirst { it.id == song.id }.coerceAtLeast(0)
        _isPlaying.value = true
        _currentPositionMs.value = 0L
        audioEngine.playSong(song, 0L)
    }

    fun togglePlayPause() {
        val current = _currentSong.value ?: return
        if (_isPlaying.value) {
            _isPlaying.value = false
            audioEngine.pause()
        } else {
            _isPlaying.value = true
            audioEngine.resume()
        }
    }

    fun seekTo(positionMs: Long) {
        _currentPositionMs.value = positionMs
        audioEngine.seekTo(positionMs)
    }

    fun playNext() {
        val queue = _playbackQueue.value
        if (queue.isEmpty()) return

        var nextIndex = _queueIndex.value + 1
        if (_isShuffle.value) {
            nextIndex = (0 until queue.size).random()
        } else if (nextIndex >= queue.size) {
            if (_repeatMode.value == RepeatMode.ALL) {
                nextIndex = 0
            } else {
                _isPlaying.value = false
                audioEngine.stop()
                return
            }
        }

        _queueIndex.value = nextIndex
        val nextSong = queue[nextIndex]
        _currentSong.value = nextSong
        _currentPositionMs.value = 0L
        _isPlaying.value = true
        audioEngine.playSong(nextSong, 0L)
    }

    fun playPrevious() {
        val queue = _playbackQueue.value
        if (queue.isEmpty()) return

        if (_currentPositionMs.value > 3000L) {
            seekTo(0L)
            return
        }

        var prevIndex = _queueIndex.value - 1
        if (prevIndex < 0) {
            prevIndex = if (_repeatMode.value == RepeatMode.ALL) queue.size - 1 else 0
        }

        _queueIndex.value = prevIndex
        val prevSong = queue[prevIndex]
        _currentSong.value = prevSong
        _currentPositionMs.value = 0L
        _isPlaying.value = true
        audioEngine.playSong(prevSong, 0L)
    }

    fun toggleShuffle() {
        _isShuffle.value = !_isShuffle.value
        showToast(if (_isShuffle.value) "Shuffle turned ON" else "Shuffle turned OFF")
    }

    fun toggleRepeatMode() {
        val nextMode = when (_repeatMode.value) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        _repeatMode.value = nextMode
        val label = when (nextMode) {
            RepeatMode.OFF -> "Repeat OFF"
            RepeatMode.ALL -> "Repeat ALL"
            RepeatMode.ONE -> "Repeat CURRENT TRACK"
        }
        showToast(label)
    }

    private fun handleSongCompleted() {
        when (_repeatMode.value) {
            RepeatMode.ONE -> {
                _currentPositionMs.value = 0L
                _currentSong.value?.let { audioEngine.playSong(it, 0L) }
            }
            RepeatMode.ALL, RepeatMode.OFF -> {
                playNext()
            }
        }
    }

    // Favorites & Downloads
    fun toggleFavorite(songId: String) {
        val currentFavs = _favorites.value.toMutableSet()
        val isNowFav = if (currentFavs.contains(songId)) {
            currentFavs.remove(songId)
            false
        } else {
            currentFavs.add(songId)
            true
        }
        _favorites.value = currentFavs

        _allSongs.value = _allSongs.value.map {
            if (it.id == songId) it.copy(isFavorite = isNowFav) else it
        }
        showToast(if (isNowFav) "Added to Favorites" else "Removed from Favorites")
    }

    fun downloadSong(song: Song) {
        if (_downloadedSongIds.value.contains(song.id)) {
            // Already downloaded, allow deleting
            val updated = _downloadedSongIds.value.toMutableSet().apply { remove(song.id) }
            _downloadedSongIds.value = updated
            _allSongs.value = _allSongs.value.map {
                if (it.id == song.id) it.copy(isDownloaded = false) else it
            }
            showToast("Removed from Offline Downloads")
            return
        }

        // Simulate fast, smooth offline download with progress
        viewModelScope.launch {
            val progressMap = _downloadProgressMap.value.toMutableMap()
            progressMap[song.id] = 0.1f
            _downloadProgressMap.value = progressMap

            for (p in listOf(0.3f, 0.65f, 0.9f, 1.0f)) {
                delay(300)
                progressMap[song.id] = p
                _downloadProgressMap.value = HashMap(progressMap)
            }

            progressMap.remove(song.id)
            _downloadProgressMap.value = progressMap

            val updated = _downloadedSongIds.value.toMutableSet().apply { add(song.id) }
            _downloadedSongIds.value = updated
            _allSongs.value = _allSongs.value.map {
                if (it.id == song.id) it.copy(isDownloaded = true) else it
            }
            showToast("Downloaded \"${song.title}\" for Offline Playback (HQ 320k)")
        }
    }

    // Playlists
    fun createPlaylist(name: String, description: String) {
        if (name.isBlank()) return
        val newPlaylist = Playlist(
            id = "playlist_${UUID.randomUUID()}",
            name = name.trim(),
            description = description.ifBlank { "Custom curated collection by user." },
            songIds = emptyList(),
            isCustom = true,
            coverGradientStart = listOf(0xFF7B2CBFL, 0xFF3A86FFL, 0xFFFF006EL, 0xFF06D6A0L).random(),
            coverGradientEnd = listOf(0xFF00F5D4L, 0xFFFFD166L, 0xFFE63946L, 0xFF8338ECL).random()
        )
        _playlists.value = listOf(newPlaylist) + _playlists.value
        _showCreatePlaylistDialog.value = false
        showToast("Playlist \"$name\" created!")
    }

    fun addSongToPlaylist(playlistId: String, songId: String) {
        _playlists.value = _playlists.value.map { playlist ->
            if (playlist.id == playlistId) {
                val updatedIds = if (!playlist.songIds.contains(songId)) playlist.songIds + songId else playlist.songIds
                playlist.copy(songIds = updatedIds)
            } else playlist
        }
        _songToAddToPlaylist.value = null
        showToast("Added song to playlist!")
    }

    fun removeSongFromPlaylist(playlistId: String, songId: String) {
        _playlists.value = _playlists.value.map { playlist ->
            if (playlist.id == playlistId) {
                playlist.copy(songIds = playlist.songIds.filter { it != songId })
            } else playlist
        }
        if (_selectedPlaylistDetail.value?.id == playlistId) {
            _selectedPlaylistDetail.value = _playlists.value.firstOrNull { it.id == playlistId }
        }
        showToast("Removed song from playlist")
    }

    fun deletePlaylist(playlistId: String) {
        _playlists.value = _playlists.value.filter { it.id != playlistId }
        if (_selectedPlaylistDetail.value?.id == playlistId) {
            _selectedPlaylistDetail.value = null
        }
        showToast("Playlist deleted")
    }

    // Smart Personalized Playlist Generator
    fun generatePersonalizedPlaylist(mood: String, songCount: Int = 5) {
        val pool = _allSongs.value
        val filtered = when (mood) {
            "Energetic Workout" -> pool.filter { it.genre in listOf("Electronic", "Synthwave", "Hip-Hop") }
            "Late Night Neon" -> pool.filter { it.genre in listOf("Synthwave", "Dream Pop") }
            "Deep Focus & Lo-Fi" -> pool.filter { it.genre in listOf("Lo-Fi Chill", "Acoustic", "Classical") }
            "Calm Meditation" -> pool.filter { it.genre in listOf("Acoustic", "Classical", "Dream Pop") }
            else -> pool
        }.shuffled().take(songCount)

        val selectedIds = if (filtered.isNotEmpty()) filtered.map { it.id } else pool.take(songCount).map { it.id }
        val newPlaylist = Playlist(
            id = "ai_playlist_${UUID.randomUUID()}",
            name = "Smart Mix: $mood",
            description = "Intelligently personalized for your taste and current mood ($mood).",
            songIds = selectedIds,
            isCustom = true,
            coverGradientStart = 0xFF7B2CBFL,
            coverGradientEnd = 0xFFFF006EL
        )
        _playlists.value = listOf(newPlaylist) + _playlists.value
        _showPersonalizedGeneratorDialog.value = false
        showToast("Personalized \"$mood\" Mix generated with ${selectedIds.size} songs!")
    }

    // Equalizer
    fun setEqualizerPreset(presetName: String) {
        val current = _equalizerState.value
        val updated = when (presetName) {
            "Bass Boost" -> current.copy(presetName = presetName, band60Hz = 8f, band230Hz = 5f, band910Hz = 0f, band3600Hz = -1f, band14000Hz = 2f, bassBoost = 0.8f)
            "Rock" -> current.copy(presetName = presetName, band60Hz = 6f, band230Hz = 3f, band910Hz = -2f, band3600Hz = 4f, band14000Hz = 6f)
            "Pop" -> current.copy(presetName = presetName, band60Hz = -2f, band230Hz = 2f, band910Hz = 5f, band3600Hz = 3f, band14000Hz = -1f)
            "Electronic" -> current.copy(presetName = presetName, band60Hz = 7f, band230Hz = 4f, band910Hz = 0f, band3600Hz = 3f, band14000Hz = 7f, bassBoost = 0.7f, virtualizer = 0.6f)
            "Jazz" -> current.copy(presetName = presetName, band60Hz = 3f, band230Hz = 2f, band910Hz = -1f, band3600Hz = 3f, band14000Hz = 4f)
            "Vocal" -> current.copy(presetName = presetName, band60Hz = -3f, band230Hz = 0f, band910Hz = 6f, band3600Hz = 5f, band14000Hz = 2f)
            "Classical" -> current.copy(presetName = presetName, band60Hz = 4f, band230Hz = 3f, band910Hz = -1f, band3600Hz = 2f, band14000Hz = 4f)
            else -> current.copy(presetName = "Normal", band60Hz = 0f, band230Hz = 0f, band910Hz = 0f, band3600Hz = 0f, band14000Hz = 0f, bassBoost = 0.3f, virtualizer = 0.3f)
        }
        _equalizerState.value = updated
        audioEngine.setEqualizer(updated)
    }

    fun updateEqualizerBand(bandIndex: Int, value: Float) {
        val current = _equalizerState.value
        val updated = when (bandIndex) {
            0 -> current.copy(presetName = "Custom", band60Hz = value)
            1 -> current.copy(presetName = "Custom", band230Hz = value)
            2 -> current.copy(presetName = "Custom", band910Hz = value)
            3 -> current.copy(presetName = "Custom", band3600Hz = value)
            4 -> current.copy(presetName = "Custom", band14000Hz = value)
            else -> current
        }
        _equalizerState.value = updated
        audioEngine.setEqualizer(updated)
    }

    fun updateBassBoost(value: Float) {
        val updated = _equalizerState.value.copy(bassBoost = value)
        _equalizerState.value = updated
        audioEngine.setEqualizer(updated)
    }

    fun updateVirtualizer(value: Float) {
        val updated = _equalizerState.value.copy(virtualizer = value)
        _equalizerState.value = updated
        audioEngine.setEqualizer(updated)
    }

    fun toggleEqualizer(enabled: Boolean) {
        val updated = _equalizerState.value.copy(isEnabled = enabled)
        _equalizerState.value = updated
        audioEngine.setEqualizer(updated)
        showToast(if (enabled) "Equalizer Enabled" else "Equalizer Bypassed")
    }

    // Sleep Timer
    fun startSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        val totalSec = minutes * 60
        _sleepTimer.value = SleepTimerInfo(
            isActive = true,
            totalSeconds = totalSec,
            remainingSeconds = totalSec,
            targetTimeMs = System.currentTimeMillis() + (totalSec * 1000L)
        )
        _showSleepTimerDialog.value = false
        showToast("Sleep Timer set for $minutes minutes")

        sleepTimerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                val remaining = ((_sleepTimer.value.targetTimeMs - System.currentTimeMillis()) / 1000).toInt()
                if (remaining <= 0) {
                    _sleepTimer.value = SleepTimerInfo(isActive = false, totalSeconds = 0, remainingSeconds = 0)
                    // Auto-pause with gentle fade out
                    _isPlaying.value = false
                    audioEngine.pause()
                    showToast("Sleep Timer completed: Audio stopped.")
                    break
                } else {
                    _sleepTimer.value = _sleepTimer.value.copy(remainingSeconds = remaining)
                }
            }
        }
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        _sleepTimer.value = SleepTimerInfo(isActive = false, totalSeconds = 0, remainingSeconds = 0)
        showToast("Sleep Timer cancelled")
    }

    // Audio Quality & Settings
    fun setAudioQuality(quality: AudioQuality) {
        _audioQuality.value = quality
        showToast("Audio streaming set to ${quality.label} (${quality.bitRate})")
    }

    fun triggerDeviceSync() {
        viewModelScope.launch {
            showToast("Syncing playlists and favorites with cloud devices...")
            delay(1200)
            _crossDeviceSyncStatus.value = "Synced just now • Tablet & Desktop updated"
            showToast("Sync successful! All 3 devices are up to date.")
        }
    }

    fun togglePushNotifications(enabled: Boolean) {
        _pushNotificationsEnabled.value = enabled
        showToast(if (enabled) "Episode & track drop alerts turned ON" else "Alerts turned OFF")
    }

    // Social Media Share
    fun shareSong(context: Context, song: Song) {
        val shareText = "🎵 Listening to \"${song.title}\" by ${song.artist} on Pulse Music!\n" +
                "Album: ${song.album}\n" +
                "Genre: ${song.genre} • High-Fidelity Audio\n\n" +
                "Check it out on Pulse Music!"

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Track via")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    fun shareLyrics(context: Context, song: Song, lyricLine: String) {
        val shareText = "🎶 \"$lyricLine\"\n\n— ${song.title} by ${song.artist} (Pulse Music)"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Lyric via")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    // Navigation and dialogs
    fun setTab(tab: NavigationTab) {
        _currentTab.value = tab
        // Clear details if switching top tabs
        _selectedPlaylistDetail.value = null
        _selectedArtistDetail.value = null
        _selectedAlbumDetail.value = null
    }

    fun selectPlaylist(playlist: Playlist?) { _selectedPlaylistDetail.value = playlist }
    fun selectArtist(artist: Artist?) { _selectedArtistDetail.value = artist }
    fun selectAlbum(album: Album?) { _selectedAlbumDetail.value = album }

    fun setNowPlayingExpanded(expanded: Boolean) { _isNowPlayingExpanded.value = expanded }
    fun setShowLyricsSheet(show: Boolean) { _showLyricsSheet.value = show }
    fun setShowQueueSheet(show: Boolean) { _showQueueSheet.value = show }
    fun setShowEqualizerSheet(show: Boolean) { _showEqualizerSheet.value = show }
    fun setShowSleepTimerDialog(show: Boolean) { _showSleepTimerDialog.value = show }
    fun setShowCreatePlaylistDialog(show: Boolean) { _showCreatePlaylistDialog.value = show }
    fun setSongToAddToPlaylist(song: Song?) { _songToAddToPlaylist.value = song }
    fun setShowPersonalizedGeneratorDialog(show: Boolean) { _showPersonalizedGeneratorDialog.value = show }

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setSelectedGenre(genre: String?) { _selectedGenre.value = genre }
    fun toggleOfflineOnly() {
        _offlineOnlyMode.value = !_offlineOnlyMode.value
        showToast(if (_offlineOnlyMode.value) "Offline Mode Enabled (Showing downloaded tracks)" else "Offline Mode Disabled")
    }

    override fun onCleared() {
        super.onCleared()
        audioEngine.stop()
        sleepTimerJob?.cancel()
    }
}
