package com.quotespark.di

import android.content.Context
import androidx.room.Room
import com.quotespark.data.local.QuoteDao
import com.quotespark.data.local.QuoteDatabase
import com.quotespark.data.remote.QuoteApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            // Updated to the stable, active ZenQuotes endpoint
            .baseUrl("https://zenquotes.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideQuoteApiService(retrofit: Retrofit): QuoteApiService {
        return retrofit.create(QuoteApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): QuoteDatabase {
        return Room.databaseBuilder(
            context,
            QuoteDatabase::class.java,
            "quotespark_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideQuoteDao(database: QuoteDatabase): QuoteDao = database.quoteDao()
}
