package com.kbak.moviesapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbak.moviesapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    fun fetchPopularMovies() {
        viewModelScope.launch {
            Log.d("MovieViewModel", "✅ Fetching movies...")
            val movies = repository.getPopularMovies(page = 1)

            if (movies != null) {
                Log.d("MovieViewModel", "✅ Movies fetched: ${movies.results.size}")
                movies.results.forEach { movie ->
                    Log.d("MovieViewModel", "🎬 Movie: ${movie.title}, Rating: ${movie.voteAverage}")
                }
            } else {
                Log.e("MovieViewModel", "❌ Failed to fetch movies")
            }
        }
    }
}