package com.github.pamugk.polyhymniamusicplayer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import com.github.pamugk.polyhymniamusicplayer.R
import com.github.pamugk.polyhymniamusicplayer.ui.components.Loader
import com.github.pamugk.polyhymniamusicplayer.ui.components.StatusPage
import com.github.pamugk.polyhymniamusicplayer.ui.data.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.guava.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    id: String,
    mediaBrowser: MediaBrowser,
    onGoBack: () -> Unit = {},
    padding: PaddingValues = PaddingValues()
) {
    val result: Result<List<MediaItem>> by produceState(initialValue = Result(state = Result.State.LOADING)) {
        value = try {
            val response = mediaBrowser.getChildren("artists/$id", 0, 100, null).await()
            Result(data = response.value, state = Result.State.SUCCESS)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result(state = Result.State.ERROR)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                }
            )
        },
        modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
    ) { innerPadding ->
        when (result.state) {
            Result.State.ERROR -> StatusPage(
                description = stringResource(R.string.error_occured),
                icon = Icons.Default.Error,
                text = stringResource(R.string.error_occured)
            )
            Result.State.LOADING -> Loader(
                modifier = Modifier.padding(padding),
                text = stringResource(R.string.tracks_loading)
            )
            Result.State.SUCCESS -> {
                val tracks = result.data
                if (tracks.isNullOrEmpty()) {
                    StatusPage(
                        description = stringResource(R.string.nothing_found),
                        icon = Icons.Default.Warning,
                        text = stringResource(R.string.no_tracks_found)
                    )
                } else {
                    LazyColumn(modifier = Modifier.padding(innerPadding)) {
                        items(tracks) { track ->
                            Text(
                                text = track.mediaMetadata.title?.toString() ?: stringResource(R.string.no_track),
                                modifier = Modifier.clickable {
                                    mediaBrowser.setMediaItem(track)
                                })
                        }
                    }
                }
            }
        }
    }
}