package com.chrrissoft.intents.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Spoke
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawerIcon(
    modifier: Modifier = Modifier,
    title: String = "Intents App",
    icon: ImageVector = Icons.Rounded.Spoke,
    description: String? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val size = LocalConfiguration.current.screenWidthDp.div(3)
        DrawerTitle(title)
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(size.dp)
        )
        Divider(color = colorScheme.primary, modifier = Modifier.padding(10.dp))
    }
}

@Composable
fun DrawerTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier,
        style = typography.displayMedium.copy(
            textAlign = Center,
            fontWeight = Medium,
            color = colorScheme.primary,
        )
    )
}