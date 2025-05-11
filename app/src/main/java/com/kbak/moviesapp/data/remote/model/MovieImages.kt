package com.kbak.moviesapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class MovieImagesResponse(
    val id: Int,
    val backdrops: List<ImageData>,
    val posters: List<ImageData>
)

data class ImageData(
    @SerializedName("aspect_ratio")
    val aspectRatio: Double,
    val height: Int,
    val width: Int,
    @SerializedName("file_path")
    val filePath: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("iso_639_1")
    val iso: String?
)
