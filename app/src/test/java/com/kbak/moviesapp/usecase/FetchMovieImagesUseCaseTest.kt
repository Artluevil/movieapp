package com.kbak.moviesapp.usecase

import app.cash.turbine.test
import com.kbak.moviesapp.data.remote.model.MovieImagesResponse
import com.kbak.moviesapp.data.repository.MovieImagesRepository
import com.kbak.moviesapp.domain.usecase.FetchMovieImagesUseCase
import com.kbak.moviesapp.utils.ApiResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class FetchMovieImagesUseCaseTest {

    private lateinit var repository: MovieImagesRepository
    private lateinit var useCase: FetchMovieImagesUseCase

    @BeforeEach
    fun setUp(){
        repository = mockk(relaxed = true)
        useCase = FetchMovieImagesUseCase(repository)

    }
    @Test
    fun `invoke emits Loading then Success when repository returns data`() = runTest {
        val movieId = 1
        val movieImageResponse = MovieImagesResponse(
            id = 1,
            backdrops = emptyList(),
            posters = emptyList()
        )

        coEvery { repository.getMovieImages(movieId) } returns ApiResult.Success(movieImageResponse)

        useCase(movieId).test {
            assertEquals(ApiResult.Loading, awaitItem())
            assertEquals(ApiResult.Success(movieImageResponse), awaitItem())
            awaitComplete()
        }
    }
    @Test
    fun `invoke emits Loading then Error when repository returns error`() = runTest {
        val movieId = 1
        val errorMessage = "Something went wrong"

        coEvery { repository.getMovieImages(movieId) } returns ApiResult.Error(errorMessage)

        useCase(movieId).test {
            assertEquals(ApiResult.Loading, awaitItem())
            assertEquals(ApiResult.Error(errorMessage), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Loading then Error when repository catches and returns error`() = runTest {
        val movieId = 1
        val errorMessage = "Network Error: timeout"

        coEvery { repository.getMovieImages(movieId) } returns ApiResult.Error(errorMessage)

        useCase(movieId).test {
            assertEquals(ApiResult.Loading, awaitItem())
            assertEquals(ApiResult.Error(errorMessage), awaitItem())
            awaitComplete()
        }
    }
}