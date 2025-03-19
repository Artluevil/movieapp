package com.kbak.moviesapp.data.remote.api

import com.kbak.moviesapp.data.remote.model.MovieDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailsApiService {
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): retrofit2.Response<MovieDetailsResponse>
}