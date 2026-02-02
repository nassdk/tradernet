package com.nassdk.tradernet.features.quotes.presentation.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nassdk.tradernet.R
import com.nassdk.tradernet.features.quotes.di.QuotesComponent
import com.nassdk.tradernet.features.quotes.presentation.model.QuoteColor
import com.nassdk.tradernet.features.quotes.presentation.model.QuoteUiModel
import com.nassdk.tradernet.ui.components.PriceText
import com.nassdk.tradernet.ui.modifiers.shimmer
import com.nassdk.tradernet.ui.theme.DividerLight
import com.nassdk.tradernet.ui.theme.IconTint
import com.nassdk.tradernet.ui.theme.QuoteNegative
import com.nassdk.tradernet.ui.theme.QuoteNeutral
import com.nassdk.tradernet.ui.theme.QuotePositive
import kotlinx.collections.immutable.ImmutableList

private fun QuoteColor.toComposeColor(): Color {
    return when (this) {
        QuoteColor.POSITIVE -> QuotePositive
        QuoteColor.NEGATIVE -> QuoteNegative
        QuoteColor.NEUTRAL -> QuoteNeutral
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuotesContent() {
    val viewModel = viewModel { QuotesComponent.createQuotesViewModel() }
    val quotes by viewModel.quotes.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(title = { Text("Tradernet") })
        },
        content = { paddingValues ->
            Crossfade(
                modifier = Modifier.padding(paddingValues),
                targetState = quotes.isNotEmpty(),
            ) { hasContent ->
                if (hasContent) {
                    QuotesContent(quotes = quotes)
                } else {
                    QuotesLoadingContent()
                }
            }

        }
    )
}

@Composable
internal fun QuotesContent(quotes: ImmutableList<QuoteUiModel>) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            itemsIndexed(
                items = quotes,
                key = { _, item -> item.uiKey }
            ) { index, quotir ->
                QuoteItem(quotir)
                if (index != quotes.lastIndex) {
                    HorizontalDivider(
                        color = DividerLight,
                        modifier = Modifier.padding(horizontal = 18.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun QuotesLoadingContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(15) {
            ShimmerQuoteItem()
        }
    }
}

@Composable
private fun ShimmerQuoteItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(width = 120.dp, height = 20.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
                    .shimmer()
            )
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 14.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
                    .shimmer()
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(width = 60.dp, height = 18.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
                    .shimmer()
            )
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 14.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
                    .shimmer()
            )
        }

        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = IconTint,
        )
    }
}

@Composable
private fun QuoteItem(quote: QuoteUiModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            LogoName(quote.title, quote.logoUrl)
            quote.subtitle?.let { subtitle ->
                Text(
                    maxLines = 1,
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray,
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            PriceText(
                price = quote.trailingTitle,
                baseColor = quote.trailingTitleColor.toComposeColor(),
                changeDirection = quote.priceChangeDirection,
            )

            Text(
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                text = quote.trailingSubtitle,
            )
        }

        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = IconTint,
        )
    }
}


@Composable
private fun LogoName(name: String, logoUrl: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AsyncImage(
            model = logoUrl,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Text(text = name, fontSize = 20.sp)
    }
}

@Preview
@Composable
private fun QuotesContentPreview() {
    QuotesContent(quotes = PreviewArguments.getPreviewQuotes())
}

@Preview
@Composable
private fun QuotesLoadingContentPreview() {
    QuotesLoadingContent()
}
