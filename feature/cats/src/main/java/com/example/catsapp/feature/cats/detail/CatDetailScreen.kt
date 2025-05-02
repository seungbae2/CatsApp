package com.example.catsapp.feature.cats.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.catsapp.core.model.Cat

@Composable
internal fun CatDetailRoute(
    navigateBack: () -> Unit,
    viewModel: CatDetailViewModel = hiltViewModel(),
) {
    val catDetailUiState: CatDetailUiState by viewModel.catDetailUiState.collectAsState()
    CatDetailScreen(
        catDetailUiState = catDetailUiState,
        navigateBack = navigateBack
    )
}

@Composable
fun CatDetailScreen(
    catDetailUiState: CatDetailUiState,
    navigateBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        when (catDetailUiState) {
            is CatDetailUiState.Loading -> {
                CircularProgressIndicator()
            }

            is CatDetailUiState.Success -> {
                val cat = catDetailUiState.cat
                CatDetailContent(cat, navigateBack)
            }

            is CatDetailUiState.Error -> {
                Text("이미지를 찾을 수 없습니다: ${catDetailUiState.throwable.localizedMessage}")
            }
        }
    }
}

@Composable
fun CatDetailContent(
    cat: Cat,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 전체 이미지
        Image(
            painter = rememberAsyncImagePainter(model = cat.imageModel),
            contentDescription = "고양이 이미지",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(Color.Gray.copy(alpha = 0.6f), shape = CircleShape)
                    .size(40.dp)
                    .align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = Color.White
                )
            }

            Text(
                text = "ID: ${cat.id}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.Gray.copy(alpha = 0.6f), shape = CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatDetailContentPreview() {
    val sampleCat = Cat(
        id = "abc123",
        remoteUrl = "https://cdn2.thecatapi.com/images/MTY3ODIyMQ.jpg"
    )
    CatDetailContent(cat = sampleCat, onBackClick = {})
}