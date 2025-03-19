package com.kbak.moviesapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VoteAverageCircularBar(voteAverage: Float) {
    val progress = voteAverage / 10f // Convert 0-10 scale to 0-1

    val barColor = when {
        voteAverage >= 7.5 -> Color(0xFF4CAF50) // Green (Great)
        voteAverage >= 5.0 -> Color(0xFFFFC107) // Yellow (Okay)
        else -> Color(0xFFF44336) // Red (Bad)
    }

    Box(
        modifier = Modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        // Background Circle (Gray)
        CircularProgressIndicator(
            progress = 1f,
            strokeWidth = 12.dp,
            color = Color.Gray.copy(alpha = 0.3f),
            modifier = Modifier.fillMaxSize()
        )

        // Foreground Progress Circle
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 12.dp,
            color = barColor,
            modifier = Modifier.fillMaxSize()
        )

        // Display Vote Average (Larger Text)
        Text(
            text = String.format("%.1f", voteAverage),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
