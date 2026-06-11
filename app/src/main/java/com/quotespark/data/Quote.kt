package com.quotespark.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class QuoteCategory(val displayName: String) {
    ALL("All"),
    MOTIVATION("Motivation"),
    SUCCESS("Success"),
    STUDY("Study"),
    LIFE("Life"),
    HAPPINESS("Happiness")
}

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey val id: String,
    val text: String,
    val author: String,
    val category: QuoteCategory,
    val isFavorite: Boolean = false,
    val isFromApi: Boolean = false
)

// API response models
data class QuotableApiResponse(
    val _id: String,
    val content: String,
    val author: String,
    val tags: List<String>
)

data class ZenQuoteResponse(
    val q: String, // quote text
    val a: String  // author
)
