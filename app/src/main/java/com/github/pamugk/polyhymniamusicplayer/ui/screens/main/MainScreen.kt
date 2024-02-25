package com.github.pamugk.polyhymniamusicplayer.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.session.MediaController

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
fun MainScreen(controller: MediaController?) {
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
                controller = controller,
                padding = innerPadding
            )
            Fragment.TRACKS -> TracksFragment(padding = innerPadding)
            Fragment.ALBUMS -> AlbumsFragment(padding = innerPadding)
            Fragment.ARTISTS -> ArtistsFragment(padding = innerPadding)
            Fragment.GENRES -> GenresFragment(padding = innerPadding)
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