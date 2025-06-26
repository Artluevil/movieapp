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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kbak.moviesapp.R
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.utils.formatDate

@Composable
fun OfflineMovieDetailsContent(movie: Movie, genres: String) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {

        Text(
            text = movie.title,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally)
        )

        Text(
            text = genres.ifEmpty { "Unknown" },
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally)
        )

        AsyncImage(
            model = movie.fullPosterPath.takeIf { it.startsWith("https") },
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            error = painterResource(id = R.drawable.placeholder_poster),
            placeholder = painterResource(id = R.drawable.placeholder_poster)
        )

        Spacer(modifier = Modifier.height(8.dp))

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

@Composable
@Preview
fun OfflineMovieDetailsContentPreview(){
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
    OfflineMovieDetailsContent(movie = mockMovie, genres = "Sci-Fi, Drama")
}
