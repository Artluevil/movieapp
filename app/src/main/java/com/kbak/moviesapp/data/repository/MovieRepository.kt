package com.kbak.moviesapp.data.repository

import android.util.Log
import com.kbak.moviesapp.data.remote.api.MovieApiService
import com.kbak.moviesapp.data.remote.model.MovieListResponse
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor(private val apiService: MovieApiService) {
    init {
        Log.d("HiltCheck", "✅ Hilt injected MovieRepository successfully!")
    }
    suspend fun getPopularMovies(page: Int): MovieListResponse? {
        return try {
            val response: Response<MovieListResponse> = apiService.getPopularMovies(page = page)
            if (response.isSuccessful) {
                Log.d("MovieRepository", "✅ API Success: ${response.body()?.results?.size} movies")
                response.body()
            } else {
                Log.e("MovieRepository", "❌ API Error: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("MovieRepository", "❌ Network Error: ${e.message}")
            null
        }
    }
}