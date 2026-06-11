package com.quotespark.data.local

import androidx.room.*
import com.quotespark.data.Quote
import com.quotespark.data.QuoteCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quotes WHERE isFavorite = 1 ORDER BY id ASC")
    fun getFavoriteQuotes(): Flow<List<Quote>>

    @Query("SELECT * FROM quotes WHERE category = :category OR :category = 'ALL'")
    suspend fun getQuotesByCategory(category: String): List<Quote>

    @Query("SELECT * FROM quotes WHERE id = :id")
    suspend fun getQuoteById(id: String): Quote?

    @Upsert
    suspend fun upsertQuote(quote: Quote)

    @Upsert
    suspend fun upsertQuotes(quotes: List<Quote>)

    @Query("UPDATE quotes SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM quotes WHERE isFromApi = 1")
    suspend fun getApiQuoteCount(): Int

    @Query("SELECT * FROM quotes WHERE isFromApi = 0")
    suspend fun getLocalQuotes(): List<Quote>

    @Query("UPDATE quotes SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatusDirectly(id: String, isFavorite: Boolean): Int
}
