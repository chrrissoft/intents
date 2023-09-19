package com.chrrissoft.intents.custom.ui

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.RoundTripPageEvent
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.RoundTripPageEvent.PendingIntentEvent
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.RoundTripPageEvent.PendingIntentEvent.*
import com.chrrissoft.intents.custom.ui.CustomScreenState.RoundTripPageState.PendingIntentBuilder
import com.chrrissoft.intents.ui.components.Container
import com.chrrissoft.intents.ui.components.Flags
import com.chrrissoft.intents.ui.theme.getFilledIconToggleButtonColors

//const val ROUND_TRIP_PAGE_TAG = "ROUND_TRIP_PAGE_TAG"


@Composable
fun RoundTripPage(
    state: CustomScreenState.RoundTripPageState,
    onEvent: (RoundTripPageEvent) -> Unit,
) {
    NestedIntentBuilder(
        state = state.nestedIntentBuilder,
        onEvent = onEvent
    )

    Divider(
        color = colorScheme.primary.copy(.7f), modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )

    PendingIntentBuilder(
        state = state.pendingIntentBuilder,
        onEvent = onEvent,
    )

    Divider(
        color = colorScheme.primary.copy(.7f), modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )

    StartIntentBuilder(
        state = state.startIntent,
        onEvent = onEvent,
    )
}


@Composable
private fun PendingIntentBuilder(
    state: PendingIntentBuilder,
    onEvent: (PendingIntentEvent) -> Unit,
) {
    Container(
        title = "Pending Intent Builder"
    ) {
        Flags(
            tags = state.flags,
            onToggle = { flag ->
                onEvent(OnToggleFlags(flag))
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row {
            CancelPendingIntentButton(
                enabled = state.pendingIntent!=null, onClick = {
                    requireNotNull(state.pendingIntent)
                    onEvent(OnCancelPendingIntent(state.pendingIntent))
                }, modifier = Modifier
                    .weight(10f)
                    .padding(horizontal = 10.dp)
            )

            FilledIconToggleButton(
                checked = state.mutability,
                enabled = SDK_INT >= S,
                colors = getFilledIconToggleButtonColors(isCheckedAndDisabled = state.mutability && SDK_INT < S),
                onCheckedChange = { onEvent(OnChangeMutability(it)) },
                modifier = Modifier.padding(end = 10.dp)
            ) {
                Icon(imageVector = Icons.Rounded.Favorite, contentDescription = null)
            }
        }
    }
}
