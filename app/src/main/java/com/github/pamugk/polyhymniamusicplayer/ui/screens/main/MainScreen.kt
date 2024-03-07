package com.github.pamugk.polyhymniamusicplayer.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.session.MediaBrowser

private enum class Fragment {
    NOW_PLAYING,
    TRACKS,
    ALBUMS,
    ARTISTS,
    GENRES
}

private data class Destination(
    val fragment: Fragment,
    val description: String,
    val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mediaBrowser: MediaBrowser?) {
    if (mediaBrowser == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.width(128.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Player initialization…")
        }
    } else {
        var searchBarActive by remember { mutableStateOf(false) }
        var searchBarQuery by remember { mutableStateOf("") }

        var currentFragment by remember { mutableStateOf(Fragment.NOW_PLAYING) }
        val destinations = listOf(
            Destination(Fragment.NOW_PLAYING, "Now Playing", Icons.Default.PlayCircle),
            Destination(Fragment.TRACKS, "Tracks", Icons.Default.Audiotrack),
            Destination(Fragment.ALBUMS, "Albums", Icons.Default.Album),
            Destination(Fragment.ARTISTS, "Artists", Icons.Default.Person),
            Destination(Fragment.GENRES, "Genres", Icons.AutoMirrored.Filled.Label),
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        SearchBar(
                            query = searchBarQuery,
                            onQueryChange = { searchBarQuery = it },
                            onSearch = {},
                            active = false,
                            onActiveChange = { searchBarActive = it },
                            placeholder = { Text(text = "Search for music...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        ) {
                        }
                    },
                    actions = {
                        if (currentFragment != Fragment.NOW_PLAYING) {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Sort,
                                    contentDescription = "Settings",
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    destinations.forEach { destination ->
                        NavigationBarItem(
                            selected = currentFragment == destination.fragment,
                            onClick = { currentFragment = destination.fragment },
                            icon = { Icon(imageVector = destination.icon, contentDescription = destination.description) })
                    }
                }
            }
        ) { innerPadding ->
            when(currentFragment) {
                Fragment.NOW_PLAYING -> NowPlayingFragment(
                    player = mediaBrowser,
                    padding = innerPadding
                )
                Fragment.TRACKS -> TracksFragment(mediaBrowser = mediaBrowser, padding = innerPadding)
                Fragment.ALBUMS -> AlbumsFragment(mediaBrowser = mediaBrowser, padding = innerPadding)
                Fragment.ARTISTS -> ArtistsFragment(mediaBrowser = mediaBrowser, padding = innerPadding)
                Fragment.GENRES -> GenresFragment(mediaBrowser = mediaBrowser, padding = innerPadding)
            }
        }
    }
}

@Composable
@Preview
private fun MainScreePreview() {
    MaterialTheme {
        MainScreen(null)
    }
}