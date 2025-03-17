package com.kbak.moviesapp.ui.screen

import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.kbak.moviesapp.R
import com.kbak.moviesapp.ui.viewmodel.MovieViewModel
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.ui.components.AnimatedBackground
import com.kbak.moviesapp.ui.components.MovieListCard
import com.kbak.moviesapp.ui.components.RatingBar
import com.kbak.moviesapp.ui.viewmodel.GenreViewModel
import com.kbak.moviesapp.utils.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MovieListScreen(navController: NavController, viewModel: MovieViewModel = hiltViewModel(), genreViewModel: GenreViewModel) {
    val moviesState by viewModel.moviesState.collectAsState()

    when (moviesState) {
        is ApiResult.Loading -> {
            CircularProgressIndicator(Modifier.padding(16.dp))
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
        AnimatedBackground() // Custom background animation

        Column(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                items(movies) { movie ->
                    MovieListCard(movie, navController, genreViewModel)
                }
            }
        }
    }
}






