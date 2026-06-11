package com.quotespark.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.quotespark.data.Quote
import com.quotespark.data.QuoteCategory

class Converters {
    @TypeConverter
    fun fromCategory(value: QuoteCategory): String = value.name

    @TypeConverter
    fun toCategory(value: String): QuoteCategory = QuoteCategory.valueOf(value)
}

@Database(entities = [Quote::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
}
