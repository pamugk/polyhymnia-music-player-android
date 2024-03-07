package com.github.pamugk.polyhymniamusicplayer.data.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.session.MediaBrowser

@Composable
fun rememberMediaBrowser(
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle
): State<MediaBrowser?> {
    val appContext = LocalContext.current.applicationContext
    val mediaBrowserManager = remember { MediaBrowserManager.getInstance(appContext) }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mediaBrowserManager.initialize()
                Lifecycle.Event.ON_STOP -> mediaBrowserManager.release()
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    return mediaBrowserManager.mediaBrowser
}