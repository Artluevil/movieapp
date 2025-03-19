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
        fetchGenres()
    }

    fun fetchGenres() {
        viewModelScope.launch {
            try {
                val genres = genreRepository.fetchGenresFromApi()
                Log.d("GenreViewModel", "🔥 Genres fetched from API: $genres")

                genreRepository.insertGenres(genres.map { GenreEntity(it.id, it.name) })

                genreRepository.getGenres().collect { savedGenres ->
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
