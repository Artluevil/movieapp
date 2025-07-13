package com.kbak.moviesapp.data.remote.mediator

import androidx.paging.*
import androidx.room.withTransaction
import com.kbak.moviesapp.data.local.AppDatabase
import com.kbak.moviesapp.data.local.MovieEntity
import com.kbak.moviesapp.data.remote.api.MovieApiService
import com.kbak.moviesapp.data.remote.model.toMovieEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val api: MovieApiService,
    private val db: AppDatabase
) : RemoteMediator<Int, MovieEntity>() {

    private var currentPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {

        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    currentPage = 1
                    1
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    currentPage += 1
                    currentPage
                }
            }

            val response = api.getPopularMovies("en-US", page)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            val moviesFromApi = response.body()?.results?.map { it.toMovieEntity() } ?: emptyList()
            val before = db.movieDao().countAllMovies()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.movieDao().deleteAllMovies()
                }
                db.movieDao().insertMovies(moviesFromApi)
            }

            val after = db.movieDao().countAllMovies()
            val newlyInserted = after - before
            val endReached = moviesFromApi.isEmpty() || newlyInserted == 0


            return MediatorResult.Success(endOfPaginationReached = endReached)

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}
