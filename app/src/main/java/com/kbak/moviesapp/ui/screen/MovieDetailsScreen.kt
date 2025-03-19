package com.kbak.moviesapp.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.data.remote.model.MovieDetailsResponse
import com.kbak.moviesapp.ui.components.AnimatedBackground
import com.kbak.moviesapp.ui.components.MovieDetailsContent
import com.kbak.moviesapp.ui.components.OfflineMovieDetailsContent
import com.kbak.moviesapp.ui.viewmodel.GenreViewModel
import com.kbak.moviesapp.ui.viewmodel.MovieDetailsViewModel
import com.kbak.moviesapp.utils.ApiResult

@Composable
fun MovieDetailsScreen(movieId: Int?, movie: Movie, genreViewModel: GenreViewModel, movieDetailsViewModel: MovieDetailsViewModel) {
    AnimatedBackground()
    val genreNames = remember { mutableStateOf("") }

    val movieDetailsState by movieDetailsViewModel.movieDetailsState.collectAsState()

    // Fetch genre names for each movie
    LaunchedEffect(movie.genreIds) {
        Log.d("MovieDetailsScreen", "🚀 LaunchedEffect triggered for movie: ${movie.title}")
        val names = movie.genreIds.mapNotNull { genreViewModel.getGenreNameById(it) }
        genreNames.value = names.joinToString(", ") // Join names with comma
        if (movieId != null) movieDetailsViewModel.fetchMovieDetails(movieId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (movieDetailsState) {
            is ApiResult.Loading -> {
                CircularProgressIndicator()
            }
            is ApiResult.Success -> {
                val details = (movieDetailsState as ApiResult.Success<MovieDetailsResponse>).data
                MovieDetailsContent(movie, genreNames.value, details)
            }
            is ApiResult.Error -> {
                Text(
                    text = "*You are in offline mode*",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
                OfflineMovieDetailsContent(movie, genreNames.value)
            }
        }
    }
}

