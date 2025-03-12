package com.kbak.moviesapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun AnimatedBackground() {
    val screenHeight = 1920f // Approximate screen height (adjust for different screens)
    val particles = remember { List(100) { createParticle(screenHeight) } } // ✅ More particles
    val infiniteTransition = rememberInfiniteTransition()

    val animatedOffsets = particles.map { particle ->
        infiniteTransition.animateFloat(
            initialValue = particle.startY, // ✅ Starts at bottom
            targetValue = 0f, // ✅ Moves to top
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = particle.duration,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    val animatedSizes = particles.map { particle ->
        infiniteTransition.animateFloat(
            initialValue = particle.radius,
            targetValue = 1f, // ✅ Shrinks gradually
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = particle.duration,
                    easing = FastOutLinearInEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    val animatedAlphas = particles.map { particle ->
        infiniteTransition.animateFloat(
            initialValue = 1f, // ✅ Fully visible at start
            targetValue = 0f, // ✅ Vanishes only when reaching the top
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = particle.duration,
                    easing = FastOutLinearInEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background( // ✅ Smooth Black-to-Dark Blue Gradient Background
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black, Color(0xFF001F3F)) // Dark Blue
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            animatedOffsets.forEachIndexed { index, animatedOffset ->
                drawCircle(
                    color = particles[index].color.copy(alpha = animatedAlphas[index].value), // ✅ Vanishes at the top
                    radius = animatedSizes[index].value, // ✅ Shrinks as it moves up
                    center = Offset(particles[index].x, animatedOffset.value)
                )
            }
        }
    }
}

// ✅ Particle Data Model
data class Particle(
    val x: Float,
    val startY: Float,
    val radius: Float,
    val color: Color,
    val duration: Int
)

// ✅ Creates particles that start from the bottom and move up
fun createParticle(screenHeight: Float): Particle {
    return Particle(
        x = Random.nextFloat() * 1080f,
        startY = screenHeight, // ✅ Starts at the bottom
        radius = Random.nextFloat() * 12f + 6f, // ✅ Random sizes for variety
        color = listOf(
            Color(0xFF1565C0), // Deep Blue
            Color(0xFF0D47A1), // Darker Blue
            Color(0xFFBB86FC)  // Light Blue Glow
        ).random(),
        duration = Random.nextInt(5000, 9000) // ✅ Slow animation for smooth effect
    )
}

@Preview
@Composable
fun PreviewAnimatedParticlesBackground() {
    AnimatedBackground()
}
