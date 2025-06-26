package com.kbak.moviesapp.data.repository

import android.util.Log
import com.kbak.moviesapp.data.local.GenreDao
import com.kbak.moviesapp.data.local.GenreEntity
import com.kbak.moviesapp.data.remote.api.GenreApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GenreRepository @Inject constructor(
    private val genreDao: GenreDao,
    private val genreApiService: GenreApiService // injecting GenreApiService
) {

    suspend fun insertGenres(genres: List<GenreEntity>) {
        genreDao.insertAll(genres)
    }

    fun getGenres(): Flow<List<GenreEntity>> {
        return genreDao.getAllGenres()
    }

    fun getGenreById(id: Int): Flow<String?> {
        return flow {
            val genreName = genreDao.getGenreNameById(id)
            emit(genreName)
        }
    }

    suspend fun fetchGenresFromApi(): List<GenreEntity> {
        return try {
            val response = genreApiService.getGenres()
            val genres = response.genres.map { GenreEntity(it.id, it.name) }
            genres
        } catch (e: Exception) {
            emptyList()
        }
    }
}
