package com.github.pamugk.polyhymniamusicplayer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Text
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

@Composable
fun AlbumsScreen(
    mediaBrowser: MediaBrowser,
    onAlbumSelected: (String) -> Unit = {},
    padding: PaddingValues = PaddingValues()
) {
    val result: Result<List<MediaItem>> by produceState(initialValue = Result(state = Result.State.LOADING)) {
        value = try {
            val response = mediaBrowser.getChildren("albums", 0, 100, null).await()
            Result(data = response.value, state = Result.State.SUCCESS)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result(state = Result.State.ERROR)
        }
    }

    when (result.state) {
        Result.State.ERROR -> StatusPage(
            description = stringResource(R.string.error_occured),
            icon = Icons.Default.Error,
            text = stringResource(R.string.error_occured)
        )
        Result.State.LOADING -> Loader(
            modifier = Modifier.padding(padding),
            text = stringResource(R.string.albums_loading)
        )
        Result.State.SUCCESS -> {
            val albums = result.data
            if (albums.isNullOrEmpty()) {
                StatusPage(
                    description = stringResource(R.string.nothing_found),
                    icon = Icons.Default.Warning,
                    text = stringResource(R.string.no_albums_found)
                )
            } else {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(albums) { album ->
                        Text(
                            text = album.mediaMetadata.albumTitle?.toString() ?: stringResource(R.string.no_album),
                            modifier = Modifier.clickable { onAlbumSelected(album.mediaId) }
                        )
                    }
                }
            }
        }
    }
}