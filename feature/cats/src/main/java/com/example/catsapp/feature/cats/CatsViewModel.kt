package com.example.catsapp.feature.cats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.catsapp.core.common.NetworkWatcher
import com.example.catsapp.core.domain.usecase.GetCatImagesUseCase
import com.example.catsapp.core.model.Cat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(
    getCatImagesUseCase: GetCatImagesUseCase,
    val networkWatcher: NetworkWatcher,
) : ViewModel() {
    val catsPagingResult: StateFlow<PagingData<Cat>> =
        getCatImagesUseCase()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
}