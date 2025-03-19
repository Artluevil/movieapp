package com.kbak.moviesapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbak.moviesapp.data.remote.model.MovieDetailsResponse
import com.kbak.moviesapp.data.repository.MovieDetailsRepository
import com.kbak.moviesapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val repository: MovieDetailsRepository) : ViewModel() {
    private val _movieDetailsState = MutableStateFlow<ApiResult<MovieDetailsResponse>>(ApiResult.Loading)
    val movieDetailsState = _movieDetailsState.asStateFlow()

    fun fetchMovieDetails(movieId: Int) {
        //Log.d("MovieDetailsViewModel", "📡 Fetching movie details for ID: $movieId")
        viewModelScope.launch {
            _movieDetailsState.value = ApiResult.Loading
            when(val result = repository.getDetailsOfAMovie(movieId)) {
                is ApiResult.Success -> {
                    _movieDetailsState.value = ApiResult.Success(result.data)
                    //Log.d("MovieDetaisViewModel", "✅ Movie details fetched successfully")
                }
                is ApiResult.Error -> {
                    _movieDetailsState.value = ApiResult.Error(result.message)
                    //Log.d("MovieDetaisViewModel", " Movie details fetching error: ${result.message}")
                }
                is ApiResult.Loading -> {}
            }
        }
    }
}