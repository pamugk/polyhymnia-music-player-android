package com.github.pamugk.polyhymniamusicplayer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import kotlinx.coroutines.guava.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    id: String,
    mediaBrowser: MediaBrowser,
    onGoBack: () -> Unit = {},
    padding: PaddingValues = PaddingValues()
) {
    val tracks by produceState(initialValue = emptyList<MediaItem>()) {
        val response = mediaBrowser.getChildren("artists/$id", 0, 100, null).await()
        value = response.value ?: emptyList()
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