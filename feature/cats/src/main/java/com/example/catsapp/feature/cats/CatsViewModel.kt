package com.example.catsapp.feature.cats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catsapp.core.common.Resource
import com.example.catsapp.core.domain.usecase.GetCatImagesUseCase
import com.example.catsapp.core.model.Cat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(
    private val getCatImagesUseCase: GetCatImagesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CatsState>(CatsState.Loading)
    val state: StateFlow<CatsState> = _state

    init {
        getCatImages()
    }

    private fun getCatImages(limit: Int = 10) {
        _state.value = CatsState.Loading
        getCatImagesUseCase(limit).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = CatsState.Loading
                }
                is Resource.Success -> {
                    _state.value = CatsState.Success(result.data)
                }
                is Resource.Error -> {
                    _state.value = CatsState.Error(
                        result.exception.message ?: "예상치 못한 오류가 발생했습니다."
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        getCatImages()
    }
}

sealed class CatsState {
    object Loading : CatsState()
    data class Success(val cats: List<Cat>) : CatsState()
    data class Error(val message: String) : CatsState()
}