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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kbak.moviesapp.data.remote.model.MovieDetailsResponse
import com.kbak.moviesapp.data.remote.model.MovieImagesResponse
import com.kbak.moviesapp.utils.formatDate

@Composable
fun MovieDetailsContent(genres: String, details: MovieDetailsResponse, images: MovieImagesResponse){
    val scrollState = rememberScrollState()
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "${details.title}",
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

    ImagesSection(images)
    Spacer(modifier = Modifier.height(8.dp))

    VoteAverageCircularBar(voteAverage = details.voteAverage)

    Spacer(modifier = Modifier.height(8.dp))

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(scrollState),
    ) {
        Text(
            text = "Release Date: ${formatDate(details.releaseDate).ifEmpty { "Unknown" }}",
            color = Color.White,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Runtime: ${if(details.runtime == 0) "Unknown" else details.runtime}",
            color = Color.White,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Description: ${details.overview?.ifEmpty { "Unknown" }}",
            color = Color.White,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
