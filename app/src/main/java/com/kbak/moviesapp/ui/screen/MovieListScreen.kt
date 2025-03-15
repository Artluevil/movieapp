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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.kbak.moviesapp.ui.viewmodel.MovieViewModel
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.ui.components.AnimatedBackground
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

                    // 🔥 Remember genre names as state
                    val genreNames = remember { mutableStateOf("") }

                    // 🔄 Fetch genre names for each movie
                    LaunchedEffect(movie.genreIds) {
                        val names = movie.genreIds.mapNotNull { genreViewModel.getGenreNameById(it) }
                        genreNames.value = names.joinToString(", ") // Join names with comma
                    }
                    //Glow Animation
                    val infiniteTransition = rememberInfiniteTransition()
                    val glowAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.5f,
                        targetValue = 1.0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )


                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .shadow(
                                elevation = 28.dp, // glow effect
                                shape = RoundedCornerShape(14.dp),
                                ambientColor = Color(0xFF82B1FF).copy(alpha = glowAlpha), // Blue Glow
                                spotColor = Color(0xFFAA00FF).copy(alpha = glowAlpha * 0.9f) // Deep Purple Glow
                            )
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF90CAF9).copy(alpha = glowAlpha * 0.9f), // Soft Blue Center
                                        Color(0xFF6200EA).copy(alpha = glowAlpha * 0.8f), // Deep Purple Outer Glow
                                        Color.Transparent
                                    ),
                                    center = Offset(0f, 0f),
                                    radius = 400f // Spread glow further
                                ),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                val movieJson = Uri.encode(Gson().toJson(movie))
                                navController.navigate("movie_details/$movieJson")
                            }
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.Black),
                            elevation = CardDefaults.cardElevation(10.dp), // Higher elevation for depth
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = movie.fullPosterPath,
                                    contentDescription = movie.title,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = movie.title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = genreNames.value,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .padding(top = 4.dp),
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                RatingBar(voteAverage = movie.voteAverage)
                            }
                        }
                    }
                }
            }
        }
    }
}






