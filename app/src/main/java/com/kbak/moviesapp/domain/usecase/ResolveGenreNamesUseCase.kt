package com.kbak.moviesapp.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ResolveGenreNamesUseCase @Inject constructor(
    private val getGenreNameByIdUseCase: GetGenreNameByIdUseCase
) {
    operator fun invoke(genreIds: List<Int>): Flow<String> {
        if (genreIds.isEmpty()) return flowOf("")

        val genreNameFlows = genreIds.map { id ->
            getGenreNameByIdUseCase(id)
        }

        return combine(genreNameFlows) { genreNames ->
            genreNames.filterNotNull().joinToString(", ")
        }
    }
}
