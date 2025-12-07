package com.example.machinecodingroundapp.ui.video_player_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.example.machinecodingroundapp.domain.model.Video
import com.example.machinecodingroundapp.domain.usecase.GetVideoByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val getVideoByIdUseCase: GetVideoByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val videoId: String = checkNotNull(savedStateHandle["videoId"])

    private val _uiState = MutableStateFlow<VideoPlayerUiState>(VideoPlayerUiState.Loading)
    val uiState: StateFlow<VideoPlayerUiState> = _uiState

    init {
        loadVideo()
    }

    private fun loadVideo() {
        viewModelScope.launch {
            val video = getVideoByIdUseCase(videoId)
            if (video != null) {
                _uiState.value = VideoPlayerUiState.Loaded(
                    video = video,
                    isBuffering = true
                )
            } else {
                _uiState.value = VideoPlayerUiState.Error("Video not found")
            }
        }
    }

    fun onEvent(action: VideoPlayerAction) {
        when (action) {
            is VideoPlayerAction.PlayerStateChanged -> {
                val isBuffering = action.state == Player.STATE_BUFFERING
                val current = _uiState.value
                if (current is VideoPlayerUiState.Loaded) {
                    _uiState.value = current.copy(isBuffering = isBuffering)
                }
            }
        }
    }
}

sealed class VideoPlayerUiState {
    object Loading : VideoPlayerUiState()
    data class Loaded(
        val video: Video,
        val isBuffering: Boolean = false
    ) : VideoPlayerUiState()
    data class Error(val message: String) : VideoPlayerUiState()
}

sealed class VideoPlayerAction {
    data class PlayerStateChanged(val state: Int) : VideoPlayerAction()
}


