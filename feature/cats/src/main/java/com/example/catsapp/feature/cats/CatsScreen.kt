package com.example.catsapp.feature.cats

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
internal fun CatsRoute(
) {
    CatsScreen()
}

@Composable
fun CatsScreen(viewModel: CatsViewModel = hiltViewModel()) {
    // 상태 수집
    val state by viewModel.state.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is CatsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is CatsState.Error -> {
                val errorState = state as CatsState.Error
                Text(
                    text = "오류 발생: ${errorState.message}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is CatsState.Success -> {
                val successState = state as CatsState.Success
                if (successState.cats.isEmpty()) {
                    Text(
                        text = "표시할 이미지가 없습니다",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(successState.cats) { cat ->
                            CatImageItem(cat.url)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CatImageItem(imageUrl: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(0.8f)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build()
            ),
            contentDescription = "고양이 이미지",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}