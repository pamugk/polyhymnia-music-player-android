package com.github.pamugk.polyhymniamusicplayer.ui.screens.main

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
import com.github.pamugk.polyhymniamusicplayer.data.datasource.getArtists

@Composable
internal fun ArtistsFragment(padding: PaddingValues = PaddingValues()) {
    val context = LocalContext.current.applicationContext
    val artists by produceState(initialValue = emptyList<MediaItem>()) {
        value = context.contentResolver.getArtists()
    }

    LazyColumn(modifier = Modifier.padding(padding)) {
        items(artists) { artist ->
            Text(text = artist.mediaMetadata.artist?.toString() ?: "Unknown artist")
        }
    }
}