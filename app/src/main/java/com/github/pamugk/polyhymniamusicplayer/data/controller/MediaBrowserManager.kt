package com.github.pamugk.polyhymniamusicplayer.data.controller

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.github.pamugk.polyhymniamusicplayer.data.service.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

@Stable
internal class MediaBrowserManager private constructor(context: Context) : RememberObserver {
    private val appContext = context.applicationContext
    private var factory: ListenableFuture<MediaBrowser>? = null

    private val _mediaBrowser = mutableStateOf<MediaBrowser?>(null)
    val mediaBrowser: State<MediaBrowser?>
        get() = _mediaBrowser

    init { initialize() }

    internal fun initialize() {
        if (factory == null || factory?.isDone == true) {
            factory = MediaBrowser.Builder(
                appContext,
                SessionToken(appContext, ComponentName(appContext, PlaybackService::class.java))
            ).buildAsync()
        }
        factory?.addListener(
            {
                _mediaBrowser.value = factory?.let {
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
            MediaBrowser.releaseFuture(it)
            _mediaBrowser.value = null
        }
        factory = null
    }

    override fun onAbandoned() { release() }
    override fun onForgotten() { release() }
    override fun onRemembered() {}

    companion object {
        @Volatile
        private var instance: MediaBrowserManager? = null

        fun getInstance(context: Context): MediaBrowserManager {
            return instance ?: synchronized(this) {
                instance ?: MediaBrowserManager(context).also { instance = it }
            }
        }
    }
}