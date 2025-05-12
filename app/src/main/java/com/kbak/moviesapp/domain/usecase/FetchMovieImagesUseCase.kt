package com.kbak.moviesapp.domain.usecase

import com.kbak.moviesapp.data.remote.model.MovieImagesResponse
import com.kbak.moviesapp.data.repository.MovieImagesRepository
import com.kbak.moviesapp.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchMovieImagesUseCase @Inject constructor(
    private val repository: MovieImagesRepository
) {
    operator fun invoke(movieId: Int): Flow<ApiResult<MovieImagesResponse>> = flow {
        emit(ApiResult.Loading)
        when (val result = repository.getMovieImages(movieId)) {
            is ApiResult.Success -> emit(ApiResult.Success(result.data))
            is ApiResult.Error -> emit(ApiResult.Error(result.message))
            is ApiResult.Loading -> {}
        }
    }
}
