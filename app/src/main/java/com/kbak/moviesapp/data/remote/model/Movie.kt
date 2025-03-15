package com.kbak.moviesapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Float,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("vote_count")
    val voteCount: Int,

) {
    val fullPosterPath: String
        get() = "https://image.tmdb.org/t/p/w500$posterPath"

    val fullBackdropPath: String
        get() = "https://image.tmdb.org/t/p/w500$backdropPath"
}
