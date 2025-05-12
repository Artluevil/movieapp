package com.kbak.moviesapp.domain.usecase

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
        val genres = repository.fetchGenresFromApi()
        repository.insertGenres(genres.map { GenreEntity(it.id, it.name) })
        emitAll(repository.getGenres())
    }
}