package com.quotespark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quotespark.data.Quote
import com.quotespark.data.QuoteCategory
import com.quotespark.data.QuoteRepository
import com.quotespark.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val currentQuote: Quote? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val quotesViewed: Int = 0,
    val selectedCategory: QuoteCategory = QuoteCategory.ALL,
    val availableQuotes: List<Quote> = emptyList(),
    val useApiMode: Boolean = false
)

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val repository: QuoteRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState: StateFlow<HomeUiState> = _homeState.asStateFlow()

    val favoriteQuotes: StateFlow<List<Quote>> = repository.getFavoriteQuotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var previousQuoteId: String? = null

    init {
        viewModelScope.launch {
            repository.seedLocalQuotesIfNeeded()
            loadQuotesForCategory(QuoteCategory.ALL)
        }
    }

    private suspend fun loadQuotesForCategory(category: QuoteCategory) {
        _homeState.update { it.copy(isLoading = true, error = null) }

        val localQuotes = repository.getQuotesByCategory(category)
        if (localQuotes.isNotEmpty()) {
            val randomQuote = pickRandom(localQuotes)
            _homeState.update {
                it.copy(
                    availableQuotes = localQuotes,
                    currentQuote = randomQuote,
                    isLoading = false
                )
            }
            previousQuoteId = randomQuote?.id
        }

        // Try to fetch from API in background
        if (_homeState.value.useApiMode) {
            fetchFromApi()
        }
    }

    fun nextQuote() {
        val state = _homeState.value
        val pool = state.availableQuotes.filter { it.id != previousQuoteId }

        val newQuote = if (pool.isEmpty()) {
            state.availableQuotes.randomOrNull()
        } else {
            pool.random()
        }

        previousQuoteId = newQuote?.id
        _homeState.update {
            it.copy(
                currentQuote = newQuote,
                // Force an immediate real-time update to the counter
                quotesViewed = it.quotesViewed + 1
            )
        }
    }

    fun selectCategory(category: QuoteCategory) {
        _homeState.update { it.copy(selectedCategory = category) }
        viewModelScope.launch {
            loadQuotesForCategory(category)
        }
    }

    fun toggleApiMode(enabled: Boolean) {
        _homeState.update { it.copy(useApiMode = enabled) }
        if (enabled) {
            viewModelScope.launch { fetchFromApi() }
        }
    }

    fun fetchFromApi() {
        viewModelScope.launch {
            _homeState.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.fetchRandomQuoteFromApi()) {
                is Resource.Success -> {
                    _homeState.update {
                        it.copy(
                            currentQuote = result.data,
                            isLoading = false,
                            quotesViewed = it.quotesViewed + 1
                        )
                    }
                    previousQuoteId = result.data.id
                }
                is Resource.Error -> {
                    _homeState.update {
                        it.copy(isLoading = false, error = result.message)
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun toggleFavorite(quote: Quote) {
        viewModelScope.launch {
            // 1. Calculate the new toggled status
            val newFavoriteStatus = !quote.isFavorite

            // 2. Safely update the repository database with the correct new state
            val quoteToUpdate = quote.copy(isFavorite = newFavoriteStatus)
            repository.toggleFavorite(quoteToUpdate)

            // 3. Update our current displayed Home quote state
            if (_homeState.value.currentQuote?.id == quote.id) {
                _homeState.update {
                    it.copy(currentQuote = quoteToUpdate)
                }
            }
            _homeState.update { state ->
                val updatedList = state.availableQuotes.map {
                    if (it.id == quote.id) quoteToUpdate else it
                }
                state.copy(availableQuotes = updatedList)
            }
        }
    }

    fun dismissError() {
        _homeState.update { it.copy(error = null) }
    }

    private fun pickRandom(quotes: List<Quote>): Quote? {
        return quotes.filter { it.id != previousQuoteId }.randomOrNull()
            ?: quotes.randomOrNull()
    }
}
