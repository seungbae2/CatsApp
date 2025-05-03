package com.example.catsapp.feature.cats

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    navigateToCatDetail: (String) -> Unit,
    viewModel: CatsViewModel = hiltViewModel(),
) {
    val catPagingItems = viewModel.catsPagingResult.collectAsLazyPagingItems()
    val isOnline by viewModel.networkWatcher.networkStatusFlow.collectAsState()

    // 온라인 전환이 감지되면 retry() 호출
    LaunchedEffect(isOnline) {
        if (isOnline) {
            catPagingItems.retry()
        }
    }

    CatsScreen(
        catPagingItems = catPagingItems,
        onCatClick = navigateToCatDetail
    )
}

@Composable
fun CatsScreen(
    catPagingItems: LazyPagingItems<Cat>,
    onCatClick: (String) -> Unit,
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
                        index = index,
                        model = cat.imageModel,
                        onCatClick = { onCatClick(cat.id) }
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
fun CatImageItem(
    index: Int,
    model: Any,
    modifier: Modifier = Modifier,
    onCatClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(0.8f)
            .clickable(onClick = onCatClick)
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = "Index: ${index + 1}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(Color.Gray.copy(alpha = 0.6f), shape = CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}