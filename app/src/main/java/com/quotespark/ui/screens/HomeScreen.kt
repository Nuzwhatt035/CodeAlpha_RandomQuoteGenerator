package com.quotespark.ui.screens

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quotespark.ui.components.CategoryFilterRow
import com.quotespark.ui.components.QuoteCard
import com.quotespark.viewmodel.QuoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToFavorites: () -> Unit,
    viewModel: QuoteViewModel
) {
    val state by viewModel.homeState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "QuoteSpark",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (state.quotesViewed > 0) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = "Viewed: ${state.quotesViewed}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favorites",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(12.dp))

            CategoryFilterRow(
                selectedCategory = state.selectedCategory,
                onCategorySelected = { category -> viewModel.selectCategory(category) }
            )

            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 300.dp),
                contentAlignment = Alignment.Center
            ) {

                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                    }

                    state.error != null && state.currentQuote == null -> {
                        ErrorState(
                            message = state.error!!,
                            onRetry = { viewModel.fetchFromApi() },
                            onDismiss = { viewModel.dismissError() }
                        )
                    }

                    state.currentQuote != null -> {
                        AnimatedContent(
                            targetState = state.currentQuote!!,
                            transitionSpec = {
                                if (initialState.id != targetState.id) {
                                    (slideInHorizontally { width -> width } + fadeIn())
                                        .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
                                } else {
                                    EnterTransition.None togetherWith ExitTransition.None
                                }
                            },
                            label = "quote_transition"
                        ) { animatedQuote ->
                            QuoteCard(
                                quote = animatedQuote,
                                onFavoriteClick = { quote -> viewModel.toggleFavorite(quote) },
                                onShareClick = { quote ->
                                    val shareText = "\"${quote.text}\"\n— ${quote.author}\n\nShared via QuoteSpark"
                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                    }
                                    context.startActivity(Intent.createChooser(intent, "Share Quote"))
                                }
                            )
                        }

                        if (state.error != null) {
                            Spacer(Modifier.height(12.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = state.error!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    if (state.useApiMode) viewModel.fetchFromApi()
                    else viewModel.nextQuote()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("New Quote", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Connection Issue",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onDismiss) {
                Text("Use Offline")
            }
            Button(onClick = onRetry) {
                Text("Try Again")
            }
        }
    }
}
