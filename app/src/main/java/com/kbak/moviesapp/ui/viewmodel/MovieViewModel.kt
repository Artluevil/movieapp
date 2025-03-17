package com.kbak.moviesapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.data.remote.model.toMovieEntity
import com.kbak.moviesapp.data.repository.MovieRepository
import com.kbak.moviesapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private val _moviesState = MutableStateFlow<ApiResult<List<Movie>>>(ApiResult.Loading)
    val moviesState = _moviesState.asStateFlow()

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            Log.d("MovieViewModel", "🎬 Fetching movies...")
            _moviesState.value = ApiResult.Loading

            // Load cached movies from Room first
            repository.getCachedMovies()
                .onEach { cachedMovies ->
                    if (cachedMovies.isNotEmpty()) {
                        Log.d("MovieViewModel", "💾 Loaded ${cachedMovies.size} movies from Room DB!")

                        val movies = cachedMovies.map { it }

                        cachedMovies.forEach { movie ->
                            Log.d("MovieViewModel", "🎥 Cached Movie: ${movie.title} | Released: ${movie.releaseDate}")
                        }

                        _moviesState.value = ApiResult.Success(movies)
                    }
                }
                .launchIn(viewModelScope)

            // Fetch from API
            when (val result = repository.getPopularMovies(page = 1)) {
                is ApiResult.Success -> {
                    Log.d("MovieViewModel", "🌍 API Fetched ${result.data.results.size} movies!")

                    // Convert List<Movie> → List<MovieEntity> before saving
                    val movieEntities = result.data.results.map { it.toMovieEntity() }

                    repository.saveMoviesToDb(movieEntities) // Store movies in room

                    // Update UI with latest movies from API
                    _moviesState.value = ApiResult.Success(result.data.results)
                }
                is ApiResult.Error -> {
                    Log.e("MovieViewModel", "❌ Error fetching movies: ${result.message}")
                    _moviesState.value = ApiResult.Error(result.message)
                }
                is ApiResult.Loading -> {} // action to implement
            }
        }
    }
}
