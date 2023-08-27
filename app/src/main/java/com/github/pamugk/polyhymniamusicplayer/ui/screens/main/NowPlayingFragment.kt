package com.github.pamugk.polyhymniamusicplayer.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun NowPlayingFragment(padding: PaddingValues = PaddingValues()) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            imageVector = Icons.Default.BrokenImage,
            contentDescription = "Album cover",
            modifier = Modifier.fillMaxSize(0.8f),
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
        )
        Text(text = "No track is playing")
        Text(text = "Unknown album")
        Text(text = "Unknown artist")
        Slider(
            modifier = Modifier.semantics { contentDescription = "Playback slider" },
            onValueChange = {},
            value = 0.0f,
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                )
            }
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    Icons.Default.FastRewind,
                    contentDescription = "Previous",
                )
            }
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play",
                )
            }
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    Icons.Default.FastForward,
                    contentDescription = "Next",
                )
            }
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    Icons.Default.SkipNext,
                    contentDescription = "Next",
                )
            }
        }
    }
}

@Composable
@Preview
private fun NowPlayingFragmentPreview() {
    MaterialTheme {
        NowPlayingFragment()
    }
}