package com.github.pamugk.polyhymniamusicplayer.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.AudioAttributes
import androidx.media3.common.DeviceInfo
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.text.CueGroup

fun Player.state(): PlayerState {
    return PlayerState(this)
}

class PlayerState internal constructor(private val player: Player) {
    var availableCommands by mutableStateOf(player.availableCommands)
        private set
    var audioAttributes by mutableStateOf(player.audioAttributes)
        private set
    var cues by mutableStateOf(player.currentCues)
        private set
    var deviceInfo by mutableStateOf(player.deviceInfo)
        private set
    var deviceMuted by mutableStateOf(player.isDeviceMuted)
        private set
    var deviceVolume by mutableIntStateOf(player.deviceVolume)
        private set
    var isLoading by mutableStateOf(player.isLoading)
        private set
    var isPlaying by mutableStateOf(player.isPlaying)
        private set
    var maxSeekToPreviousPosition by mutableLongStateOf(player.maxSeekToPreviousPosition)
        private set
    var mediaMetadata by mutableStateOf(player.mediaMetadata)
        private set
    var playbackParameters by mutableStateOf(player.playbackParameters)
        private set
    var playbackState by mutableIntStateOf(player.playbackState)
        private set
    var playbackSuppressionReason by mutableIntStateOf(player.playbackSuppressionReason)
        private set
    var playerError by mutableStateOf(player.playerError)
        private set
    var playlistMetadata by mutableStateOf(player.playlistMetadata)
        private set
    var playWhenReady by mutableStateOf(player.playWhenReady)
        private set
    var repeatMode by mutableIntStateOf(player.repeatMode)
        private set
    var seekBackIncrement by mutableLongStateOf(player.seekBackIncrement)
        private set
    var seekForwardIncrement by mutableLongStateOf(player.seekForwardIncrement)
        private set
    var shuffleModeEnabled by mutableStateOf(player.shuffleModeEnabled)
        private set
    var timeline by mutableStateOf(player.currentTimeline)
        private set
    var tracks by mutableStateOf(player.currentTracks)
        private set
    var trackSelectionParameters by mutableStateOf(player.trackSelectionParameters)
        private set
    var videoSize by mutableStateOf(player.videoSize)
        private set
    var volume by mutableFloatStateOf(player.volume)
        private set

    private val listener = object : Player.Listener {
        override fun onAvailableCommandsChanged(availableCommands: Player.Commands) {
            this@PlayerState.availableCommands = availableCommands
        }

        override fun onAudioAttributesChanged(audioAttributes: AudioAttributes) {
            this@PlayerState.audioAttributes = audioAttributes
        }

        override fun onCues(cueGroup: CueGroup) {
            this@PlayerState.cues = cueGroup
        }

        override fun onDeviceInfoChanged(deviceInfo: DeviceInfo) {
            this@PlayerState.deviceInfo = deviceInfo
        }

        override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
            this@PlayerState.deviceMuted = muted
            this@PlayerState.deviceVolume = volume
        }

        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
        }

        override fun onIsLoadingChanged(isLoading: Boolean) {
            this@PlayerState.isLoading = isLoading
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            this@PlayerState.isPlaying = isPlaying
        }

        override fun onMaxSeekToPreviousPositionChanged(maxSeekToPreviousPositionMs: Long) {
            this@PlayerState.maxSeekToPreviousPosition = maxSeekToPreviousPositionMs
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            this@PlayerState.mediaMetadata = mediaMetadata
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            this@PlayerState.playbackParameters = playbackParameters
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            this@PlayerState.playbackState = playbackState
        }

        override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
            this@PlayerState.playbackSuppressionReason = playbackSuppressionReason
        }

        override fun onPlayerErrorChanged(error: PlaybackException?) {
            this@PlayerState.playerError = error
        }

        override fun onPlaylistMetadataChanged(mediaMetadata: MediaMetadata) {
            this@PlayerState.playlistMetadata = mediaMetadata
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            this@PlayerState.playWhenReady = playWhenReady
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            this@PlayerState.repeatMode = repeatMode
        }

        override fun onSeekBackIncrementChanged(seekBackIncrementMs: Long) {
            this@PlayerState.seekBackIncrement = seekBackIncrementMs
        }

        override fun onSeekForwardIncrementChanged(seekForwardIncrementMs: Long) {
            this@PlayerState.seekForwardIncrement = seekForwardIncrementMs
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            this@PlayerState.shuffleModeEnabled = shuffleModeEnabled
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            this@PlayerState.timeline = timeline
        }

        override fun onTracksChanged(tracks: Tracks) {
            this@PlayerState.tracks = tracks
        }

        override fun onTrackSelectionParametersChanged(parameters: TrackSelectionParameters) {
            this@PlayerState.trackSelectionParameters = parameters
        }

        override fun onVideoSizeChanged(videoSize: VideoSize) {
            this@PlayerState.videoSize = videoSize
        }

        override fun onVolumeChanged(volume: Float) {
            this@PlayerState.volume = volume
        }
    }

    init {
        player.addListener(listener)
    }

    fun dispose() {
        player.removeListener(listener)
    }
}