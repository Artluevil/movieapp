package com.kbak.moviesapp.domain.usecase

import androidx.paging.*
import com.kbak.moviesapp.data.local.AppDatabase
import com.kbak.moviesapp.data.remote.api.MovieApiService
import com.kbak.moviesapp.data.remote.mediator.MovieRemoteMediator
import com.kbak.moviesapp.data.remote.model.toMovie
import com.kbak.moviesapp.data.remote.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FetchPagedMoviesUseCase @Inject constructor(
    private val db: AppDatabase,
    private val api: MovieApiService
) {
    operator fun invoke(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                initialLoadSize = 40,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(api, db),
            pagingSourceFactory = { db.movieDao().getMoviesPaging() }
        ).flow.map { pagingData ->
            pagingData.map { it.toMovie() }
        }
    }
}


