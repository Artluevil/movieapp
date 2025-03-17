package com.kbak.moviesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.utils.Converters

@Entity(tableName = "movies")
@TypeConverters(Converters::class) // Handles lists (genreIds)
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Float,
    val voteCount: Int,
    val popularity: Float,
    val originalLanguage: String,
    val adult: Boolean,
    val video: Boolean,
    val genreIds: List<Int> // Needs TypeConverter for Room
)



