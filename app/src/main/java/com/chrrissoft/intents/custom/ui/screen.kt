package com.chrrissoft.intents.custom.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.unit.dp
import com.chrrissoft.intents.MainActivity
import com.chrrissoft.intents.Util.getBundleData
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.OnChangePage
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.OnCreatePendingIntent
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.ReplayPageEvent.OnChangeRoundTripPage
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.RoundTripPageEvent.*
import com.chrrissoft.intents.custom.ui.CustomScreenState.Page
import com.chrrissoft.intents.custom.ui.CustomScreenState.Page.Companion.pages
import com.chrrissoft.intents.ui.components.*
import com.chrrissoft.intents.ui.theme.centerAlignedTopAppBarColors
import com.chrrissoft.intents.ui.theme.navigationBarItemColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScreen(
    state: CustomScreenState,
    onOpenDrawer: () -> Unit,
    onEvent: (CustomScreenEvent) -> Unit,
) {
    MainActivity.setBarsColors(bottom = colorScheme.primaryContainer)

    val ctx = LocalContext.current

    var showError by remember {
        mutableStateOf(false)
    }

    if (showError) {
        AlertDialog(
            onDismissRequest = { showError = false },
            confirmButton = {
                Button(
                    onClick = { showError = false },
                    colors = buttonColors(
                        containerColor = colorScheme.error,
                        contentColor = colorScheme.onError
                    )
                ) {
                    Text(text = "Ok")
                }
            },
            text = { Text(text = "App is not in background, please open and leave it in background") },
            title = { Text(text = "App is not in background") },
            containerColor = colorScheme.errorContainer,
            titleContentColor = colorScheme.onErrorContainer,
            textContentColor = colorScheme.onErrorContainer,
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Custom Intents", fontWeight = Medium) },
                colors = centerAlignedTopAppBarColors,
                navigationIcon = {
                    IconButton(onClick = { onOpenDrawer() }) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = colorScheme.primaryContainer,
                contentColor = colorScheme.primary,
            ) {
                pages.forEach {
                    NavigationBarItem(
                        selected = state.page==it,
                        onClick = { onEvent(OnChangePage(it)) },
                        icon = {
                            Icon(imageVector = it.icon, contentDescription = null)
                        },
                        label = { Text(text = it.label) },
                        colors = navigationBarItemColors,
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    when (state.page) {
                        Page.SEND -> {
                            val intent = state.send.builder.build()
                            state.send.builder.startComponent(ctx, intent)
                        }
                        Page.ROUND_TRIP -> {
                            val nestedIntent = state.roundTrip.nestedIntentBuilder.build()
                            val pendingIntent = state.roundTrip.nestedIntentBuilder.component.let {
                                state.roundTrip.pendingIntentBuilder.build(
                                    ctx = ctx,
                                    intent = nestedIntent,
                                    component = it
                                )
                            }
                            val startIntent =
                                state.roundTrip.startIntent.build(pendingIntent, nestedIntent)
                            onEvent(OnCreatePendingIntent(pendingIntent))

                            try {
                                state.roundTrip.startIntent.startComponent(ctx, startIntent)
                            } catch (e: Exception) {
                                showError = true
                            }
                        }
                        Page.REPLAY -> {
                            onEvent(OnChangeRoundTripPage())
                        }
                    }
                },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
            ) {
                Text(
                    text = state.page.floatingActionButtonLabel,
                    style = typography.titleMedium
                )
                Icon(
                    imageVector = state.page.floatingActionButtonIcon,
                    contentDescription = null
                )
            }
        },
        content = { padding ->
            PageContainer(padding) {
                when (state.page) {
                    Page.SEND -> {
                        SendPage(state.send, onEvent)
                    }
                    Page.REPLAY -> {
                        ReplayPage(state.replay, onEvent)
                    }
                    Page.ROUND_TRIP -> {
                        RoundTripPage(state.roundTrip, onEvent)
                    }
                }
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                Spacer(modifier = Modifier.height(screenHeight.div(other = 10)))
            }
        },
    )
}
