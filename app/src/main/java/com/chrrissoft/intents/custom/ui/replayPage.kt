package com.chrrissoft.intents.custom.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chrrissoft.intents.Util.getBundleData
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.ReplayPageEvent
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.ReplayPageEvent.OnChangeRoundTripPage
import com.chrrissoft.intents.custom.ui.CustomScreenState.ReplayPageState
import com.chrrissoft.intents.ui.components.Actions
import com.chrrissoft.intents.ui.components.BundleDataItem
import com.chrrissoft.intents.ui.components.Container

@Composable
fun ReplayPage(
    state: ReplayPageState,
    onEvent: (ReplayPageEvent) -> Unit,
) {
    if (state.intent==null) {
        Box(modifier = Modifier
            .clickable { onEvent(OnChangeRoundTripPage()) }
            .fillMaxSize()
        ) {
            Text(text = "Empty replay, tap to open app companion")
        }
    } else {
        IntentReader(state.intent)
    }
}

@Composable
fun IntentReader(
    intent: Intent,
) {
    Container(title = "Intent Reader") {
        Actions(
            action = "${intent.action}",
            actions = CustomScreenState.Actions.companionActions,
            onChangeAction = {}
        )
    }
}
