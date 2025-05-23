package com.kbak.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbak.moviesapp.data.remote.model.MovieDetailsResponse
import com.kbak.moviesapp.domain.usecase.FetchMovieDetailsUseCase
import com.kbak.moviesapp.domain.usecase.ResolveGenreNamesUseCase
import com.kbak.moviesapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val fetchMovieDetailsUseCase: FetchMovieDetailsUseCase,
    private val resolveGenreNamesUseCase: ResolveGenreNamesUseCase
) : ViewModel() {

    private val _movieDetailsState = MutableStateFlow<ApiResult<MovieDetailsResponse>>(ApiResult.Loading)
    val movieDetailsState = _movieDetailsState.asStateFlow()

    private val _genreNames = MutableStateFlow("")
    val genreNames = _genreNames.asStateFlow()

    fun fetchMovieDetails(movieId: Int, genreIds: List<Int>) {
        viewModelScope.launch {
            // Collect resolved genre names
            launch {
                resolveGenreNamesUseCase(genreIds).collect { resolvedNames ->
                    _genreNames.value = resolvedNames
                }
            }

            // Collect movie details
            fetchMovieDetailsUseCase(movieId).collect { result ->
                _movieDetailsState.value = result
            }
        }
    }
}