package com.github.pamugk.polyhymniamusicplayer.data.service

import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession

class PlaybackService : MediaLibraryService() {
    private lateinit var mediaLibrarySession: MediaLibrarySession

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaLibrarySession = MediaLibrarySession.Builder(
            this,
            player,
            PlaybackServiceCallback(this)
        ).build()
    }

    override fun onDestroy() {
        mediaLibrarySession.player.release()
        mediaLibrarySession.release()
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession =
        mediaLibrarySession

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (!mediaLibrarySession.player.playWhenReady || mediaLibrarySession.player.mediaItemCount == 0) {
            stopSelf()
        }
    }

}
