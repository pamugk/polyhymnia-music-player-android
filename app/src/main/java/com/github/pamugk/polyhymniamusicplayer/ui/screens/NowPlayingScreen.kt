package com.github.pamugk.polyhymniamusicplayer.ui.screens

import android.graphics.BitmapFactory
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
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import com.github.pamugk.polyhymniamusicplayer.R
import com.github.pamugk.polyhymniamusicplayer.ui.state.rememberMediaState
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun NowPlayingScreen(
    player: Player,
    padding: PaddingValues = PaddingValues()
) {
    val playerState = rememberMediaState(player = player).playerState
    val albumCover = playerState.mediaMetadata.artworkData?.let { albumData ->
        BitmapFactory.decodeByteArray(albumData, 0, albumData.size)
    }
    val currentPosition by produceState(initialValue = player.currentPosition) {
        while (isActive) {
            delay(500.milliseconds)
            value = player.currentPosition
        }
    }
    val trackAvailable = playerState.timeline != Timeline.EMPTY

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally) {
        if (albumCover == null) {
            Image(
                imageVector = Icons.Default.BrokenImage,
                contentDescription = stringResource(R.string.album_cover),
                modifier = Modifier.fillMaxSize(0.8f)
            )
        } else {
            Image(
                bitmap = albumCover.asImageBitmap(),
                contentDescription = stringResource(R.string.album_cover),
                modifier = Modifier.fillMaxSize(0.8f)
            )
        }
        Text(text = playerState.mediaMetadata.title?.toString() ?: stringResource(R.string.no_track_playing))
        Text(text = playerState.mediaMetadata.albumTitle?.toString() ?: stringResource(R.string.no_album))
        Text(text = playerState.mediaMetadata.artist ?.toString() ?: stringResource(R.string.no_artist))
        Slider(
            onValueChange = { player.seekTo(it.toLong()) },
            value = currentPosition.toFloat(),
            valueRange = 0f..max(player.duration.toFloat(), 0f),
            enabled = trackAvailable
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            IconButton(
                onClick = { player.seekToPreviousMediaItem() },
                enabled = player.hasPreviousMediaItem(),
            ) {
                Icon(
                    Icons.Default.SkipPrevious,
                    contentDescription = stringResource(R.string.previous),
                )
            }
            IconButton(
                onClick = { player.seekBack() },
                enabled = trackAvailable
            ) {
                Icon(
                    Icons.Default.FastRewind,
                    contentDescription = stringResource(R.string.fast_rewind),
                )
            }
            IconButton(
                onClick = {
                    if (playerState.isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                },
                enabled = trackAvailable
            ) {
                if (playerState.isPlaying) {
                    Icon(
                        Icons.Default.Pause,
                        contentDescription = stringResource(R.string.pause),
                    )
                } else {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = stringResource(R.string.play),
                    )
                }
            }
            IconButton(
                onClick = { player.seekForward() },
                enabled = trackAvailable,
            ) {
                Icon(
                    Icons.Default.FastForward,
                    contentDescription = stringResource(R.string.fast_forward),
                )
            }
            IconButton(
                onClick = { player.seekToNextMediaItem() },
                enabled = player.hasNextMediaItem(),
            ) {
                Icon(
                    Icons.Default.SkipNext,
                    contentDescription = stringResource(R.string.next),
                )
            }
        }
    }
}