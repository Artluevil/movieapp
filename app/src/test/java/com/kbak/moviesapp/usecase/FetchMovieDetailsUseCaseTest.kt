package com.kbak.moviesapp.usecase

import app.cash.turbine.test
import com.kbak.moviesapp.data.remote.model.MovieDetailsResponse
import com.kbak.moviesapp.data.repository.MovieDetailsRepository
import com.kbak.moviesapp.domain.usecase.FetchMovieDetailsUseCase
import com.kbak.moviesapp.utils.ApiResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class FetchMovieDetailsUseCaseTest {

    private lateinit var repository: MovieDetailsRepository
    private lateinit var useCase: FetchMovieDetailsUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = FetchMovieDetailsUseCase(repository)
    }

    @Test
    fun `invoke emits Loading then Success when repository returns data`() = runTest {
        val movieId = 1
        val movieDetails = MovieDetailsResponse(
            adult = false,
            backdropPath = "/backdrop.jpg",
            belongsToCollection = null,
            budget = 100000000,
            genres = emptyList(),
            homepage = "https://movie.com",
            id = movieId,
            imdbId = "tt1234567",
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalTitle = "Original Title",
            overview = "Some overview",
            popularity = 123.4f,
            posterPath = "/poster.jpg",
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            releaseDate = "2025-01-01",
            revenue = 500000000,
            runtime = 120,
            spokenLanguages = emptyList(),
            status = "Released",
            tagline = "A great movie",
            title = "Movie Title",
            video = false,
            voteAverage = 8.5f,
            voteCount = 1000
        )

        coEvery { repository.getDetailsOfAMovie(movieId) } returns ApiResult.Success(movieDetails)

        useCase(movieId).test {
            assertEquals(ApiResult.Loading, awaitItem())
            assertEquals(ApiResult.Success(movieDetails), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Loading then Error when repository returns error`() = runTest {
        val movieId = 1
        val errorMessage = "Network error"
        coEvery { repository.getDetailsOfAMovie(movieId) } returns ApiResult.Error(errorMessage)

        useCase(movieId).test {
            assertEquals(ApiResult.Loading, awaitItem())
            assertEquals(ApiResult.Error(errorMessage), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Error when repository throws exception`() = runTest {
        val movieId = 123
        val exceptionMessage = "Network error"

        coEvery { repository.getDetailsOfAMovie(movieId) } throws RuntimeException(exceptionMessage)

        useCase(movieId).test {
            assertEquals(ApiResult.Loading, awaitItem())
            val errorResult = awaitItem()
            assert(errorResult is ApiResult.Error)
            assertEquals(exceptionMessage, (errorResult as ApiResult.Error).message)
            awaitComplete()
        }
    }
}