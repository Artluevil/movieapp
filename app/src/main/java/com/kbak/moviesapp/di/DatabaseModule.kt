package com.kbak.moviesapp.di

import android.content.Context
import androidx.room.Room
import com.kbak.moviesapp.data.local.AppDatabase
import com.kbak.moviesapp.data.local.GenreDao
import com.kbak.moviesapp.data.local.MovieDao
import com.kbak.moviesapp.data.remote.api.GenreApiService
import com.kbak.moviesapp.data.repository.GenreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideGenreDao(database: AppDatabase): GenreDao {
        return database.genreDao()
    }

    @Provides
    fun provideGenreRepository(genreDao: GenreDao, genreApiService: GenreApiService): GenreRepository {
        return GenreRepository(genreDao, genreApiService)
    }
    @Provides
    fun provideMovieDao(database: AppDatabase): MovieDao {
        return database.movieDao()
    }
}
