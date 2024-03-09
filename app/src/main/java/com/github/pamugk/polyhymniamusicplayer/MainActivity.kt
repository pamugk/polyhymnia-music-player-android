package com.github.pamugk.polyhymniamusicplayer

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.media3.session.MediaBrowser
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.github.pamugk.polyhymniamusicplayer.data.controller.rememberMediaBrowser
import com.github.pamugk.polyhymniamusicplayer.ui.components.Loader
import com.github.pamugk.polyhymniamusicplayer.ui.components.StatusPage
import com.github.pamugk.polyhymniamusicplayer.ui.screens.AlbumScreen
import com.github.pamugk.polyhymniamusicplayer.ui.screens.AlbumsScreen
import com.github.pamugk.polyhymniamusicplayer.ui.screens.ArtistScreen
import com.github.pamugk.polyhymniamusicplayer.ui.screens.ArtistsScreen
import com.github.pamugk.polyhymniamusicplayer.ui.screens.GenreScreen
import com.github.pamugk.polyhymniamusicplayer.ui.screens.GenresScreen
import com.github.pamugk.polyhymniamusicplayer.ui.screens.NowPlayingScreen
import com.github.pamugk.polyhymniamusicplayer.ui.screens.TracksScreen

class MainActivity : ComponentActivity() {

    private val storagePermission: String
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                READ_MEDIA_AUDIO
            else READ_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var enoughPermissions by remember {
                mutableStateOf(ContextCompat.checkSelfPermission(
                    this,
                    storagePermission) == PackageManager.PERMISSION_GRANTED)
            }
            val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                enoughPermissions = it
            }
            LaunchedEffect(Unit) {
                permissionLauncher.launch(storagePermission)
            }

            MaterialTheme {
                if (enoughPermissions) {
                    val controller by rememberMediaBrowser()
                    ApplicationRoot(controller)
                } else {
                    StatusPage(
                        description = stringResource(R.string.no_permission),
                        icon = Icons.Default.Block,
                        text = stringResource(R.string.not_enough_permissions)
                    )
                }
            }
        }
    }
}

private sealed class Destination(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    data object Albums : Destination("/albums", R.string.albums, Icons.Default.Album)
    data object Artists : Destination("/artists", R.string.artists, Icons.Default.Person)
    data object Genres : Destination("/genres", R.string.genres, Icons.AutoMirrored.Filled.Label)
    data object NowPlaying : Destination("/now-playing", R.string.now_playing, Icons.Default.PlayCircle)
    data object Tracks : Destination("/tracks", R.string.tracks, Icons.Default.Audiotrack)
}

@Composable
private fun ApplicationRoot(mediaBrowser: MediaBrowser? = null) {
    if (mediaBrowser == null) {
        Loader(text = stringResource(R.string.player_initialization))
    } else {
        val destinations = listOf(
            Destination.NowPlaying,
            Destination.Tracks,
            Destination.Albums,
            Destination.Artists,
            Destination.Genres
        )

        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    destinations.forEach { destination ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = stringResource(destination.resourceId)
                                )
                            })
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Destination.NowPlaying.route
            ) {
                navigation(startDestination = "/albums/", route = "/albums") {
                    composable("/albums/") {
                        AlbumsScreen(
                            mediaBrowser = mediaBrowser,
                            onAlbumSelected = { id -> navController.navigate("/albums/$id") },
                            padding = innerPadding
                        )
                    }
                    composable("/albums/{id}") { backStackEntry ->
                        AlbumScreen(
                            id = backStackEntry.arguments!!.getString("id")!!,
                            mediaBrowser = mediaBrowser,
                            onGoBack = { navController.popBackStack() },
                            padding = innerPadding
                        )
                    }
                }
                navigation(startDestination = "/artists/", route = "/artists") {
                    composable("/artists/") {
                        ArtistsScreen(
                            mediaBrowser = mediaBrowser,
                            onArtistSelected = { id -> navController.navigate("/artists/$id") },
                            padding = innerPadding
                        )
                    }
                    composable("/artists/{id}") { backStackEntry ->
                        ArtistScreen(
                            id = backStackEntry.arguments!!.getString("id")!!,
                            mediaBrowser = mediaBrowser,
                            onGoBack = { navController.popBackStack() },
                            padding = innerPadding
                        )
                    }
                }
                navigation(startDestination = "/genres/", route = "/genres") {
                    composable("/genres/") {
                        GenresScreen(
                            mediaBrowser = mediaBrowser,
                            onGenreSelected = { id -> navController.navigate("/genres/$id") },
                            padding = innerPadding
                        )
                    }
                    composable("/genres/{id}") { backStackEntry ->
                        GenreScreen(
                            id = backStackEntry.arguments!!.getString("id")!!,
                            mediaBrowser = mediaBrowser,
                            onGoBack = { navController.popBackStack() },
                            padding = innerPadding
                        )
                    }
                }
                composable("/now-playing") {
                    NowPlayingScreen(player = mediaBrowser, padding = innerPadding)
                }
                composable("/tracks") {
                    TracksScreen(mediaBrowser = mediaBrowser, padding = innerPadding)
                }
            }
        }
    }
}