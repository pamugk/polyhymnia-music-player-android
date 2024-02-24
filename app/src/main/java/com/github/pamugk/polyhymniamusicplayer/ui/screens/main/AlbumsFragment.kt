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
import com.github.pamugk.polyhymniamusicplayer.data.datasource.getAlbums
import com.github.pamugk.polyhymniamusicplayer.data.datasource.getArtists
import com.github.pamugk.polyhymniamusicplayer.data.entity.Album
import com.github.pamugk.polyhymniamusicplayer.data.entity.Artist

@Composable
internal fun AlbumsFragment(padding: PaddingValues = PaddingValues()) {
    val context = LocalContext.current.applicationContext
    val albums by produceState(initialValue = emptyList<Album>()) {
        value = context.contentResolver.getAlbums()
    }

    LazyColumn(modifier = Modifier.padding(padding)) {
        items(albums) { album ->
            Text(text = album.title ?: "Unknown album")
        }
    }
}