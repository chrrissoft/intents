package com.chrrissoft.intents.custom.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.RoundTripPageEvent.NestedIntentBuilderEvent
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.RoundTripPageEvent.NestedIntentBuilderEvent.OnChangeAction
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.RoundTripPageEvent.NestedIntentBuilderEvent.OnRemoveBundleData
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.RoundTripPageEvent.StartIntentBuilderEvent
import com.chrrissoft.intents.custom.ui.CustomScreenState.Actions.actions
import com.chrrissoft.intents.custom.ui.CustomScreenState.Actions.companionActions
import com.chrrissoft.intents.custom.ui.CustomScreenState.Actions.sendActions
import com.chrrissoft.intents.custom.ui.CustomScreenState.IntentBuilder
import com.chrrissoft.intents.custom.ui.CustomScreenState.IntentBuilder.Component.Companion.components
import com.chrrissoft.intents.custom.ui.CustomScreenState.IntentBuilder.Component.Companion.componentsCompanion
import com.chrrissoft.intents.ui.components.IntentBuilder

@Composable
fun SendIntentBuilder(
    state: IntentBuilder,
    onChangeState: (IntentBuilder) -> Unit,
) {
    IntentBuilder(
        title = "Send Intent Builder",
        actions = sendActions,
        state = state,
        components = componentsCompanion,
        onChangeComponent = {
            onChangeState(state.copy(component = it))
        },
        onAddBundleData = {
            onChangeState(state.copy(bundleData = state.bundleData.plus(it)))
        },
        onRemoveBundleData = {
            onChangeState(state.copy(bundleData = state.bundleData.minus(it)))
        },
        onChangeAction = {
            onChangeState(state.copy(action = it))
        }
    )
}


//////////////////////////////////////////////////////

@Composable
fun NestedIntentBuilder(
    state: IntentBuilder,
    onEvent: (NestedIntentBuilderEvent) -> Unit,
) {
    IntentBuilder(
        title = "Nested Intent Builder",
        actions = companionActions,
        state = state,
        components = components,
        onChangeComponent = {
            onEvent(NestedIntentBuilderEvent.OnChangeComponent(it))
        },
        onAddBundleData = {
            onEvent(NestedIntentBuilderEvent.OnAddBundleData(it))
        },
        onRemoveBundleData = {
            onEvent(OnRemoveBundleData(it))
        },
        onChangeAction = {
            onEvent(OnChangeAction(it))
        }
    )
}


@Composable
fun StartIntentBuilder(
    state: IntentBuilder,
    onEvent: (StartIntentBuilderEvent) -> Unit,
) {
    IntentBuilder(
        title = "Start Intent Builder",
        state = state,
        actions = actions,
        components = componentsCompanion,
        onChangeComponent = {
            onEvent(StartIntentBuilderEvent.OnChangeComponent(it))
        },
        onAddBundleData = {
            onEvent(StartIntentBuilderEvent.OnAddBundleData(it))
        },
        onRemoveBundleData = {
            onEvent(StartIntentBuilderEvent.OnRemoveBundleData(it))
        },
        onChangeAction = {
            onEvent(StartIntentBuilderEvent.OnChangeAction(it))
        }
    )
}


@Composable
fun CancelPendingIntentButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    PendingIntentBuilderButton(
        enabled = enabled,
        label = "Cancel Pending Intent",
        onClick = { onClick() },
        modifier = modifier
    )
}


@Composable
private fun PendingIntentBuilderButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.Cancel,
    enabled: Boolean = true,
) {
    Button(
        enabled = enabled,
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.primary,
            contentColor = colorScheme.onPrimary
        ),
        modifier = modifier
    ) {
        Text(text = label)
        Spacer(modifier = Modifier.width(10.dp))
        Icon(imageVector = icon, contentDescription = null)
    }
}

