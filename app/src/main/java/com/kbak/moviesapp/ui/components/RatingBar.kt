package com.kbak.moviesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(voteAverage: Float) {
    // Convert TMDB rating (0-10) into a fraction (0.0 - 1.0)
    val ratingPercentage = (voteAverage / 10f).coerceIn(0f, 1f)

    // mapped gradient from RED → ORANGE → GREEN
    val ratingBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFD32F2F), // 🔴 BAD (0-4)
            Color(0xFFFFA000), // 🟠 MID (4-6.5)
            Color(0xFF4CAF50)  // 🟢 GOOD (6.5-10)
        ),
        startX = 0f,
        endX = 300f // this ensures smooth color blending
    )

    // Background Bar (Dark Gray for Contrast)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(8.dp)
            .background(color = Color.DarkGray, shape = RoundedCornerShape(4.dp))
    ) {
        // Filler Bar (Properly scaled by rating)
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = ratingPercentage)
                .height(8.dp)
                .background(brush = ratingBrush, shape = RoundedCornerShape(4.dp))
        )
    }
}

@Composable
@Preview
fun RatingBarPreview() {
    RatingBar(voteAverage = 3.5F)
}


