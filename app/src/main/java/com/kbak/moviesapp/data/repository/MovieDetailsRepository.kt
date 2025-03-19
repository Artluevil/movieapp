package com.kbak.moviesapp.data.repository

import android.util.Log
import com.kbak.moviesapp.data.remote.api.MovieDetailsApiService
import com.kbak.moviesapp.data.remote.model.MovieDetailsResponse
import com.kbak.moviesapp.utils.ApiResult
import retrofit2.Response
import javax.inject.Inject

class MovieDetailsRepository @Inject constructor(
    private val movieDetailsApiService: MovieDetailsApiService
) {
    init {
        //Log.d("HiltCheck", "✅ Hilt injected MovieDetailsRepository successfully!")
    }

    suspend fun getDetailsOfAMovie(movieId: Int): ApiResult<MovieDetailsResponse> {
        return try {
            val response: Response<MovieDetailsResponse> = movieDetailsApiService.getMovieDetails(movieId)
            if (response.isSuccessful) {
                response.body()?.let { movieDetailsResponse ->
                    ApiResult.Success(movieDetailsResponse)
                } ?: ApiResult.Error("Empty response body")
            } else {
                ApiResult.Error("API Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("Network Error: ${e.message}")

        }
    }
}