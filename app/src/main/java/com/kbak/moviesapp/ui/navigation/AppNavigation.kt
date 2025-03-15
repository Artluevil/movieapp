package com.kbak.moviesapp.ui.navigation

import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.ui.screen.MovieDetailsScreen
import com.kbak.moviesapp.ui.screen.MovieListScreen
import com.kbak.moviesapp.ui.viewmodel.GenreViewModel
import com.kbak.moviesapp.ui.viewmodel.MovieViewModel

@Composable
fun AppNavigation(navController: NavHostController, movieViewModel: MovieViewModel, genreViewModel: GenreViewModel) {
    NavHost(navController = navController, startDestination = "movie_list") {
        composable("movie_list") {
            MovieListScreen(navController, movieViewModel, genreViewModel)
        }
        composable(
            route = "movie_details/{movieJson}",
            arguments = listOf(navArgument("movieJson") { type = NavType.StringType }),
        ) { backStackEntry ->
            val movieJson = backStackEntry.arguments?.getString("movieJson")
            val movie = Gson().fromJson(Uri.decode(movieJson), Movie::class.java)
            MovieDetailsScreen(movie)
        }
    }
}