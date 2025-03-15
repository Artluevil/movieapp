package com.kbak.moviesapp.di

import android.content.Context
import androidx.room.Room
import com.kbak.moviesapp.data.local.GenreDao
import com.kbak.moviesapp.data.local.GenreDatabase
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
    fun provideGenreDatabase(@ApplicationContext context: Context): GenreDatabase {
        return Room.databaseBuilder(
            context,
            GenreDatabase::class.java,
            "genre_database"
        ).build()
    }

    @Provides
    fun provideGenreDao(database: GenreDatabase): GenreDao {
        return database.genreDao()
    }

    @Provides
    fun provideGenreRepository(genreDao: GenreDao, genreApiService: GenreApiService): GenreRepository {
        return GenreRepository(genreDao, genreApiService)
    }
}
