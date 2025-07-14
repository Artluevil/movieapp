package com.kbak.moviesapp.repository

import com.kbak.moviesapp.data.remote.api.MovieImagesApiService
import com.kbak.moviesapp.data.remote.model.MovieImagesResponse
import com.kbak.moviesapp.data.repository.MovieImagesRepository
import com.kbak.moviesapp.utils.ApiResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import retrofit2.Response

@ExperimentalCoroutinesApi
class MovieImagesRepositoryTest {

    private lateinit var apiService: MovieImagesApiService
    private lateinit var repository: MovieImagesRepository

    @BeforeEach
    fun setUp() {
        apiService = mockk()
        repository = MovieImagesRepository(apiService)
    }

    @Test
    fun `getMovieImages returns Success when API call is successful`() = runTest {
        val movieId = 123
        val mockResponse = MovieImagesResponse(
            id = movieId,
            backdrops = emptyList(),
            posters = emptyList()
        )

        coEvery { apiService.getMovieImages(movieId) } returns Response.success(mockResponse)

        val result = repository.getMovieImages(movieId)

        assertTrue(result is ApiResult.Success)
        assertEquals(mockResponse, (result as ApiResult.Success).data)
    }

    @Test
    fun `getMovieImages returns Error when response body is null`() = runTest {
        val movieId = 123

        coEvery { apiService.getMovieImages(movieId) } returns Response.success(null)

        val result = repository.getMovieImages(movieId)

        assertTrue(result is ApiResult.Error)
        assertEquals("Empty response body", (result as ApiResult.Error).message)
    }

    @Test
    fun `getMovieImages returns Error when API call fails`() = runTest {
        val movieId = 123

        val errorResponse = Response.error<MovieImagesResponse>(
            404,
            ResponseBody.create("application/json".toMediaType(), "{\"error\":\"Not found\"}")
        )

        coEvery { apiService.getMovieImages(movieId) } returns errorResponse

        val result = repository.getMovieImages(movieId)

        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("API Error"))
    }

    @Test
    fun `getMovieImages returns Error when exception is thrown`() = runTest {
        val movieId = 123

        coEvery { apiService.getMovieImages(movieId) } throws RuntimeException("Network failure")

        val result = repository.getMovieImages(movieId)

        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Network Error"))
    }
}