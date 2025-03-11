package com.kbak.moviesapp.data.remote.model

data class MovieListResponse(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)