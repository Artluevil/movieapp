package com.kbak.moviesapp.di

import com.kbak.moviesapp.BuildConfig
import com.kbak.moviesapp.data.remote.api.GenreApiService
import com.kbak.moviesapp.data.remote.api.MovieApiService
import com.kbak.moviesapp.data.remote.api.MovieDetailsApiService
import com.kbak.moviesapp.data.remote.api.MovieImagesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val url = original.url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build()

            val request = original.newBuilder().url(url).build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideGenreApiService(retrofit: Retrofit): GenreApiService {
        return retrofit.create(GenreApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideMovieDetailsApiService(retrofit: Retrofit): MovieDetailsApiService {
        return retrofit.create(MovieDetailsApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideMovieImagesApiService(retrofit: Retrofit): MovieImagesApiService {
        return retrofit.create(MovieImagesApiService::class.java)
    }
}
