package com.quotespark.data.remote

import com.quotespark.data.ZenQuoteResponse
import retrofit2.Response
import retrofit2.http.GET

interface QuoteApiService {

    // ZenQuotes API - Stable and active free fallback
    @GET("api/random")
    suspend fun getRandomQuote(): Response<List<ZenQuoteResponse>>

    // ZenQuotes returns a list of 50 quotes for its quotes endpoint
    @GET("api/quotes")
    suspend fun getRandomQuotes(): Response<List<ZenQuoteResponse>>
}