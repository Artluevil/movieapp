// File: MovieListCard.kt
package com.kbak.moviesapp.ui.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kbak.moviesapp.R
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun MovieListCard(
    movie: Movie,
    genreNames: String,
    onClick: () -> Unit
) {
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
                elevation = 28.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color(0xFF82B1FF).copy(alpha = glowAlpha),
                spotColor = Color(0xFFAA00FF).copy(alpha = glowAlpha * 0.9f)
            )
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF90CAF9).copy(alpha = glowAlpha * 0.9f),
                        Color(0xFF6200EA).copy(alpha = glowAlpha * 0.8f),
                        Color.Transparent
                    ),
                    center = Offset(0f, 0f),
                    radius = 400f
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
    ) {
        Card(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            elevation = CardDefaults.cardElevation(10.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = movie.fullPosterPath.takeIf { it.startsWith("https") },
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    error = painterResource(id = R.drawable.placeholder_poster),
                    placeholder = painterResource(id = R.drawable.placeholder_poster)
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
                    text = genreNames.ifEmpty { "Unknown" },
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

@Preview(showBackground = true)
@Composable
fun MovieListCardPreview() {
    val mockMovie = Movie(
        id = 1,
        title = "Inception",
        genreIds = listOf(1, 2),
        voteAverage = 8.5F,
        posterPath = "https://via.placeholder.com/300x400",
        backdropPath = "",
        originalLanguage = "en",
        originalTitle = "Inception",
        overview = "A mind-bending thriller",
        popularity = 123.4F,
        releaseDate = "2010-07-16",
        video = false,
        voteCount = 10000,
        adult = false
    )

    MoviesAppTheme {
        MovieListCard(
            movie = mockMovie,
            genreNames = "Sci-Fi, Drama",
            onClick = {}
        )
    }
}
