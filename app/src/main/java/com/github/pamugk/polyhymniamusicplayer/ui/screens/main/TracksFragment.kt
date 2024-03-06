package com.github.pamugk.polyhymniamusicplayer.ui.screens.main

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
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.github.pamugk.polyhymniamusicplayer.data.datasource.getTracks

@Composable
internal fun TracksFragment(controller: MediaController? = null, padding: PaddingValues = PaddingValues()) {
    val context = LocalContext.current
    val tracks by produceState(initialValue = emptyList<MediaItem>()) {
        value = context.contentResolver.getTracks()
    }

    LazyColumn(modifier = Modifier.padding(padding)) {
        items(tracks) { track ->
            Text(
                text = track.mediaMetadata.title?.toString() ?: "Unknown track",
                modifier = Modifier.clickable {
                    controller?.setMediaItem(track)
                })
        }
    }
}