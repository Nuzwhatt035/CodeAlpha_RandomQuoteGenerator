package com.quotespark.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quotespark.data.Quote

@Composable
fun QuoteCard(
    quote: Quote,
    onFavoriteClick: (Quote) -> Unit,
    onShareClick: (Quote) -> Unit,
    modifier: Modifier = Modifier,
    showActions: Boolean = true
) {
    var isFavorite by remember(quote.id) { mutableStateOf(quote.isFavorite) }

    // Animate favorite icon scale
    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "favorite_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Opening quote mark
            Text(
                text = "\u201C",
                fontSize = 72.sp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                lineHeight = 0.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .offset(y = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quote text
            Text(
                text = quote.text,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Divider
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(0.4f),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Author
            Text(
                text = "— ${quote.author}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            // Category chip
            Spacer(modifier = Modifier.height(12.dp))
            SuggestionChip(
                onClick = {},
                label = {
                    Text(
                        text = quote.category.displayName,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )

            if (showActions) {
                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 1. Single and Clean Favorite Button
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            onFavoriteClick(quote)
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.scale(scale)
                        )
                    }

                    // 2. Share button
                    IconButton(onClick = { onShareClick(quote) }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share quote",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}