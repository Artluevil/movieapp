package com.kbak.moviesapp.ui.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.data.remote.model.MovieDetailsResponse
import com.kbak.moviesapp.data.remote.model.MovieImagesResponse
import com.kbak.moviesapp.ui.components.AnimatedBackground
import com.kbak.moviesapp.ui.components.MovieDetailsContent
import com.kbak.moviesapp.ui.components.OfflineMovieDetailsContent
import com.kbak.moviesapp.ui.viewmodel.MovieDetailsViewModel
import com.kbak.moviesapp.ui.viewmodel.MovieImagesViewModel
import com.kbak.moviesapp.utils.ApiResult

@Composable
fun MovieDetailsScreen(movieId: Int?, movie: Movie, movieDetailsViewModel: MovieDetailsViewModel, movieImagesViewModel: MovieImagesViewModel) {

    val movieDetailsState by movieDetailsViewModel.movieDetailsState.collectAsState()
    val movieImagesState by movieImagesViewModel.movieImagesState.collectAsState()
    val genreNames by movieDetailsViewModel.genreNames.collectAsState()

    LaunchedEffect(movieId) {
        movieId?.let {
            movieDetailsViewModel.fetchMovieDetails(it, movie.genreIds)
            movieImagesViewModel.fetchMovieImages(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            movieDetailsState is ApiResult.Loading || movieImagesState is ApiResult.Loading -> {
                CircularProgressIndicator()
            }

            movieDetailsState is ApiResult.Success && movieImagesState is ApiResult.Success -> {
                val details = (movieDetailsState as ApiResult.Success<MovieDetailsResponse>).data
                val images = (movieImagesState as ApiResult.Success<MovieImagesResponse>).data
                MovieDetailsContent(genreNames, details, images)
            }

            else -> {
                Text(
                    text = "*You are in offline mode or something went wrong*",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
                OfflineMovieDetailsContent(movie, genreNames)
            }
        }
    }
}

