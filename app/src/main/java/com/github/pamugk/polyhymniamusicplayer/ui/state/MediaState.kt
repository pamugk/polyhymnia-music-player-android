package com.github.pamugk.polyhymniamusicplayer.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.Player

@Composable
fun rememberMediaState(player: Player): MediaState =
    remember { MediaState(player) }.apply { this.player = player }

@Stable
class MediaState(initialPlayer: Player) {
    var player: Player
        set(current) {
            if (current !== _player) {
                playerState.dispose()
                playerState = current.state()
                _player = current
            }
        }
        get() = _player

    var playerState by mutableStateOf(initialPlayer.state())
        private set

    private var _player: Player = initialPlayer
}