package com.github.pamugk.polyhymniamusicplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NowPlayingUiState(
    val hasNextTrack: Boolean = false,
    val hasPreviousTrack: Boolean = false,
    val playing: Boolean = false
)

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NowPlayingUiState())
    val uiState: StateFlow<NowPlayingUiState> = _uiState.asStateFlow()


}