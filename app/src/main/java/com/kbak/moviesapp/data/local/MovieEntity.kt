package com.kbak.moviesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kbak.moviesapp.utils.Converters

@Entity(tableName = "movies")
@TypeConverters(Converters::class)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val id: Int,
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
    val genreIds: List<Int>
)



