package com.kbak.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbak.moviesapp.domain.usecase.FetchGenresUseCase
import com.kbak.moviesapp.domain.usecase.GetGenreNameByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val fetchGenresUseCase: FetchGenresUseCase,
    private val getGenreNameByIdUseCase: GetGenreNameByIdUseCase
) : ViewModel() {

    init {
        fetchGenres()
    }

    fun fetchGenres() {
        viewModelScope.launch {
            fetchGenresUseCase().onEach { _ -> }
        }
    }

    fun getGenreNameById(genreId: Int, callback: (String?) -> Unit) {
        viewModelScope.launch {
            getGenreNameByIdUseCase(genreId).collect { name ->
                callback(name)
            }
        }
    }
}

