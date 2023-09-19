package com.chrrissoft.intents.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.chrrissoft.intents.ui.theme.cardColors

@Composable
fun Container(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.padding(10.dp),
        colors = cardColors
    ) {
        Spacer(modifier = Modifier.padding(top = 10.dp))
        if (title!=null) {
            Text(
                text = title,
                style = typography.titleLarge.copy(
                    color = colorScheme.primary,
                    fontWeight = Medium,
                    textAlign = Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Divider(
                modifier = Modifier.padding(10.dp),
                color = colorScheme.primary.copy(.7f),
            )
        }
        content()
        Spacer(modifier = Modifier.padding(top = 10.dp))
    }
}
