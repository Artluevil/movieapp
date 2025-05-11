package com.kbak.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbak.moviesapp.data.remote.model.MovieImagesResponse
import javax.inject.Inject
import com.kbak.moviesapp.data.repository.MovieImagesRepository
import com.kbak.moviesapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MovieImagesViewModel @Inject constructor(private val repository: MovieImagesRepository) : ViewModel() {
    private val _movieImagesState = MutableStateFlow<ApiResult<MovieImagesResponse>>(ApiResult.Loading)
    val movieImagesState = _movieImagesState.asStateFlow()

    fun fetchMovieImages(movieId: Int) {
        viewModelScope.launch {
            _movieImagesState.value = ApiResult.Loading
            when(val result = repository.getMovieImages(movieId)) {
                is ApiResult.Success ->
                    _movieImagesState.value = ApiResult.Success(result.data)
                is ApiResult.Error ->
                    _movieImagesState.value = ApiResult.Error(result.message)
                is ApiResult.Loading -> {}
            }
        }
    }
}