package com.kbak.moviesapp.data.remote.api

import com.kbak.moviesapp.data.remote.model.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface  MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): retrofit2.Response<MovieListResponse>
}