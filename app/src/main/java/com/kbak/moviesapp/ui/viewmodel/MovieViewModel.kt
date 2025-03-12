package com.kbak.moviesapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.data.repository.MovieRepository
import com.kbak.moviesapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private val _moviesState = MutableStateFlow<ApiResult<List<Movie>>>(ApiResult.Loading)
    val moviesState = _moviesState.asStateFlow()

    init {
        fetchPopularMovies() // ✅ Fetch data automatically when ViewModel is created
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            Log.d("MovieViewModel", "✅ Fetching movies...")
            _moviesState.value = ApiResult.Loading
            val result = repository.getPopularMovies(page = 1)

            _moviesState.value = when (result) {
                is ApiResult.Success -> ApiResult.Success(result.data.results)
                is ApiResult.Error -> ApiResult.Error(result.message)
                is ApiResult.Loading -> ApiResult.Loading
            }
        }
    }
}