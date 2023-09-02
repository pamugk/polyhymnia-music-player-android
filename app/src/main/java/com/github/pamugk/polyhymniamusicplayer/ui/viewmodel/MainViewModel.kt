package com.github.pamugk.polyhymniamusicplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.max
import kotlin.math.min

data class NowPlayingUiState(
    val currentTrack: Int = 0,
    val currentPosition: Float = 0f,
    val duration: Float = 125f,
    val playing: Boolean = false,
    val trackCount: Int = 10,
) {
    val fastForwardEnabled: Boolean
        get() = currentPosition < duration

    val fastRewindEnabled: Boolean
        get() = currentPosition > 0

    val hasNextTrack: Boolean
        get() = currentTrack < trackCount - 1

    val hasPreviousTrack: Boolean
        get() = currentTrack > 0
}

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NowPlayingUiState())
    val uiState: StateFlow<NowPlayingUiState> = _uiState.asStateFlow()

    fun goToNextTrack() {
        _uiState.update {
            it.copy(currentTrack = min(it.currentTrack + 1, it.trackCount - 1))
        }
    }

    fun goToPreviousTrack() {
        _uiState.update {
            it.copy(currentTrack = max(it.currentTrack - 1, 0))
        }
    }

    fun fastForward() {
        _uiState.update {
            it.copy(currentPosition = min(it.currentPosition + 5, it.duration))
        }
    }

    fun fastRewind() {
        _uiState.update {
            it.copy(currentPosition = max(it.currentPosition - 5, 0f))
        }
    }

    fun seekPosition(newPosition: Float) {
        _uiState.update {
            it.copy(currentPosition = min(newPosition, it.duration))
        }
    }

    fun switchPlaying() {
        _uiState.update {
            it.copy(playing = !it.playing)
        }
    }
}