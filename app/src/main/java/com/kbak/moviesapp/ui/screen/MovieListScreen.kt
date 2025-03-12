package com.kbak.moviesapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kbak.moviesapp.ui.viewmodel.MovieViewModel
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.utils.ApiResult

@Composable
fun MovieListScreen(viewModel: MovieViewModel = hiltViewModel()) {
    val moviesState by viewModel.moviesState.collectAsState()

    when (moviesState) {
        is ApiResult.Loading -> {
            CircularProgressIndicator(Modifier.padding(16.dp))
        }
        is ApiResult.Success -> {
            MovieList(movies = (moviesState as ApiResult.Success<List<Movie>>).data)
        }
        is ApiResult.Error -> {
            Text(
                text = "Error: ${(moviesState as ApiResult.Error).message}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun MovieList(movies: List<Movie>) {
    LazyColumn {
        items(movies) { movie ->
            Text(text = movie.title, modifier = Modifier.padding(8.dp))
        }
    }
}
