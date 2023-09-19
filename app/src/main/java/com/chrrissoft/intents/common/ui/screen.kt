package com.chrrissoft.intents.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import com.chrrissoft.intents.MainActivity
import com.chrrissoft.intents.common.ui.CommonScreenEvent.OnChangePage
import com.chrrissoft.intents.common.ui.CommonScreenState.Page.BUILD
import com.chrrissoft.intents.common.ui.CommonScreenState.Page.Companion.pages
import com.chrrissoft.intents.common.ui.CommonScreenState.Page.RESOLVE
import com.chrrissoft.intents.ui.components.PageContainer
import com.chrrissoft.intents.ui.theme.centerAlignedTopAppBarColors
import com.chrrissoft.intents.ui.theme.navigationBarItemColors
import com.chrrissoft.intents.ui.theme.navigationDrawerItemColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonScreen(
    state: CommonScreenState,
    onEvent: (CommonScreenEvent) -> Unit,
    onOpenDrawer: () -> Unit,
) {
    MainActivity.setBarsColors()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = centerAlignedTopAppBarColors,
                navigationIcon = {
                    IconButton(onClick = { onOpenDrawer() }) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = null
                        )
                    }
                },
                title = { Text(text = "Common intents screen", fontWeight = Medium) }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = colorScheme.primaryContainer
            ) {
                pages.forEach {
                    NavigationBarItem(
                        selected = state.page==it,
                        onClick = { onEvent(OnChangePage(it)) },
                        label = { Text(text = it.label) },
                        icon = { Icon(imageVector = it.icon, contentDescription = null) },
                        colors = navigationBarItemColors
                    )
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(colorScheme.onPrimary)
            ) {
                when (state.page) {
                    BUILD -> {}
                    RESOLVE -> ResolvePage(state = state.resolve, onEvent = onEvent)
                }
            }
        }
    )
}
