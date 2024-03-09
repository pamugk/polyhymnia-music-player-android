package com.github.pamugk.polyhymniamusicplayer.ui.data

data class Result<T>(
    val data: T? = null,
    val error: Exception? = null,
    val state: State
) {
    enum class State {
        ERROR,
        LOADING,
        SUCCESS
    }
}