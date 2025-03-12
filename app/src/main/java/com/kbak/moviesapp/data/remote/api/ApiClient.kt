package com.kbak.moviesapp.data.remote.api

import com.kbak.moviesapp.BuildConfig

object ApiClient {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = BuildConfig.TMDB_API_KEY
}