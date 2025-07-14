package com.kbak.moviesapp.repository

import com.kbak.moviesapp.data.remote.api.MovieDetailsApiService
import com.kbak.moviesapp.data.remote.model.MovieDetailsResponse
import com.kbak.moviesapp.data.repository.MovieDetailsRepository
import com.kbak.moviesapp.utils.ApiResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class MovieDetailsRepositoryTest {

    private lateinit var apiService: MovieDetailsApiService
    private lateinit var repository: MovieDetailsRepository

    @BeforeEach
    fun setUp() {
        apiService = mockk()
        repository = MovieDetailsRepository(apiService)
    }

    @Test
    fun `getDetailsOfAMovie returns Success when API call is successful`() = runTest {
        val movieId = 123
        val mockResponse = MovieDetailsResponse(
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

        coEvery { apiService.getMovieDetails(movieId) } returns Response.success(mockResponse)

        val result = repository.getDetailsOfAMovie(movieId)

        assertTrue(result is ApiResult.Success)
        assertEquals(mockResponse, (result as ApiResult.Success).data)
    }

    @Test
    fun `getDetailsOfAMovie returns Error when API response body is null`() = runTest {
        val movieId = 123

        coEvery { apiService.getMovieDetails(movieId) } returns Response.success(null)

        val result = repository.getDetailsOfAMovie(movieId)

        assertTrue(result is ApiResult.Error)
        assertEquals("Empty response body", (result as ApiResult.Error).message)
    }

    @Test
    fun `getDetailsOfAMovie returns Error when API call fails`() = runTest {
        val movieId = 123
        val errorResponse = Response.error<MovieDetailsResponse>(
            404,
            ResponseBody.create("application/json".toMediaType(), "{\"error\":\"Not found\"}")
        )

        coEvery { apiService.getMovieDetails(movieId) } returns errorResponse

        val result = repository.getDetailsOfAMovie(movieId)

        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("API Error"))
    }

    @Test
    fun `getDetailsOfAMovie returns Error when exception is thrown`() = runTest {
        val movieId = 123
        coEvery { apiService.getMovieDetails(movieId) } throws RuntimeException("Network failure")

        val result = repository.getDetailsOfAMovie(movieId)

        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Network Error"))
    }
}