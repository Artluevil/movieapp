package com.kbak.moviesapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    val adult: Boolean,

    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: Any?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String?,
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("origin_country")
    val originCountry: List<String>?,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String?,
    val popularity: Float,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>?,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val revenue: Int,
    val runtime: Int?,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>?,
    val status: String?,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("vote_count")
    val voteCount: Int
) {
    val fullPosterPath: String
        get() = "https://image.tmdb.org/t/p/w500${posterPath ?: ""}"

    val fullBackdropPath: String
        get() = "https://image.tmdb.org/t/p/w500${backdropPath ?: ""}"
}

data class Genre(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String?,
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
)

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val isoCode: String,
    val name: String
)

data class SpokenLanguage(
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("iso_639_1")
    val isoCode: String,
    val name: String
)
