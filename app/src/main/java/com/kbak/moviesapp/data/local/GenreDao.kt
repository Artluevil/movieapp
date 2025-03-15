package com.kbak.moviesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genres: List<GenreEntity>)

    @Query("SELECT * FROM genres")
    fun getAllGenres(): Flow<List<GenreEntity>> // Returns Flow for Reactivity

    @Query("SELECT name FROM genres WHERE id = :genreId")
    suspend fun getGenreNameById(genreId: Int): String? // Correct return type
}
