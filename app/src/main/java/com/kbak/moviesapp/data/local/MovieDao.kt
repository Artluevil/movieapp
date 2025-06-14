package com.kbak.moviesapp.data.local

import android.util.Log
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies ORDER BY localId ASC")
    fun getMoviesPaging(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE id = :movieId LIMIT 1")
    fun getMovieById(movieId: Int): Flow<MovieEntity?>

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun countAllMovies(): Int

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}

