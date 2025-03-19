package com.kbak.moviesapp.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kbak.moviesapp.R
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(onAnimationEnd: () -> Unit) {
    var isLogoVisible by remember { mutableStateOf(false) }
    var isTextVisible by remember { mutableStateOf(false) }

    // 🔄 Control animation sequence
    LaunchedEffect(Unit) {
        delay(500) // Small delay before animation starts
        isLogoVisible = true
        delay(1200) // Let the logo animate first
        isTextVisible = true
        delay(1500) // Display for a bit before transitioning
        onAnimationEnd()
    }

    SplashContent(isLogoVisible, isTextVisible)
}

@Composable
fun SplashContent(isLogoVisible: Boolean, isTextVisible: Boolean) {
    val scale by animateFloatAsState(
        targetValue = if (isLogoVisible) 1f else 0.6f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "Scale Animation"
    )

    val fadeInText by animateFloatAsState(
        targetValue = if (isTextVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = LinearEasing),
        label = "Fade In Animation"
    )

    // 🌑 **Darker Gradient Background (Refined)**
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1A2E), // Dark Navy Blue
            Color(0xFF0F3460), // Deep Midnight Blue
            Color(0xFF16213E)  // Almost Black
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush), // 🌟 Darker Gradient Background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🎬 Animated App Logo
            AnimatedVisibility(visible = isLogoVisible) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Splash Logo",
                    modifier = Modifier
                        .size(160.dp)
                        .scale(scale)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🎬 Subtle Fade-in App Name (Optional)
            AnimatedVisibility(visible = isTextVisible) {
                androidx.compose.material3.Text(
                    text = "Popular Movies!",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.alpha(fadeInText)
                )
            }
        }
    }
}
