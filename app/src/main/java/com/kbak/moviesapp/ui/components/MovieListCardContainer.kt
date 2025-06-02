// File: MovieListCardContainer.kt
package com.kbak.moviesapp.ui.components

import android.net.Uri
import androidx.compose.runtime.*
import com.google.gson.Gson
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.ui.viewmodel.GenreViewModel

@Composable
fun MovieListCardContainer(
    movie: Movie,
    navController: NavController,
    genreViewModel: GenreViewModel = viewModel()
) {
    val genreNames = remember { mutableStateOf("Loading...") }

    LaunchedEffect(movie.genreIds) {
        val names = movie.genreIds.mapNotNull { genreViewModel.getGenreNameById(it) }
        genreNames.value = names.joinToString(", ")
    }

    MovieListCard(
        movie = movie,
        genreNames = genreNames.value,
        onClick = {
            val movieJson = Uri.encode(Gson().toJson(movie))
            navController.navigate("movie_details/$movieJson/${movie.id}")
        }
    )
}
