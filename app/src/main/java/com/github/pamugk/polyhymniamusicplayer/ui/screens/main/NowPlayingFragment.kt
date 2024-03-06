package com.github.pamugk.polyhymniamusicplayer.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.github.pamugk.polyhymniamusicplayer.ui.state.rememberMediaState
import kotlin.math.max

@Composable
internal fun NowPlayingFragment(
    controller: Player? = null,
    padding: PaddingValues = PaddingValues()
) {
    if (controller == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.width(128.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Player initialization…")
        }
    } else {
        val playerState = rememberMediaState(player = controller).playerState
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
            Text(text = playerState.mediaMetadata.title?.toString() ?: "No track is playing")
            Text(text = playerState.mediaMetadata.albumTitle?.toString() ?: "Unknown album")
            Text(text = playerState.mediaMetadata.artist ?.toString() ?: "Unknown artist")
            Slider(
                modifier = Modifier.semantics { contentDescription = "Playback slider" },
                onValueChange = { controller.seekTo(it.toLong()) },
                value = controller.currentPosition.toFloat(),
                valueRange = 0f..max(controller.duration.toFloat(), 0f)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                IconButton(
                    onClick = { controller.seekToPreviousMediaItem() },
                    enabled = controller.hasPreviousMediaItem(),
                ) {
                    Icon(
                        Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                    )
                }
                IconButton(
                    onClick = { controller.seekBack() },
                    enabled = false
                ) {
                    Icon(
                        Icons.Default.FastRewind,
                        contentDescription = "Fast rewind",
                    )
                }
                IconButton(
                    onClick = {
                        if (playerState.isPlaying) {
                            controller.pause()
                        } else {
                            controller.play()
                        }
                    },
                ) {
                    if (playerState.isPlaying) {
                        Icon(
                            Icons.Default.Pause,
                            contentDescription = "Pause",
                        )
                    } else {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Play",
                        )
                    }
                }
                IconButton(
                    onClick = { controller.seekForward() },
                    enabled = false,
                ) {
                    Icon(
                        Icons.Default.FastForward,
                        contentDescription = "Fast-forward",
                    )
                }
                IconButton(
                    onClick = { controller.seekToNextMediaItem() },
                    enabled = controller.hasNextMediaItem(),
                ) {
                    Icon(
                        Icons.Default.SkipNext,
                        contentDescription = "Next",
                    )
                }
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