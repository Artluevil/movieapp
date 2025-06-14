package com.kbak.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.domain.usecase.FetchPagedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val fetchPagedMoviesUseCase: FetchPagedMoviesUseCase
) : ViewModel() {

    val movies: Flow<PagingData<Movie>> = fetchPagedMoviesUseCase().cachedIn(viewModelScope)
}
