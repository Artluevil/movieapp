package com.kbak.moviesapp.usecase

import app.cash.turbine.test
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.data.remote.model.MovieListResponse
import com.kbak.moviesapp.data.repository.MovieRepository
import com.kbak.moviesapp.domain.usecase.FetchMoviesUseCase
import com.kbak.moviesapp.utils.ApiResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@ExperimentalCoroutinesApi
class FetchMoviesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var useCase: FetchMoviesUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = FetchMoviesUseCase(repository)
    }

    @Test
    fun `invoke emits Loading and returns cached movies before fetching from API`() = runTest {
        val cachedMovies = listOf(
            createMovie(id = 1, title = "Movie 1"),
            createMovie(id = 2, title = "Movie 2")
        )
        val apiMovies = listOf(
            createMovie(id = 3, title = "Movie 3")
        )
        val response = MovieListResponse(results = apiMovies, page = 1, totalPages = 1, totalResults = 1)

        coEvery { repository.getCachedMovies() } returns flowOf(cachedMovies)
        coEvery { repository.getPopularMovies(1) } returns ApiResult.Success(response)

        useCase().test {
            assertEquals(ApiResult.Loading, awaitItem())
            assertEquals(ApiResult.Success(cachedMovies), awaitItem())
            assertEquals(ApiResult.Success(apiMovies), awaitItem())
            awaitComplete()
        }

        coVerify { repository.saveMoviesToDb(any()) }
    }

    @Test
    fun `invoke emits Loading and only API movies when cache is empty`() = runTest {
        val apiMovies = listOf(
            createMovie(id = 5, title = "API Movie")
        )
        val response = MovieListResponse(results = apiMovies, page = 1, totalPages = 1, totalResults = 1)

        coEvery { repository.getCachedMovies() } returns flowOf(emptyList())
        coEvery { repository.getPopularMovies(1) } returns ApiResult.Success(response)

        useCase().test {
            assertEquals(ApiResult.Loading, awaitItem())
            assertEquals(ApiResult.Success(apiMovies), awaitItem())
            awaitComplete()
        }

        coVerify { repository.saveMoviesToDb(any()) }
    }

    @Test
    fun `invoke emits Loading then Error when API fails`() = runTest {
        val cachedMovies = emptyList<Movie>()
        val errorMessage = "Network error"

        coEvery { repository.getCachedMovies() } returns flowOf(cachedMovies)
        coEvery { repository.getPopularMovies(1) } returns ApiResult.Error(errorMessage)

        useCase().test {
            assertEquals(ApiResult.Loading, awaitItem())
            assertEquals(ApiResult.Error(errorMessage), awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 0) { repository.saveMoviesToDb(any()) }
    }

    private fun createMovie(
        id: Int,
        title: String,
        overview: String = "Test overview",
        posterPath: String? = "/poster.jpg"
    ): Movie {
        return Movie(
            localId = 0,
            adult = false,
            backdropPath = "/backdrop.jpg",
            genreIds = listOf(1, 2),
            id = id,
            originalLanguage = "en",
            originalTitle = title,
            overview = overview,
            popularity = 100.0f,
            posterPath = posterPath,
            releaseDate = "2025-01-01",
            title = title,
            video = false,
            voteAverage = 7.5f,
            voteCount = 100
        )
    }
}
