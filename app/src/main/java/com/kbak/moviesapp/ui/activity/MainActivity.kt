package com.kbak.moviesapp.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kbak.moviesapp.ui.navigation.AppNavigation
import com.kbak.moviesapp.ui.viewmodel.GenreViewModel
import com.kbak.moviesapp.ui.viewmodel.MovieDetailsViewModel
import com.kbak.moviesapp.ui.viewmodel.MovieImagesViewModel
import com.kbak.moviesapp.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val movieViewModel: MovieViewModel = hiltViewModel()
            val navController = rememberNavController()
            val genreViewModel: GenreViewModel = hiltViewModel()
            val movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
            val movieImagesViewModel: MovieImagesViewModel = hiltViewModel()
            enableEdgeToEdge()
            AppNavigation(navController, movieViewModel, genreViewModel, movieDetailsViewModel, movieImagesViewModel)
        }
    }
}
