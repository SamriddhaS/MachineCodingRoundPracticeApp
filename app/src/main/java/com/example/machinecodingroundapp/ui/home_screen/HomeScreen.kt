package com.example.machinecodingroundapp.ui.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.machinecodingroundapp.domain.model.Video
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun HomeScreen(
    viewModel: VideoViewModel,
    onNavigateToPlayerScreen:(String)->Unit
) {

    val uiState by viewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        /*
        * We need to handel the navigation related events in the Screen and not in view model.
        * */
        viewModel.uiEvent.collect { event ->
            when (event) {
                is VideoUiEvent.OnVideoClicked -> {
                    val clickedVideo = event.video
                    onNavigateToPlayerScreen(clickedVideo.id)
                }
                else -> {}
            }
        }
    }

    when(uiState){
        is VideoScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading…", fontSize = 22.sp)
            }
        }
        is VideoScreenState.NoInternet -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "No Internet Connection", fontSize = 22.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadVideos() }) {
                        Text(text = "Retry")
                    }
                }
            }
        }
        is VideoScreenState.Error -> {
            val state = uiState as VideoScreenState.Error
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Oops! Something went wrong", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = state.message, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadVideos() }) {
                        Text(text = "Retry")
                    }
                }
            }
        }
        is VideoScreenState.Loaded -> {
            val state = uiState as VideoScreenState.Loaded
            VideoListContent(
                loadedState = state,
                onVideoClicked = { video ->
                    viewModel.onEvent(VideoUiEvent.OnVideoClicked(video))
                }
            )
        }

    }
}

@Composable
fun VideoListContent(
    loadedState: VideoScreenState.Loaded,
    onVideoClicked: (Video) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            item {
                CarouselSection(loadedState.carouselVideos, onVideoClicked = onVideoClicked)
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(loadedState.videos, key = {it.id}) { video ->
                VideoRowItem(video, onVideoClicked = onVideoClicked)
                Spacer(modifier = Modifier.height(12.dp))
            }

        }

        if (loadedState.isOffline){
            Text(
                text = "You are offline",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun CarouselSection(
    videos: List<Video>,
    onVideoClicked: (Video) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(videos, key = {it.id}) { video ->
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(160.dp)
                    .clickable {
                        onVideoClicked(video)
                    }
            ) {

                AsyncImage(
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(video.thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = video.title,
                    modifier = Modifier
                        .fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = video.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        maxLines = 1,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VideoRowItem(
    video: Video,
    onVideoClicked: (Video) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable {
                onVideoClicked(video)
            }
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(video.thumbnailUrl)
                .crossfade(true)
                .build(),
            contentDescription = video.title,
            modifier = Modifier
                .width(150.dp)
                .fillMaxHeight()
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(text = video.title, fontSize = 16.sp)
            Text(text = video.description, maxLines = 2, fontSize = 13.sp)
            Text(text = "Duration: ${video.duration}", fontSize = 12.sp)
        }
    }
}

