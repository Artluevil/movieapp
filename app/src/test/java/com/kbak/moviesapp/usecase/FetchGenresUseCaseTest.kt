package com.kbak.moviesapp.usecase

import app.cash.turbine.test
import com.kbak.moviesapp.data.local.GenreEntity
import com.kbak.moviesapp.data.repository.GenreRepository
import com.kbak.moviesapp.domain.usecase.FetchGenresUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

@ExperimentalCoroutinesApi
class FetchGenresUseCaseTest {

    private lateinit var repository: GenreRepository
    private lateinit var useCase: FetchGenresUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = FetchGenresUseCase(repository)
    }

    @Test
    fun `invoke fetches genres, inserts into DB, and emits from DB`() = runTest {
        val genreList = listOf(
            GenreEntity(id = 1, name = "Action"),
            GenreEntity(id = 2, name = "Drama")
        )

        coEvery { repository.fetchGenresFromApi() } returns genreList
        coEvery { repository.getGenres() } returns flowOf(genreList)

        useCase().test {
            assertEquals(genreList, awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 1) { repository.fetchGenresFromApi() }
        coVerify(exactly = 1) { repository.insertGenres(genreList) }
        coVerify(exactly = 1) { repository.getGenres() }
    }
    @Test
    fun `invoke emits empty list when both API and DB return empty`() = runTest {

        coEvery { repository.fetchGenresFromApi() } returns emptyList()
        coEvery { repository.getGenres() } returns flowOf(emptyList())

        useCase().test {
            assertEquals(emptyList<GenreEntity>(), awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 1) { repository.fetchGenresFromApi() }
        coVerify(exactly = 1) { repository.getGenres() }
    }

    @Test
    fun `invoke returns local genres when API throws exception`() = runTest {
        val localGenres = listOf(
            GenreEntity(id = 3, name = "Comedy"),
            GenreEntity(id = 4, name = "Horror")
        )

        coEvery { repository.fetchGenresFromApi() } returns emptyList()
        coEvery { repository.getGenres() } returns flowOf(localGenres)

        useCase().test {
            assertEquals(localGenres, awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 1) { repository.fetchGenresFromApi() }
        coVerify(exactly = 1) { repository.getGenres() }
        coVerify(exactly = 0) { repository.insertGenres(any()) }
    }
}