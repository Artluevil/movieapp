package com.kbak.moviesapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.kbak.moviesapp.R
import com.kbak.moviesapp.data.remote.model.MovieImagesResponse

@Composable
fun ImagesSection(images: MovieImagesResponse?) {
    val imageBaseUrl = "https://image.tmdb.org/t/p/w500"
    val selectedImage = remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        images?.backdrops?.takeIf { it.isNotEmpty() }?.let { backdrops ->
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                items(backdrops) { image ->
                    AsyncImage(
                        model = "$imageBaseUrl${image.filePath}",
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .fillParentMaxWidth(0.8f)
                            .clickable {
                                selectedImage.value = "$imageBaseUrl${image.filePath}"
                            },
                        placeholder = painterResource(id = R.drawable.placeholder_poster),
                        error = painterResource(id = R.drawable.placeholder_poster)
                    )
                }
            }
        } ?: Text(
            text = "No images available.",
            color = Color.Gray,
            fontSize = 14.sp
        )

        selectedImage.value?.let { imageUrl ->
            FullscreenImageDialog(imageUrl) {
                selectedImage.value = null
            }
        }
    }
}

@Composable
fun FullscreenImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            AsyncImage(
                model = imageUrl,
                contentDescription = "Full Screen Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.Center),
                placeholder = painterResource(id = R.drawable.placeholder_poster),
                error = painterResource(id = R.drawable.placeholder_poster)
            )
        }
    }
}
