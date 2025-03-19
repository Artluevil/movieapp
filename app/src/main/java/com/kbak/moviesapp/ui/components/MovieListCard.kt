package com.kbak.moviesapp.ui.components

import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.ui.viewmodel.GenreViewModel

@Composable
fun MovieListCard(movie: Movie, navController: NavController, genreViewModel: GenreViewModel) {
    // Remember genre names as state
    val genreNames = remember { mutableStateOf("") }

    // Fetch genre names for each movie
    LaunchedEffect(movie.genreIds) {
        val names = movie.genreIds.mapNotNull { genreViewModel.getGenreNameById(it) }
        genreNames.value = names.joinToString(", ") // Join names with comma
    }

    // Glow Animation
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
                elevation = 28.dp, // Glow effect
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
                navController.navigate("movie_details/$movieJson/${movie.id}")
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
                    model = movie.fullPosterPath.takeIf { it.startsWith("https") }, // Load only valid URLs
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp), // Max height
                    error = painterResource(id = R.drawable.placeholder_poster), // Fallback if image fails
                    placeholder = painterResource(id = R.drawable.placeholder_poster) // Placeholder while loading
                )

                Text(
                    text = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = if (genreNames.value.isNotEmpty()) genreNames.value else "Unknown",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(vertical = 4.dp, horizontal = 8.dp),
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
