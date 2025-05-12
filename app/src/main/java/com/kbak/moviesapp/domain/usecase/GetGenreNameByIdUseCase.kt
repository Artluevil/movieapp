package com.kbak.moviesapp.domain.usecase

import com.kbak.moviesapp.data.repository.GenreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGenreNameByIdUseCase @Inject constructor(
    private val repository: GenreRepository
) {
    operator fun invoke(genreId: Int): Flow<String?> {
        return repository.getGenreById(genreId)
    }
}
