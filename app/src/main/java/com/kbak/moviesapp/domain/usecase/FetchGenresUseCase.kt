package com.kbak.moviesapp.domain.usecase

import android.util.Log
import com.kbak.moviesapp.data.local.GenreEntity
import com.kbak.moviesapp.data.repository.GenreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchGenresUseCase @Inject constructor(
    private val repository: GenreRepository
) {
    operator fun invoke(): Flow<List<GenreEntity>> = flow {
        try {
            val apiGenres = repository.fetchGenresFromApi()
            if (apiGenres.isNotEmpty()) {
                repository.insertGenres(apiGenres)
            }
        } catch (e: Exception) {
            Log.e("FetchGenresUseCase", "Error fetching genres", e)
        }

        emitAll(repository.getGenres())
    }
}