package com.github.pamugk.polyhymniamusicplayer.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import com.github.pamugk.polyhymniamusicplayer.R
import com.github.pamugk.polyhymniamusicplayer.ui.components.GenreListItem
import com.github.pamugk.polyhymniamusicplayer.ui.components.Loader
import com.github.pamugk.polyhymniamusicplayer.ui.components.StatusPage
import com.github.pamugk.polyhymniamusicplayer.ui.data.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.guava.await

@Composable
fun GenresScreen(
    mediaBrowser: MediaBrowser,
    onGenreSelected: (String) -> Unit = {},
    padding: PaddingValues = PaddingValues()
) {
    val result: Result<List<MediaItem>> by produceState(initialValue = Result(state = Result.State.LOADING)) {
        value = try {
            val response = mediaBrowser.getChildren("genres", 0, 100, null).await()
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
            text = stringResource(R.string.genres_loading)
        )
        Result.State.SUCCESS -> {
            val genres = result.data
            if (genres.isNullOrEmpty()) {
                StatusPage(
                    description = stringResource(R.string.nothing_found),
                    icon = Icons.Default.Warning,
                    text = stringResource(R.string.no_genres_found)
                )
            } else {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(genres) { genre ->
                        GenreListItem(genre = genre, onClick = { onGenreSelected(genre.mediaId) })
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}