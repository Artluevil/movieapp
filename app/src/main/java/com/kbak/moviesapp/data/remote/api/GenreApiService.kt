package com.kbak.moviesapp.data.remote.api

import retrofit2.http.Query
import com.kbak.moviesapp.BuildConfig
import com.kbak.moviesapp.data.local.GenreEntity
import retrofit2.http.GET

interface GenreApiService {
    @GET("genre/movie/list")
    suspend fun getGenres(@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY): GenreResponse
}
data class GenreResponse(val genres: List<GenreEntity>)
