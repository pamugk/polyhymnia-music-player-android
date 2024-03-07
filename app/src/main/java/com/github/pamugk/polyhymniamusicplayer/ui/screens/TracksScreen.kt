package com.github.pamugk.polyhymniamusicplayer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import com.github.pamugk.polyhymniamusicplayer.R
import kotlinx.coroutines.guava.await

@Composable
fun TracksScreen(
    mediaBrowser: MediaBrowser,
    padding: PaddingValues = PaddingValues()
) {
    val tracks by produceState(initialValue = emptyList<MediaItem>()) {
        val response = mediaBrowser.getChildren("tracks", 0, 100, null).await()
        value = response.value ?: emptyList()
    }

    LazyColumn(modifier = Modifier.padding(padding)) {
        items(tracks) { track ->
            Text(
                text = track.mediaMetadata.title?.toString() ?: stringResource(R.string.no_track),
                modifier = Modifier.clickable {
                    mediaBrowser.setMediaItem(track)
                })
        }
    }
}