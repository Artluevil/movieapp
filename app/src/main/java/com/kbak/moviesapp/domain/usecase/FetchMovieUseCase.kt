package com.kbak.moviesapp.domain.usecase

import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.data.remote.model.toMovieEntity
import com.kbak.moviesapp.data.repository.MovieRepository
import com.kbak.moviesapp.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<ApiResult<List<Movie>>> = flow {
        emit(ApiResult.Loading)

        val cachedMovies = repository.getCachedMovies().first()
        if (cachedMovies.isNotEmpty()) {
            emit(ApiResult.Success(cachedMovies))
        }

        when (val result = repository.getPopularMovies(page = 1)) {
            is ApiResult.Success -> {
                val movieEntities = result.data.results.map { it.toMovieEntity() }
                repository.saveMoviesToDb(movieEntities)
                emit(ApiResult.Success(result.data.results))
            }
            is ApiResult.Error -> {
                emit(ApiResult.Error(result.message))
            }
            is ApiResult.Loading -> {}
        }
    }
}
