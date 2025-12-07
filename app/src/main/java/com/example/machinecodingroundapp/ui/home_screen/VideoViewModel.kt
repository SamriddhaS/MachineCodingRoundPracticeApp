package com.example.machinecodingroundapp.ui.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.machinecodingroundapp.domain.model.Video
import com.example.machinecodingroundapp.domain.repository.VideoRepository
import com.example.machinecodingroundapp.domain.usecase.GetAllVideosUseCase
import com.example.machinecodingroundapp.domain.usecase.GetRandomVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: VideoRepository,
    private val getAllVideosUseCase: GetAllVideosUseCase,
    private val getRandomVideosUseCase: GetRandomVideosUseCase
) : ViewModel() {

    // Single source of truth for the UI
    private val _screenState = MutableStateFlow<VideoScreenState>(VideoScreenState.Loading)
    val screenState: StateFlow<VideoScreenState> = _screenState.asStateFlow()

    // Events for one-time actions (clicks, navigation, refresh)
    private val _uiEvent = MutableSharedFlow<VideoUiEvent>()
    val uiEvent: SharedFlow<VideoUiEvent> = _uiEvent.asSharedFlow()

    init {
        loadVideos()
    }

    fun loadVideos() {
        viewModelScope.launch {
            _screenState.value = VideoScreenState.Loading

            try {
                // 1️⃣ Refresh from API (network + cache)
                repository.refreshVideos()

                // 2️⃣ Load from DB (offline-first)
                getAllVideosUseCase().collect { videos ->
                    if (videos.isEmpty()) {
                        _screenState.value = VideoScreenState.NoInternet
                    } else {
                        val carouselVideos = getRandomVideosUseCase(videos)
                        _screenState.value = VideoScreenState.Loaded(
                            videos = videos,
                            carouselVideos = carouselVideos
                        )
                    }
                }

            } catch (e: IOException) {
                // No internet / network error
                _screenState.value = VideoScreenState.NoInternet
            } catch (e: Exception) {
                // Generic error
                _screenState.value = VideoScreenState.Error(
                    message = e.localizedMessage ?: "Something went wrong"
                )
            }
        }
    }

    // Handle UI Events
    fun onEvent(event: VideoUiEvent) {
        viewModelScope.launch {
            when (event) {
                is VideoUiEvent.OnVideoClicked -> {
                    _uiEvent.emit(event) // UI can navigate to player screen
                }

                is VideoUiEvent.RefreshVideos -> {
                    loadVideos()
                }
            }
        }
    }
}

sealed class VideoScreenState {
    object Loading : VideoScreenState()
    object NoInternet : VideoScreenState()
    data class Error(val message: String) : VideoScreenState()
    data class Loaded(
        val videos: List<Video>,
        val carouselVideos: List<Video>
    ) : VideoScreenState()
}

sealed class VideoUiEvent {
    data class OnVideoClicked(val video: Video) : VideoUiEvent()
    object RefreshVideos : VideoUiEvent()
}