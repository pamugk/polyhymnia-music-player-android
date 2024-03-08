package com.github.pamugk.polyhymniamusicplayer.data.service

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.lifecycleScope
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.github.pamugk.polyhymniamusicplayer.data.datasource.MusicLibraryDatasource

class PlaybackService : MediaLibraryService(), LifecycleOwner {

    private val lifecycleDispatcher = ServiceLifecycleDispatcher(this)
    private lateinit var mediaLibrarySession: MediaLibrarySession

    override fun onCreate() {
        lifecycleDispatcher.onServicePreSuperOnCreate()
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaLibrarySession = MediaLibrarySession.Builder(
            this,
            player,
            PlaybackServiceCallback(
                coroutineScope = lifecycleScope,
                datasource = MusicLibraryDatasource(this.contentResolver)
            )
        ).build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        lifecycleDispatcher.onServicePreSuperOnBind()
        return super.onBind(intent)
    }

    @Deprecated("Deprecated in Java")
    override fun onStart(intent: Intent?, startId: Int) {
        lifecycleDispatcher.onServicePreSuperOnStart()
        super.onStart(intent, startId)
    }

    override fun onDestroy() {
        mediaLibrarySession.player.release()
        mediaLibrarySession.release()
        lifecycleDispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession =
        mediaLibrarySession

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (!mediaLibrarySession.player.playWhenReady || mediaLibrarySession.player.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override val lifecycle: Lifecycle
        get() = lifecycleDispatcher.lifecycle
}
