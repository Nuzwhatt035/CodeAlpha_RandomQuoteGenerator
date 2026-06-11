package com.quotespark.data

import android.util.Log
import com.quotespark.data.local.QuoteDao
import com.quotespark.data.remote.QuoteApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}

@Singleton
class QuoteRepository @Inject constructor(
    private val quoteDao: QuoteDao,
    private val apiService: QuoteApiService
) {
    // Seed the local database with built-in quotes on first run
    suspend fun seedLocalQuotesIfNeeded() {
        val local = quoteDao.getLocalQuotes()
        if (local.isEmpty()) {
            quoteDao.upsertQuotes(LocalQuotes.all)
        }
    }

    suspend fun getQuotesByCategory(category: QuoteCategory): List<Quote> {
        return if (category == QuoteCategory.ALL) {
            quoteDao.getLocalQuotes().ifEmpty { LocalQuotes.all }
        } else {
            quoteDao.getQuotesByCategory(category.name).ifEmpty {
                LocalQuotes.all.filter { it.category == category }
            }
        }
    }

    fun getFavoriteQuotes(): Flow<List<Quote>> = quoteDao.getFavoriteQuotes()

    suspend fun toggleFavorite(quote: Quote) {
        // 1. Try updating the row directly if it already exists
        val rowsUpdated = quoteDao.updateFavoriteStatusDirectly(quote.id, quote.isFavorite)

        // 2. If no rows were updated, it's a new quote! Let's insert it fully
        if (rowsUpdated == 0) {
            quoteDao.upsertQuote(quote)
        }
    }
    suspend fun fetchRandomQuoteFromApi(): Resource<Quote> {
        return try {
            val response = apiService.getRandomQuote()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                // ZenQuotes returns an array with a single object for random calls
                val apiQuote = response.body()!!.first()
                val quote = Quote(
                    id = java.util.UUID.randomUUID().toString(), // Generate unique ID since ZenQuotes doesn't provide one
                    text = apiQuote.q,
                    author = apiQuote.a,
                    category = QuoteCategory.MOTIVATION, // Default category for random global fetch
                    isFromApi = true
                )
                quoteDao.upsertQuote(quote)
                Resource.Success(quote)
            } else {
                Resource.Error("Failed to fetch quote: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("QuoteRepository", "Network error", e)
            Resource.Error(e.message ?: "Unknown network error")
        }
    }

    suspend fun fetchQuotesFromApi(category: QuoteCategory = QuoteCategory.ALL): Resource<List<Quote>> {
        return try {
            // ZenQuotes fetches a bulk batch of 50 quotes
            val response = apiService.getRandomQuotes()

            if (response.isSuccessful && response.body() != null) {
                val quotes = response.body()!!.map { apiQuote ->
                    Quote(
                        id = java.util.UUID.randomUUID().toString(),
                        text = apiQuote.q,
                        author = apiQuote.a,
                        // If user requested a specific category, map it, otherwise randomize categories
                        category = if (category == QuoteCategory.ALL) getRandomCategory() else category,
                        isFromApi = true
                    )
                }

                // If user filtered by a specific category, only save and return those specific ones
                val filteredQuotes = if (category == QuoteCategory.ALL) {
                    quotes
                } else {
                    quotes.filter { it.category == category }
                }

                quoteDao.upsertQuotes(filteredQuotes)
                Resource.Success(filteredQuotes)
            } else {
                Resource.Error("Failed to fetch: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("QuoteRepository", "Network error", e)
            Resource.Error(e.message ?: "Unknown network error")
        }
    }

    // Helper function to randomly assign categories to bulk incoming web quotes
    private fun getRandomCategory(): QuoteCategory {
        val categories = QuoteCategory.values().filter { it != QuoteCategory.ALL }
        return categories.random()
    }



    private fun mapTagsToCategory(tags: List<String>): QuoteCategory {
        return when {
            tags.any { it.contains("motivat", true) || it.contains("inspir", true) } -> QuoteCategory.MOTIVATION
            tags.any { it.contains("success", true) || it.contains("business", true) } -> QuoteCategory.SUCCESS
            tags.any { it.contains("wisdom", true) || it.contains("knowledge", true) } -> QuoteCategory.STUDY
            tags.any { it.contains("happi", true) || it.contains("joy", true) } -> QuoteCategory.HAPPINESS
            tags.any { it.contains("life", true) || it.contains("living", true) } -> QuoteCategory.LIFE
            else -> QuoteCategory.MOTIVATION
        }
    }
}
