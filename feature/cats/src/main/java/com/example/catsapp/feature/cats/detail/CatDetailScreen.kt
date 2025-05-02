package com.example.catsapp.feature.cats.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
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
        ZoomableImage(
            model = cat.imageModel,
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

@Composable
fun ZoomableImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var composableSize by remember { mutableStateOf(IntSize.Zero) }

    BoxWithConstraints(modifier = modifier) {
        composableSize = IntSize(constraints.maxWidth, constraints.maxHeight)

        Box(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        val newScale = (scale * zoom).coerceIn(1f, 3f)
                        scale = newScale

                        if (scale > 1f) {
                            val imageWidth = composableSize.width * scale
                            val imageHeight = composableSize.height * scale

                            val maxX = (imageWidth - composableSize.width).coerceAtLeast(0f) / 2f
                            val maxY = (imageHeight - composableSize.height).coerceAtLeast(0f) / 2f

                            val newOffsetX = (offset.x + pan.x).coerceIn(-maxX, maxX)
                            val newOffsetY = (offset.y + pan.y).coerceIn(-maxY, maxY)

                            offset = Offset(newOffsetX, newOffsetY)
                        } else {
                            offset = Offset.Zero
                        }
                    }
                }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = model),
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    ),
                contentScale = contentScale
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatDetailContentPreview() {
    val sampleCat = Cat(
        id = "abc123",
        remoteUrl = ""
    )
    CatDetailContent(cat = sampleCat, onBackClick = {})
}
