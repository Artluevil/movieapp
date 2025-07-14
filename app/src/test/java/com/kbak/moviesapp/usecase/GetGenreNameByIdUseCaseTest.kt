package com.kbak.moviesapp.usecase

import app.cash.turbine.test
import com.kbak.moviesapp.data.repository.GenreRepository
import com.kbak.moviesapp.domain.usecase.GetGenreNameByIdUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class GetGenreNameByIdUseCaseTest {

    private lateinit var repository: GenreRepository
    private lateinit var useCase: GetGenreNameByIdUseCase

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = GetGenreNameByIdUseCase(repository)
    }

    @Test
    fun `returns genre name when found`() = runTest {
        val genreId = 1
        val expectedName = "Action"

        every { repository.getGenreById(genreId) } returns flowOf(expectedName)

        useCase(genreId).test {
            assertEquals(expectedName, awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 1) { repository.getGenreById(genreId) }
    }

    @Test
    fun `returns null when genre not found`() = runTest {
        val genreId = 999

        every { repository.getGenreById(genreId) } returns flowOf(null)

        useCase(genreId).test {
            assertEquals(null, awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 1) { repository.getGenreById(genreId) }
    }
}
