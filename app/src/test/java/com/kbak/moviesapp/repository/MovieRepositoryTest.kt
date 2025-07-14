package com.kbak.moviesapp.repository

import app.cash.turbine.test
import com.kbak.moviesapp.data.local.MovieDao
import com.kbak.moviesapp.data.local.MovieEntity
import com.kbak.moviesapp.data.remote.api.MovieApiService
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.data.remote.model.MovieListResponse
import com.kbak.moviesapp.data.repository.MovieRepository
import com.kbak.moviesapp.utils.ApiResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class MovieRepositoryTest {

    private lateinit var apiService: MovieApiService
    private lateinit var movieDao: MovieDao
    private lateinit var repository: MovieRepository

    @BeforeEach
    fun setup() {
        apiService = mockk()
        movieDao = mockk(relaxed = true)
        repository = MovieRepository(apiService, movieDao)
    }

    @Test
    fun `getPopularMovies returns Success and saves to DB when API call succeeds`() = runTest {
        val movies = listOf(
            Movie(
                adult = false,
                backdropPath = "/backdrop.jpg",
                id = 1,
                originalLanguage = "en",
                originalTitle = "Original Title",
                overview = "Some overview",
                popularity = 123.4f,
                posterPath = "/poster.jpg",
                releaseDate = "2025-01-01",
                title = "Movie Title",
                video = false,
                voteAverage = 8.5f,
                voteCount = 1000,
                localId = 1,
                genreIds = listOf(1, 2))
        )
        val response = MovieListResponse(
            results = movies,
            page = 1,
            totalPages = 1,
            totalResults = 1
        )

        coEvery { apiService.getPopularMovies(page = 1) } returns Response.success(response)

        val result = repository.getPopularMovies(1)

        assertTrue(result is ApiResult.Success)
        assertEquals(response, (result as ApiResult.Success).data)

        coVerify { movieDao.deleteAllMovies() }
        coVerify { movieDao.insertMovies(any()) }
    }

    @Test
    fun `getPopularMovies returns Error when API body is null`() = runTest {
        coEvery { apiService.getPopularMovies(page = 1) } returns Response.success(null)

        val result = repository.getPopularMovies(1)

        assertTrue(result is ApiResult.Error)
        assertEquals("Empty response body", (result as ApiResult.Error).message)
    }

    @Test
    fun `getPopularMovies returns Error on API failure`() = runTest {
        val errorBody = ResponseBody.create("application/json".toMediaType(), "Not Found")
        coEvery { apiService.getPopularMovies(page = 1) } returns Response.error(404, errorBody)

        val result = repository.getPopularMovies(1)

        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("API Error"))
    }

    @Test
    fun `getPopularMovies returns Error on network exception`() = runTest {
        coEvery { apiService.getPopularMovies(page = 1) } throws RuntimeException("Timeout")

        val result = repository.getPopularMovies(1)

        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Network Error"))
    }

    @Test
    fun `getCachedMovies emits mapped list from Room`() = runTest {
        val entities = listOf(
            MovieEntity(
                adult = false,
                backdropPath = "/backdrop.jpg",
                id = 1,
                originalLanguage = "en",
                originalTitle = "Original Title",
                overview = "Some overview",
                popularity = 123.4f,
                posterPath = "/poster.jpg",
                releaseDate = "2025-01-01",
                title = "Movie A",
                video = false,
                voteAverage = 8.5f,
                voteCount = 1000,
                localId = 1,
                genreIds = listOf(1, 2)
            )
        )
        every { movieDao.getAllMovies() } returns flowOf(entities)

        repository.getCachedMovies().test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Movie A", result[0].title)
            awaitComplete()
        }
    }

    @Test
    fun `saveMoviesToDb inserts movies into DB`() = runTest {
        val entities = listOf(
            MovieEntity(
                adult = false,
                backdropPath = "/backdrop.jpg",
                id = 1,
                originalLanguage = "en",
                originalTitle = "Original Title",
                overview = "Some overview",
                popularity = 123.4f,
                posterPath = "/poster.jpg",
                releaseDate = "2025-01-01",
                title = "Movie Title",
                video = false,
                voteAverage = 8.5f,
                voteCount = 1000,
                localId = 1,
                genreIds = listOf(1, 2)
            )
        )

        repository.saveMoviesToDb(entities)

        coVerify { movieDao.insertMovies(entities) }
    }
}
