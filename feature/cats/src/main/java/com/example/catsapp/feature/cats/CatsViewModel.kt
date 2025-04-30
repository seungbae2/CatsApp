package com.example.catsapp.feature.cats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.catsapp.core.domain.usecase.GetCatImagesUseCase
import com.example.catsapp.core.model.Cat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(
    private val getCatImagesUseCase: GetCatImagesUseCase,
) : ViewModel() {
    // 페이징 데이터 스트림
    private val _catsPagingResult = MutableStateFlow<PagingData<Cat>>(PagingData.empty())
    val catsPagingResult: StateFlow<PagingData<Cat>> = _catsPagingResult

    init {
        viewModelScope.launch {
            getCatImagesUseCase.invoke()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _catsPagingResult.value = pagingData
                }
        }
    }


}