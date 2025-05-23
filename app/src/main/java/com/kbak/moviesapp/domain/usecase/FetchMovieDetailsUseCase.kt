package com.kbak.moviesapp.domain.usecase

import com.kbak.moviesapp.data.remote.model.MovieDetailsResponse
import com.kbak.moviesapp.data.repository.MovieDetailsRepository
import com.kbak.moviesapp.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchMovieDetailsUseCase @Inject constructor(
    private val repository: MovieDetailsRepository
) {
    operator fun invoke(movieId: Int): Flow<ApiResult<MovieDetailsResponse>> = flow {
        emit(ApiResult.Loading)
        when (val result = repository.getDetailsOfAMovie(movieId)) {
            is ApiResult.Success -> emit(ApiResult.Success(result.data))
            is ApiResult.Error -> emit(ApiResult.Error(result.message))
            is ApiResult.Loading -> {}
        }
    }
}
