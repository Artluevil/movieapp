package com.kbak.moviesapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.ui.components.AnimatedBackground

@Composable
fun MovieDetailsScreen(movie: Movie) {
    AnimatedBackground()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Movie Details Screen",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Sample Movie ID: ${movie.id}",
                color = Color.Black,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Placeholder image (Replace with real image later)
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_report_image), // Default Android placeholder
                contentDescription = "Placeholder Image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "This is a test screen to verify navigation is working.",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}