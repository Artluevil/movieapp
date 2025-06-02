package com.kbak.moviesapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kbak.moviesapp.ui.viewmodel.MovieViewModel
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.ui.components.AnimatedBackground
import com.kbak.moviesapp.ui.components.MovieListCardContainer
import com.kbak.moviesapp.ui.viewmodel.GenreViewModel
import com.kbak.moviesapp.utils.ApiResult

@Composable
fun MovieListScreen(navController: NavController, viewModel: MovieViewModel = hiltViewModel(), genreViewModel: GenreViewModel) {
    val moviesState by viewModel.moviesState.collectAsState()

    when (moviesState) {
        is ApiResult.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ApiResult.Success -> {
            MovieList(navController, movies = (moviesState as ApiResult.Success<List<Movie>>).data, genreViewModel)
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
fun MovieList(navController: NavController, movies: List<Movie>, genreViewModel: GenreViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                items(movies) { movie ->
                    MovieListCardContainer(movie, navController, genreViewModel)
                }
            }
        }
    }
}






