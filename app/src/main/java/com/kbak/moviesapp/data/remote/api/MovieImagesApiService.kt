package com.kbak.moviesapp.data.remote.api

import com.kbak.moviesapp.data.remote.model.MovieImagesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieImagesApiService {
    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int,
    ): retrofit2.Response<MovieImagesResponse>

}