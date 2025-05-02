package com.example.catsapp.feature.cats.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.catsapp.core.domain.usecase.GetCatByIdUseCase
import com.example.catsapp.core.model.Cat
import com.example.catsapp.feature.cats.navigation.CatDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCatByIdUseCase: GetCatByIdUseCase,
) : ViewModel() {
    val catId = savedStateHandle.toRoute<CatDetailRoute>().catId

    private val _catDetailUiState = MutableStateFlow<CatDetailUiState>(CatDetailUiState.Loading)
    val catDetailUiState: StateFlow<CatDetailUiState> = _catDetailUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCatByIdUseCase.invoke(catId).collect {
                _catDetailUiState.value = CatDetailUiState.Success(it)
            }
        }
    }
}

sealed class CatDetailUiState {
    data object Loading : CatDetailUiState()
    data class Success(val cat: Cat) : CatDetailUiState()
    data class Error(val throwable: Throwable) : CatDetailUiState()
}