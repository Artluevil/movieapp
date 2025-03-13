package com.kbak.moviesapp.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kbak.moviesapp.ui.navigation.AppNavigation
import com.kbak.moviesapp.ui.screen.MovieListScreen
import com.kbak.moviesapp.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Log.d("MainActivity", "✅ MainActivity Created!")
            //MovieListScreen() // ✅ Display the MovieListScreen here
            val movieViewModel: MovieViewModel = hiltViewModel()
            val navController = rememberNavController()
            AppNavigation(navController, movieViewModel)
        }
    }
}
