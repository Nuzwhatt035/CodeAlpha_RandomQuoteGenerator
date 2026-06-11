package com.quotespark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.quotespark.ui.QuoteSparkNavGraph
import com.quotespark.ui.theme.QuoteSparkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuoteSparkTheme {
                QuoteSparkNavGraph()
            }
        }
    }
}
