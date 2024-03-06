package com.github.pamugk.polyhymniamusicplayer.data.controller

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.github.pamugk.polyhymniamusicplayer.data.service.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

@Stable
internal class MediaControllerManager private constructor(context: Context) : RememberObserver {
    private val appContext = context.applicationContext
    private var factory: ListenableFuture<MediaController>? = null

    private val _controller = mutableStateOf<MediaController?>(null)
    val controller: State<MediaController?>
        get() = _controller

    init { initialize() }

    internal fun initialize() {
        if (factory == null || factory?.isDone == true) {
            factory = MediaController.Builder(
                appContext,
                SessionToken(appContext, ComponentName(appContext, PlaybackService::class.java))
            ).buildAsync()
        }
        factory?.addListener(
            {
                _controller.value = factory?.let {
                    if (it.isDone && !it.isCancelled)
                        it.get()
                    else
                        null
                }
            },
            MoreExecutors.directExecutor()
        )
    }

    internal fun release() {
        factory?.let {
            MediaController.releaseFuture(it)
            _controller.value = null
        }
        factory = null
    }

    override fun onAbandoned() { release() }
    override fun onForgotten() { release() }
    override fun onRemembered() {}

    companion object {
        @Volatile
        private var instance: MediaControllerManager? = null

        fun getInstance(context: Context): MediaControllerManager {
            return instance ?: synchronized(this) {
                instance ?: MediaControllerManager(context).also { instance = it }
            }
        }
    }
}