package com.chrrissoft.intents.custom.ui

import androidx.compose.runtime.Composable
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.SendPageEvent
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.SendPageEvent.OnChangeIntentBuilder
import com.chrrissoft.intents.custom.ui.CustomScreenState.SendPageState

@Composable
fun SendPage(
    state: SendPageState,
    onEvent: (SendPageEvent) -> Unit
) {
    SendIntentBuilder(
        state = state.builder,
        onChangeState = {
            onEvent(OnChangeIntentBuilder(it))
        }
    )
}
