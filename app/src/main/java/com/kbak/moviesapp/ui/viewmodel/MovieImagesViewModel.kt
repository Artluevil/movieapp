package com.kbak.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbak.moviesapp.data.remote.model.MovieImagesResponse
import javax.inject.Inject
import com.kbak.moviesapp.domain.usecase.FetchMovieImagesUseCase
import com.kbak.moviesapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MovieImagesViewModel @Inject constructor(
    private val fetchMovieImagesUseCase: FetchMovieImagesUseCase
) : ViewModel() {

    private val _movieImagesState = MutableStateFlow<ApiResult<MovieImagesResponse>>(ApiResult.Loading)
    val movieImagesState = _movieImagesState.asStateFlow()

    fun fetchMovieImages(movieId: Int) {
        viewModelScope.launch {
            fetchMovieImagesUseCase(movieId).collect { result ->
                _movieImagesState.value = result
            }
        }
    }
}
