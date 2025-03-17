package com.kbak.moviesapp.data.repository

import android.util.Log
import com.kbak.moviesapp.data.local.MovieDao
import com.kbak.moviesapp.data.local.MovieEntity
import com.kbak.moviesapp.data.remote.api.MovieApiService
import com.kbak.moviesapp.data.remote.model.Movie
import com.kbak.moviesapp.data.remote.model.MovieListResponse
import com.kbak.moviesapp.data.remote.model.toMovie
import com.kbak.moviesapp.data.remote.model.toMovieEntity
import com.kbak.moviesapp.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao // ✅ Inject MovieDao for Room DB
) {
    init {
        Log.d("HiltCheck", "✅ Hilt injected MovieRepository successfully!")
    }

    /**
     * Fetch popular movies from API and store in Room DB.
     */
    suspend fun getPopularMovies(page: Int): ApiResult<MovieListResponse> {
        return try {
            val response: Response<MovieListResponse> = apiService.getPopularMovies(page = page)
            if (response.isSuccessful) {
                response.body()?.let { movieListResponse ->
                    val movies = movieListResponse.results

                    // Store movies in Room Database
                    movieDao.deleteAllMovies() // Clear old movies
                    movieDao.insertMovies(movies.map { it.toMovieEntity() })

                    movies.forEach { movie ->
                        Log.d("MovieRepository", "✅ Movie: ${movie.title}, ${movie.overview}, ${movie.posterPath}")
                    }
                    ApiResult.Success(movieListResponse)
                } ?: ApiResult.Error("Empty response body")
            } else {
                Log.e("MovieRepository", "❌ API Error: ${response.code()} - ${response.message()}")
                ApiResult.Error("API Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("MovieRepository", "❌ Network Error: ${e.message}")
            ApiResult.Error("Network Error: ${e.message}")
        }
    }

    /**
     * Fetch all cached movies from Room DB (for offline support).
     */
    fun getCachedMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies().map { movieEntities ->
            movieEntities.map { it.toMovie() }
        }
    }

    /**
     * Fetch a specific movie by ID (from Room DB).
     */
    fun getMovieById(movieId: Int): Flow<Movie?> {
        return movieDao.getMovieById(movieId).map { it?.toMovie() }
    }
    suspend fun saveMoviesToDb(movies: List<MovieEntity>) {
        movieDao.insertMovies(movies)
        Log.d("MovieRepository", "💾 Saved ${movies.size} movies to Room DB!")
    }
}
