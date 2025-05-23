package com.kbak.moviesapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kbak.moviesapp.R
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.utils.formatDate

@Composable
fun OfflineMovieDetailsContent(movie: Movie, genres: String){
    val scrollState = rememberScrollState()

    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = movie.title,
        color = Color.White,
        fontSize = 24.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Text(
        text = genres.ifEmpty { "Unknown" },
        color = Color.Gray,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    AsyncImage(
        model = movie.fullPosterPath.takeIf { it.startsWith("https") }, // Load only valid URLs
        contentDescription = movie.title,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp), // Max height
        error = painterResource(id = R.drawable.placeholder_poster), // Fallback if image fails
        placeholder = painterResource(id = R.drawable.placeholder_poster) // Placeholder while loading
    )

    Spacer(modifier = Modifier.height(8.dp))

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(scrollState),
    ) {
        Text(
            text = "Release Date: ${formatDate(movie.releaseDate).ifEmpty { "Unknown" }}",
            color = Color.White,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Description: ${movie.overview?.ifEmpty { "Unknown" }}",
            color = Color.White,
            fontSize = 16.sp
        )
    }
}
