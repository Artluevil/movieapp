package com.kbak.moviesapp.repository

import com.kbak.moviesapp.data.local.GenreDao
import com.kbak.moviesapp.data.local.GenreEntity
import com.kbak.moviesapp.data.remote.api.GenreApiService
import com.kbak.moviesapp.data.remote.api.GenreResponse
import com.kbak.moviesapp.data.repository.GenreRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class GenreRepositoryTest {

    private lateinit var repository: GenreRepository
    private val genreDao = mockk<GenreDao>(relaxed = true)
    private val genreApiService = mockk<GenreApiService>()

    @BeforeEach
    fun setUp() {
        repository = GenreRepository(genreDao, genreApiService)
    }

    @Test
    fun `fetchGenresFromApi returns mapped list on success`() = runTest {
        val apiGenres = listOf(
            GenreDto(id = 1, name = "Action"),
            GenreDto(id = 2, name = "Drama")
        )
        val genreEntities = apiGenres.map { GenreEntity(it.id, it.name) }
        val response = GenreResponse(genres = genreEntities)

        coEvery { genreApiService.getGenres() } returns response

        val result = repository.fetchGenresFromApi()

        assertEquals(2, result.size)
        assertEquals("Action", result[0].name)
    }

    @Test
    fun `fetchGenresFromApi returns emptyList on failure`() = runTest {
        coEvery { genreApiService.getGenres() } throws RuntimeException("fail")

        val result = repository.fetchGenresFromApi()

        assertTrue(result.isEmpty())
    }
}

data class GenreDto(
    val id: Int,
    val name: String
)