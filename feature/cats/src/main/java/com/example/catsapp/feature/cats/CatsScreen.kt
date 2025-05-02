package com.example.catsapp.feature.cats

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.catsapp.core.model.Cat

@Composable
internal fun CatsRoute(
) {
    CatsScreen()
}

@Composable
fun CatsScreen(viewModel: CatsViewModel = hiltViewModel()) {
    val catPagingItems = viewModel.catsPagingResult.collectAsLazyPagingItems()
    CatsPagingGrid(catPagingItems)
}

@Composable
fun CatsPagingGrid(
    catPagingItems: LazyPagingItems<Cat>,
) {
    // 화면 방향 감지
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val columnCount = if (isLandscape) 3 else 1

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = catPagingItems.itemCount
            ) { index ->
                catPagingItems[index]?.let { cat ->
                    CatImageItem(
                        model = cat.imageModel,      // File 또는 String
                    )
                }
            }

            item(span = { GridItemSpan(columnCount) }) {
                when (val loadState = catPagingItems.loadState.append) {
                    is LoadState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is LoadState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("오류 발생: ${loadState.error.localizedMessage}")
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = { catPagingItems.retry() }) {
                                    Text("다시 시도")
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }
        }

        // 초기 로딩 상태 처리
        when (catPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is LoadState.Error -> {
                val error = catPagingItems.loadState.refresh as LoadState.Error
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("오류 발생: ${error.error.localizedMessage}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { catPagingItems.retry() }) {
                        Text("다시 시도")
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
fun CatImageItem(model: Any, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(0.8f)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = model,
                contentScale = ContentScale.Fit,
            ),
            contentDescription = "고양이 이미지",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}