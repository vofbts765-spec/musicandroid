package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Album
import com.example.model.Artist
import com.example.model.Song
import com.example.ui.components.AlbumArtBox
import com.example.ui.components.TrackRow
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ArtistsAlbumsScreen(
    artists: List<Artist>,
    albums: List<Album>,
    allSongs: List<Song>,
    selectedArtist: Artist?,
    selectedAlbum: Album?,
    currentSong: Song?,
    isPlaying: Boolean,
    onSelectArtist: (Artist?) -> Unit,
    onSelectAlbum: (Album?) -> Unit,
    onSongClick: (Song, List<Song>) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onDownloadSong: (Song) -> Unit,
    onAddToPlaylist: (Song) -> Unit,
    onShareSong: (Song) -> Unit,
    downloadProgressMap: Map<String, Float>,
    modifier: Modifier = Modifier
) {
    // Artist Detail View
    if (selectedArtist != null) {
        BackHandler { onSelectArtist(null) }

        val artistSongs = allSongs.filter { it.artist.equals(selectedArtist.name, ignoreCase = true) }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(DarkBackground),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                IconButton(
                    onClick = { onSelectArtist(null) },
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(Color(selectedArtist.primaryColorHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selectedArtist.name.take(2).uppercase(),
                            color = Color.White,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = selectedArtist.name,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Text(
                        text = "${selectedArtist.monthlyListeners} Monthly Listeners",
                        color = ElectricCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = selectedArtist.bio,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    if (artistSongs.isNotEmpty()) {
                        Button(
                            onClick = { onSongClick(artistSongs.first(), artistSongs) },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = Color(0xFF003830)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(0.6f)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Play Artist Top Tracks", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            items(artistSongs) { song ->
                val isPlayingThis = currentSong?.id == song.id
                TrackRow(
                    song = song,
                    isPlayingThisSong = isPlayingThis,
                    isCurrentlyPlaying = isPlaying && isPlayingThis,
                    onTrackClick = { onSongClick(song, artistSongs) },
                    onToggleFavorite = { onToggleFavorite(song.id) },
                    onDownloadClick = { onDownloadSong(song) },
                    onAddToPlaylistClick = { onAddToPlaylist(song) },
                    onShareClick = { onShareSong(song) },
                    downloadProgress = downloadProgressMap[song.id],
                    modifier = Modifier.padding(horizontal = 14.dp)
                )
            }
        }
        return
    }

    // Album Detail View
    if (selectedAlbum != null) {
        BackHandler { onSelectAlbum(null) }

        val albumSongs = allSongs.filter { it.album.equals(selectedAlbum.title, ignoreCase = true) }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(DarkBackground),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                IconButton(
                    onClick = { onSelectAlbum(null) },
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AlbumArtBox(
                        primaryColorHex = selectedAlbum.primaryColorHex,
                        secondaryColorHex = selectedAlbum.secondaryColorHex,
                        modifier = Modifier.size(160.dp),
                        iconSize = 64.dp,
                        showGlow = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = selectedAlbum.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Text(
                        text = "By ${selectedAlbum.artist} • Released ${selectedAlbum.year}",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (albumSongs.isNotEmpty()) {
                        Button(
                            onClick = { onSongClick(albumSongs.first(), albumSongs) },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = Color(0xFF003830)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(0.6f)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Play Album", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            items(albumSongs) { song ->
                val isPlayingThis = currentSong?.id == song.id
                TrackRow(
                    song = song,
                    isPlayingThisSong = isPlayingThis,
                    isCurrentlyPlaying = isPlaying && isPlayingThis,
                    onTrackClick = { onSongClick(song, albumSongs) },
                    onToggleFavorite = { onToggleFavorite(song.id) },
                    onDownloadClick = { onDownloadSong(song) },
                    onAddToPlaylistClick = { onAddToPlaylist(song) },
                    onShareClick = { onShareSong(song) },
                    downloadProgress = downloadProgressMap[song.id],
                    modifier = Modifier.padding(horizontal = 14.dp)
                )
            }
        }
        return
    }

    // Main Artists / Albums Tabs
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Artists", "Albums")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Artists & Albums",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
        }

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkBackground,
            contentColor = ElectricCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = ElectricCyan
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) ElectricCyan else TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }

        if (selectedTab == 0) {
            // Artists List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
            ) {
                items(artists) { artist ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onSelectArtist(artist) },
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color(artist.primaryColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = artist.name.take(2).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = artist.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${artist.monthlyListeners} monthly listeners",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Albums List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp, start = 20.dp, end = 20.dp, top = 12.dp)
            ) {
                items(albums.chunked(2)) { pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        pair.forEach { album ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { onSelectAlbum(album) },
                                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    AlbumArtBox(
                                        primaryColorHex = album.primaryColorHex,
                                        secondaryColorHex = album.secondaryColorHex,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(130.dp),
                                        iconSize = 44.dp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = album.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${album.artist} • ${album.year}",
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
