package com.nassdk.tradernet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nassdk.tradernet.features.quotes.presentation.ui.QuotesContent
import com.nassdk.tradernet.ui.theme.TradernetTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TradernetTheme {
                QuotesContent()
            }
        }
    }
}