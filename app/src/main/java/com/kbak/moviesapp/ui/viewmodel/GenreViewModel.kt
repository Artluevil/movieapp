package com.kbak.moviesapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbak.moviesapp.data.local.GenreEntity
import com.kbak.moviesapp.data.repository.GenreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val genreRepository: GenreRepository
) : ViewModel() {


    init {
        Log.d("GenreViewModel", "✅ GenreViewModel initialized!")
        fetchGenres() // ✅ Automatically fetch genres when ViewModel is created
    }

    fun fetchGenres() {
        viewModelScope.launch {
            try {
                val genres = genreRepository.fetchGenresFromApi() // ✅ Fetch from API
                Log.d("GenreViewModel", "🔥 Genres fetched from API: $genres")

                // ✅ Insert into Room DB
                genreRepository.insertGenres(genres.map { GenreEntity(it.id, it.name) })
                Log.d("GenreViewModel", "✅ Genres saved to Room DB!")

                // ✅ Fetch from Room to verify
                genreRepository.getGenres().collect { savedGenres ->
                    Log.d("GenreViewModel", "💾 Fetched from Room DB: $savedGenres")
                }
            } catch (e: Exception) {
                Log.e("GenreViewModel", "❌ Error fetching genres: ${e.message}", e)
            }
        }
    }
    fun getGenreNameById(genreId: Int): String? {
        var genreName: String? = null
        runBlocking {
            genreRepository.getGenreById(genreId).collect { name ->
                genreName = name
            }
        }
        return genreName
    }
}
