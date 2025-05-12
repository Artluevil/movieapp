package com.kbak.moviesapp.ui.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.domain.usecase.FetchMoviesUseCase
import com.kbak.moviesapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val fetchMoviesUseCase: FetchMoviesUseCase
) : ViewModel() {

    private val _moviesState = MutableStateFlow<ApiResult<List<Movie>>>(ApiResult.Loading)
    val moviesState = _moviesState.asStateFlow()

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            fetchMoviesUseCase().collect { result ->
                _moviesState.value = result
            }
        }
    }
}
