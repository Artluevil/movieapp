package com.kbak.moviesapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun AnimatedBackground() {
    // Creates a smooth, irregular pulsating glow effect (like a TV screen flicker)
    val infiniteTransition = rememberInfiniteTransition()

    // Glow pulsates between 40% to 80% intensity to simulate a living, dynamic background
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f, // Glow starts at 40% intensity
        targetValue = 0.8f, // Pulsates up to 80% brightness
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = Random.nextInt(2000, 4000), // Random flickering between 2s and 4s
                easing = FastOutSlowInEasing // Smooth out the brightness changes
            ),
            repeatMode = RepeatMode.Reverse // Glow fades in and out repeatedly
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // Background gradient: Smooth transition from Black to Deep Blue
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black, // Darker top (feels immersive)
                        Color(0xFF0D47A1) // Deep blue bottom (cool cinematic tone)
                    )
                )
            )
    ) {
        // Light Blue Cinematic Glow positioned at the bottom
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF82B1FF).copy(alpha = glowAlpha), // Vibrant Light Blue Glow
                        Color(0xFF1976D2).copy(alpha = glowAlpha * 0.7f), // Deeper Blue Glow fading out
                        Color.Transparent // Outer edges smoothly disappear into the background
                    ),
                    center = Offset(size.width / 2, size.height * 1.2f), // Moves glow lower (half-cut effect)
                    radius = size.width * 0.9f // Expands glow for a more immersive effect
                ),
                center = Offset(size.width / 2, size.height * 1.2f), // Keeping glow lower than the screen
                radius = size.width * 0.9f
            )
        }
    }
}

@Preview
@Composable
fun PreviewAnimatedBackground() {
    AnimatedBackground()
}
