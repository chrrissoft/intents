package com.chrrissoft.intents.common.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.InputChipDefaults.inputChipBorder
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.chrrissoft.intents.common.ui.CommonScreenEvent.ResolveEvent
import com.chrrissoft.intents.common.ui.CommonScreenEvent.ResolveEvent.OnChangeAction
import com.chrrissoft.intents.common.ui.CommonScreenState.ResolvePage.Companion.actions
import com.chrrissoft.intents.ui.components.Container
import com.chrrissoft.intents.ui.theme.inputChipColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResolvePage(
    state: CommonScreenState.ResolvePage,
    onEvent: (ResolveEvent) -> Unit
) {
    Container(
        title = "Actions",
        content = {
            LazyRow(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                items(actions) { action ->
                    InputChip(
                        selected = state.action==action,
                        onClick = { onEvent(OnChangeAction(action)) },
                        label = { Text("${action?.takeLastWhile { "$it" != "." }}") },
                        colors = inputChipColors,
                        border = inputChipBorder(borderColor = colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    )

    Divider(color = colorScheme.primary)

    Container(
        title = "Best",
        content = {
            if (state.bestIcon!=null && state.bestLabel!=null) {
                Row(
                    verticalAlignment = CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .height(100.dp)
                        .fillMaxWidth()
                ) {
                    DrawableImage(drawable = state.bestIcon, modifier = Modifier.fillMaxHeight())
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = state.bestLabel.toString(),
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )

    Divider(color = colorScheme.primary)

    LazyColumn {
        item { Spacer(modifier = Modifier.height(10.dp)) }
        items(state.resolved.toList()) {
            Item(drawable = it.first, label = it.second)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun DrawableImage(
    drawable: Drawable,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    val imageBitmap = remember(drawable) {
        drawable.toBitmap()
    }

    val painter = remember(imageBitmap) {
        BitmapPainter(imageBitmap.asImageBitmap())
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}

@Composable
fun Item(
    drawable: Drawable,
    label: CharSequence,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = CenterVertically,
        modifier = modifier
            .height(70.dp)
            .padding(horizontal = 10.dp)
            .clip(shapes.large)
            .background(colorScheme.primaryContainer)
            .fillMaxWidth()
    ) {
        DrawableImage(drawable = drawable, modifier = Modifier.fillMaxHeight())
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label.toString(),
            style = typography.titleMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}
