package com.example.machinecodingroundapp.ui.video_player_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.machinecodingroundapp.domain.model.Video

@Composable
fun VideoPlayerScreen(
    viewModel: VideoPlayerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when (val ui = state) {
        is VideoPlayerUiState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier
                    .size(78.dp)
                    .align(Alignment.Center)
                )
            }
        }

        is VideoPlayerUiState.Error -> {
            Box(Modifier.fillMaxSize()) {
                Text("Error: ${ui.message}", modifier = Modifier.fillMaxSize())
            }
        }

        is VideoPlayerUiState.Loaded -> {

            Box(Modifier.fillMaxSize()) {

                VideoPlayerView(
                    viewModel = viewModel,
                    video = ui.video
                )

                if (ui.isBuffering) {
                    CircularProgressIndicator(Modifier
                        .size(78.dp)
                        .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun VideoPlayerView(
    viewModel: VideoPlayerViewModel,
    video:Video
) {
    val context = LocalContext.current

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(video.videoUrl))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                viewModel.onEvent(VideoPlayerAction.PlayerStateChanged(state))
            }
        })
        onDispose {
            player.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                this.player = player
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
