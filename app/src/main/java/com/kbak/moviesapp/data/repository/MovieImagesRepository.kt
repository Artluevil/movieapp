package com.kbak.moviesapp.data.repository

import com.kbak.moviesapp.data.remote.api.MovieImagesApiService
import com.kbak.moviesapp.data.remote.model.MovieImagesResponse
import com.kbak.moviesapp.utils.ApiResult
import retrofit2.Response
import javax.inject.Inject

class MovieImagesRepository @Inject constructor(
    private val movieImagesApiService: MovieImagesApiService
){
    suspend fun getMovieImages(movieId: Int): ApiResult<MovieImagesResponse> {
        return try {
            val response: Response<MovieImagesResponse> = movieImagesApiService.getMovieImages(movieId)
            if (response.isSuccessful) {
                response.body()?.let { movieImagesResponse ->
                    ApiResult.Success(movieImagesResponse)
                } ?: ApiResult.Error("Empty response body")
            } else {
                ApiResult.Error("API Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("Network Error: ${e.message}")
        }
    }
}
