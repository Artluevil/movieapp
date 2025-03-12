package com.kbak.moviesapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kbak.moviesapp.ui.viewmodel.MovieViewModel
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.ui.components.AnimatedBackground
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
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground()
        Column(modifier = Modifier.fillMaxSize()){
            Text(
                text = "Movie List",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            items(movies) { movie ->
                Box(modifier = Modifier.padding(6.dp).shadow(12.dp, shape = RoundedCornerShape(12.dp)).background(Color(0x552196F3), shape = RoundedCornerShape(10.dp))) {
                    Card (modifier = Modifier.padding(2.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.Black), elevation = CardDefaults.cardElevation(4.dp), shape = RoundedCornerShape(12.dp)) {
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            AsyncImage(
                                model = movie.fullPosterPath,
                                contentDescription = movie.title,
                                modifier = Modifier
                                    .fillMaxWidth())
                            Text(
                                text = movie.title,
                                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally).padding(16.dp),
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

