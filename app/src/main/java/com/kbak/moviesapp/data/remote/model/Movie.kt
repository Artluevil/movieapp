package com.kbak.moviesapp.data.remote.model

import com.google.gson.annotations.SerializedName
import com.kbak.moviesapp.data.local.MovieEntity

data class Movie(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?, // Allow nullable
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
    val posterPath: String?, // Allow nullable
    @SerializedName("release_date")
    val releaseDate: String?,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("vote_count")
    val voteCount: Int,
) {
    val fullPosterPath: String
        get() = if(!posterPath.isNullOrEmpty()) {
            "https://image.tmdb.org/t/p/w500$posterPath"
        } else {
            ""
        }
    val fullBackdropPath: String
        get() = "https://image.tmdb.org/t/p/w500${backdropPath ?: ""}" // ✅ Safe fallback

}

// Converts Room Entity (MovieEntity) → API Model (Movie)
fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        originalTitle = this.originalTitle,
        overview = this.overview,
        posterPath = this.posterPath, // ✅ Already nullable
        backdropPath = this.backdropPath, // ✅ Already nullable
        releaseDate = this.releaseDate, // ✅ Already nullable
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        popularity = this.popularity,
        originalLanguage = this.originalLanguage,
        adult = this.adult,
        video = this.video,
        genreIds = this.genreIds
    )
}

// Converts API Model (Movie) → Room Entity (MovieEntity)
fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        originalTitle = this.originalTitle,
        overview = this.overview,
        posterPath = this.posterPath ?: "", // ✅ Safe fallback
        backdropPath = this.backdropPath ?: "", // ✅ Safe fallback
        releaseDate = this.releaseDate ?: "", // ✅ Safe fallback
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        popularity = this.popularity,
        originalLanguage = this.originalLanguage,
        adult = this.adult,
        video = this.video,
        genreIds = this.genreIds
    )
}
