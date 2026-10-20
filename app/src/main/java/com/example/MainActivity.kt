package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Song
import com.example.ui.components.AddToPlaylistBottomSheet
import com.example.ui.components.CreatePlaylistDialog
import com.example.ui.components.EqualizerBottomSheet
import com.example.ui.components.LyricsBottomSheet
import com.example.ui.components.MiniPlayer
import com.example.ui.components.PersonalizedPlaylistGeneratorDialog
import com.example.ui.components.SleepTimerDialog
import com.example.ui.screens.ArtistsAlbumsScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NowPlayingScreen
import com.example.ui.screens.PlaylistsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SongsScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.LimeAccent
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.NavigationTab
import com.example.viewmodel.PlayerViewModel

class MainActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PulseMusicApp(viewModel = playerViewModel)
            }
        }
    }
}

@Composable
fun PulseMusicApp(viewModel: PlayerViewModel) {
    val context = LocalContext.current

    // State collections
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val allSongs by viewModel.allSongs.collectAsStateWithLifecycle()
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()
    val albums by viewModel.albums.collectAsStateWithLifecycle()
    val artists by viewModel.artists.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val downloadedSongIds by viewModel.downloadedSongIds.collectAsStateWithLifecycle()
    val downloadProgressMap by viewModel.downloadProgressMap.collectAsStateWithLifecycle()

    val currentSong by viewModel.currentSong.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val currentPositionMs by viewModel.currentPositionMs.collectAsStateWithLifecycle()
    val playbackQueue by viewModel.playbackQueue.collectAsStateWithLifecycle()
    val isShuffle by viewModel.isShuffle.collectAsStateWithLifecycle()
    val repeatMode by viewModel.repeatMode.collectAsStateWithLifecycle()
    val visualizerBars by viewModel.visualizerBars.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()
    val offlineOnlyMode by viewModel.offlineOnlyMode.collectAsStateWithLifecycle()

    val equalizerState by viewModel.equalizerState.collectAsStateWithLifecycle()
    val sleepTimer by viewModel.sleepTimer.collectAsStateWithLifecycle()
    val audioQuality by viewModel.audioQuality.collectAsStateWithLifecycle()
    val syncStatus by viewModel.crossDeviceSyncStatus.collectAsStateWithLifecycle()
    val pushNotificationsEnabled by viewModel.pushNotificationsEnabled.collectAsStateWithLifecycle()

    val isNowPlayingExpanded by viewModel.isNowPlayingExpanded.collectAsStateWithLifecycle()
    val showLyricsSheet by viewModel.showLyricsSheet.collectAsStateWithLifecycle()
    val showEqualizerSheet by viewModel.showEqualizerSheet.collectAsStateWithLifecycle()
    val showSleepTimerDialog by viewModel.showSleepTimerDialog.collectAsStateWithLifecycle()
    val showCreatePlaylistDialog by viewModel.showCreatePlaylistDialog.collectAsStateWithLifecycle()
    val songToAddToPlaylist by viewModel.songToAddToPlaylist.collectAsStateWithLifecycle()
    val showPersonalizedGeneratorDialog by viewModel.showPersonalizedGeneratorDialog.collectAsStateWithLifecycle()

    val selectedPlaylistDetail by viewModel.selectedPlaylistDetail.collectAsStateWithLifecycle()
    val selectedArtistDetail by viewModel.selectedArtistDetail.collectAsStateWithLifecycle()
    val selectedAlbumDetail by viewModel.selectedAlbumDetail.collectAsStateWithLifecycle()

    val statusMessage by viewModel.statusMessage.collectAsStateWithLifecycle()

    val downloadedSongs = allSongs.filter { downloadedSongIds.contains(it.id) }
    val favoriteSongs = allSongs.filter { favorites.contains(it.id) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = DarkBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                Column(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    // Persistent Mini Player above bottom nav
                    if (currentSong != null && !isNowPlayingExpanded) {
                        MiniPlayer(
                            song = currentSong,
                            isPlaying = isPlaying,
                            currentPositionMs = currentPositionMs,
                            visualizerBars = visualizerBars,
                            onPlayPauseClick = { viewModel.togglePlayPause() },
                            onNextClick = { viewModel.playNext() },
                            onFavoriteClick = { currentSong?.let { viewModel.toggleFavorite(it.id) } },
                            onMiniPlayerClick = { viewModel.setNowPlayingExpanded(true) }
                        )
                    }

                    // Bottom Navigation Bar
                    NavigationBar(
                        containerColor = DarkSurface,
                        tonalElevation = 6.dp,
                        modifier = Modifier.testTag("bottom_nav_bar")
                    ) {
                        NavigationBarItem(
                            selected = currentTab == NavigationTab.HOME,
                            onClick = { viewModel.setTab(NavigationTab.HOME) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ElectricCyan,
                                selectedTextColor = ElectricCyan,
                                indicatorColor = DarkSurfaceElevated,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                        NavigationBarItem(
                            selected = currentTab == NavigationTab.SONGS,
                            onClick = { viewModel.setTab(NavigationTab.SONGS) },
                            icon = { Icon(Icons.Default.MusicNote, contentDescription = "Songs") },
                            label = { Text("Songs", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ElectricCyan,
                                selectedTextColor = ElectricCyan,
                                indicatorColor = DarkSurfaceElevated,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                        NavigationBarItem(
                            selected = currentTab == NavigationTab.PLAYLISTS,
                            onClick = { viewModel.setTab(NavigationTab.PLAYLISTS) },
                            icon = { Icon(Icons.Default.PlaylistPlay, contentDescription = "Playlists") },
                            label = { Text("Playlists", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ElectricCyan,
                                selectedTextColor = ElectricCyan,
                                indicatorColor = DarkSurfaceElevated,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                        NavigationBarItem(
                            selected = currentTab == NavigationTab.ARTISTS,
                            onClick = { viewModel.setTab(NavigationTab.ARTISTS) },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Artists") },
                            label = { Text("Artists", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ElectricCyan,
                                selectedTextColor = ElectricCyan,
                                indicatorColor = DarkSurfaceElevated,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                        NavigationBarItem(
                            selected = currentTab == NavigationTab.DOWNLOADS,
                            onClick = { viewModel.setTab(NavigationTab.DOWNLOADS) },
                            icon = { Icon(Icons.Default.DownloadDone, contentDescription = "Downloads") },
                            label = { Text("Downloads", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = LimeAccent,
                                selectedTextColor = LimeAccent,
                                indicatorColor = DarkSurfaceElevated,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                        NavigationBarItem(
                            selected = currentTab == NavigationTab.SETTINGS,
                            onClick = { viewModel.setTab(NavigationTab.SETTINGS) },
                            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                            label = { Text("Settings", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ElectricCyan,
                                selectedTextColor = ElectricCyan,
                                indicatorColor = DarkSurfaceElevated,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding()
            ) {
                when (currentTab) {
                    NavigationTab.HOME -> HomeScreen(
                        songs = allSongs,
                        playlists = playlists,
                        artists = artists,
                        albums = albums,
                        currentSong = currentSong,
                        isPlaying = isPlaying,
                        onSongClick = { s -> viewModel.playSong(s, allSongs) },
                        onPlaylistClick = { p ->
                            viewModel.selectPlaylist(p)
                            viewModel.setTab(NavigationTab.PLAYLISTS)
                        },
                        onArtistClick = { a ->
                            viewModel.selectArtist(a)
                            viewModel.setTab(NavigationTab.ARTISTS)
                        },
                        onAlbumClick = { al ->
                            viewModel.selectAlbum(al)
                            viewModel.setTab(NavigationTab.ARTISTS)
                        },
                        onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                        onDownloadSong = { s -> viewModel.downloadSong(s) },
                        onAddToPlaylist = { s -> viewModel.setSongToAddToPlaylist(s) },
                        onShareSong = { s -> viewModel.shareSong(context, s) },
                        onOpenPersonalizedGenerator = { viewModel.setShowPersonalizedGeneratorDialog(true) },
                        onOpenEqualizer = { viewModel.setShowEqualizerSheet(true) },
                        onOpenSleepTimer = { viewModel.setShowSleepTimerDialog(true) },
                        downloadProgressMap = downloadProgressMap
                    )

                    NavigationTab.SONGS -> SongsScreen(
                        songs = allSongs,
                        searchQuery = searchQuery,
                        selectedGenre = selectedGenre,
                        offlineOnlyMode = offlineOnlyMode,
                        currentSong = currentSong,
                        isPlaying = isPlaying,
                        onSearchQueryChange = { q -> viewModel.setSearchQuery(q) },
                        onGenreSelected = { g -> viewModel.setSelectedGenre(g) },
                        onToggleOfflineOnly = { viewModel.toggleOfflineOnly() },
                        onSongClick = { s -> viewModel.playSong(s, allSongs) },
                        onPlayAll = {
                            if (allSongs.isNotEmpty()) viewModel.playSong(allSongs.first(), allSongs)
                        },
                        onShuffleAll = {
                            if (allSongs.isNotEmpty()) {
                                val shuffled = allSongs.shuffled()
                                viewModel.playSong(shuffled.first(), shuffled)
                            }
                        },
                        onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                        onDownloadSong = { s -> viewModel.downloadSong(s) },
                        onAddToPlaylist = { s -> viewModel.setSongToAddToPlaylist(s) },
                        onShareSong = { s -> viewModel.shareSong(context, s) },
                        downloadProgressMap = downloadProgressMap
                    )

                    NavigationTab.PLAYLISTS -> PlaylistsScreen(
                        playlists = playlists,
                        allSongs = allSongs,
                        selectedPlaylist = selectedPlaylistDetail,
                        currentSong = currentSong,
                        isPlaying = isPlaying,
                        onSelectPlaylist = { p -> viewModel.selectPlaylist(p) },
                        onCreatePlaylistClick = { viewModel.setShowCreatePlaylistDialog(true) },
                        onDeletePlaylist = { id -> viewModel.deletePlaylist(id) },
                        onRemoveSongFromPlaylist = { pId, sId -> viewModel.removeSongFromPlaylist(pId, sId) },
                        onSongClick = { s, queue -> viewModel.playSong(s, queue) },
                        onPlayAllPlaylist = { songsList ->
                            if (songsList.isNotEmpty()) viewModel.playSong(songsList.first(), songsList)
                        },
                        onShufflePlaylist = { songsList ->
                            if (songsList.isNotEmpty()) {
                                val shuffled = songsList.shuffled()
                                viewModel.playSong(shuffled.first(), shuffled)
                            }
                        },
                        onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                        onDownloadSong = { s -> viewModel.downloadSong(s) },
                        onAddToPlaylist = { s -> viewModel.setSongToAddToPlaylist(s) },
                        onShareSong = { s -> viewModel.shareSong(context, s) },
                        downloadProgressMap = downloadProgressMap
                    )

                    NavigationTab.ARTISTS -> ArtistsAlbumsScreen(
                        artists = artists,
                        albums = albums,
                        allSongs = allSongs,
                        selectedArtist = selectedArtistDetail,
                        selectedAlbum = selectedAlbumDetail,
                        currentSong = currentSong,
                        isPlaying = isPlaying,
                        onSelectArtist = { a -> viewModel.selectArtist(a) },
                        onSelectAlbum = { al -> viewModel.selectAlbum(al) },
                        onSongClick = { s, queue -> viewModel.playSong(s, queue) },
                        onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                        onDownloadSong = { s -> viewModel.downloadSong(s) },
                        onAddToPlaylist = { s -> viewModel.setSongToAddToPlaylist(s) },
                        onShareSong = { s -> viewModel.shareSong(context, s) },
                        downloadProgressMap = downloadProgressMap
                    )

                    NavigationTab.DOWNLOADS -> DownloadsScreen(
                        downloadedSongs = downloadedSongs,
                        favoriteSongs = favoriteSongs,
                        offlineOnlyMode = offlineOnlyMode,
                        currentSong = currentSong,
                        isPlaying = isPlaying,
                        onToggleOfflineOnly = { viewModel.toggleOfflineOnly() },
                        onDownloadSong = { s -> viewModel.downloadSong(s) },
                        onSongClick = { s -> viewModel.playSong(s, downloadedSongs) },
                        onPlayAll = {
                            if (downloadedSongs.isNotEmpty()) viewModel.playSong(downloadedSongs.first(), downloadedSongs)
                        },
                        onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                        onAddToPlaylist = { s -> viewModel.setSongToAddToPlaylist(s) },
                        onShareSong = { s -> viewModel.shareSong(context, s) },
                        downloadProgressMap = downloadProgressMap
                    )

                    NavigationTab.SETTINGS -> SettingsScreen(
                        currentQuality = audioQuality,
                        syncStatus = syncStatus,
                        pushNotificationsEnabled = pushNotificationsEnabled,
                        onSetAudioQuality = { q -> viewModel.setAudioQuality(q) },
                        onTriggerSync = { viewModel.triggerDeviceSync() },
                        onTogglePushNotifications = { e -> viewModel.togglePushNotifications(e) },
                        onOpenEqualizer = { viewModel.setShowEqualizerSheet(true) },
                        onOpenSleepTimer = { viewModel.setShowSleepTimerDialog(true) }
                    )
                }

                // Toast status banner
                AnimatedVisibility(
                    visible = statusMessage != null,
                    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                ) {
                    statusMessage?.let { msg ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DarkSurfaceElevated,
                            shadowElevation = 8.dp,
                            modifier = Modifier.fillMaxWidth(0.92f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = msg,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Full Screen Now Playing Animated Modal
        AnimatedVisibility(
            visible = isNowPlayingExpanded,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            NowPlayingScreen(
                song = currentSong,
                isPlaying = isPlaying,
                currentPositionMs = currentPositionMs,
                visualizerBars = visualizerBars,
                isShuffle = isShuffle,
                repeatMode = repeatMode,
                audioQuality = audioQuality,
                sleepTimerActive = sleepTimer.isActive,
                queue = playbackQueue,
                onCollapse = { viewModel.setNowPlayingExpanded(false) },
                onPlayPause = { viewModel.togglePlayPause() },
                onNext = { viewModel.playNext() },
                onPrevious = { viewModel.playPrevious() },
                onSeek = { pos -> viewModel.seekTo(pos) },
                onToggleShuffle = { viewModel.toggleShuffle() },
                onToggleRepeat = { viewModel.toggleRepeatMode() },
                onToggleFavorite = { currentSong?.let { viewModel.toggleFavorite(it.id) } },
                onOpenLyrics = { viewModel.setShowLyricsSheet(true) },
                onOpenEqualizer = { viewModel.setShowEqualizerSheet(true) },
                onOpenSleepTimer = { viewModel.setShowSleepTimerDialog(true) },
                onOpenShare = { currentSong?.let { viewModel.shareSong(context, it) } },
                onPlayQueueSong = { s -> viewModel.playSong(s, playbackQueue) }
            )
        }

        // Lyrics Bottom Sheet
        if (showLyricsSheet) {
            LyricsBottomSheet(
                song = currentSong,
                currentPositionMs = currentPositionMs,
                onSeekTo = { pos -> viewModel.seekTo(pos) },
                onShareLyric = { line -> currentSong?.let { viewModel.shareLyrics(context, it, line) } },
                onDismiss = { viewModel.setShowLyricsSheet(false) }
            )
        }

        // Equalizer Bottom Sheet
        if (showEqualizerSheet) {
            EqualizerBottomSheet(
                equalizerState = equalizerState,
                onPresetSelected = { preset -> viewModel.setEqualizerPreset(preset) },
                onBandChange = { band, value -> viewModel.updateEqualizerBand(band, value) },
                onBassBoostChange = { bb -> viewModel.updateBassBoost(bb) },
                onVirtualizerChange = { v -> viewModel.updateVirtualizer(v) },
                onToggleEqualizer = { enabled -> viewModel.toggleEqualizer(enabled) },
                onDismiss = { viewModel.setShowEqualizerSheet(false) }
            )
        }

        // Sleep Timer Dialog
        if (showSleepTimerDialog) {
            SleepTimerDialog(
                sleepTimerInfo = sleepTimer,
                onStartTimer = { mins -> viewModel.startSleepTimer(mins) },
                onCancelTimer = { viewModel.cancelSleepTimer() },
                onDismiss = { viewModel.setShowSleepTimerDialog(false) }
            )
        }

        // Create Playlist Dialog
        if (showCreatePlaylistDialog) {
            CreatePlaylistDialog(
                onConfirm = { name, desc -> viewModel.createPlaylist(name, desc) },
                onDismiss = { viewModel.setShowCreatePlaylistDialog(false) }
            )
        }

        // Add to Playlist Bottom Sheet
        if (songToAddToPlaylist != null) {
            AddToPlaylistBottomSheet(
                song = songToAddToPlaylist,
                playlists = playlists,
                onPlaylistSelected = { pId ->
                    songToAddToPlaylist?.let { s -> viewModel.addSongToPlaylist(pId, s.id) }
                },
                onCreateNewClick = { viewModel.setShowCreatePlaylistDialog(true) },
                onDismiss = { viewModel.setSongToAddToPlaylist(null) }
            )
        }

        // Personalized Generator Dialog
        if (showPersonalizedGeneratorDialog) {
            PersonalizedPlaylistGeneratorDialog(
                onGenerate = { mood, count -> viewModel.generatePersonalizedPlaylist(mood, count) },
                onDismiss = { viewModel.setShowPersonalizedGeneratorDialog(false) }
            )
        }
    }
}
