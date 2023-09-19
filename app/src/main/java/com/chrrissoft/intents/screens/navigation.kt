package com.chrrissoft.intents.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue.Closed
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chrrissoft.intents.common.ui.CommonScreen
import com.chrrissoft.intents.common.ui.CommonScreenEvent
import com.chrrissoft.intents.common.ui.CommonScreenState
import com.chrrissoft.intents.custom.ui.CustomScreen
import com.chrrissoft.intents.custom.ui.CustomScreenEvent
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.OnChangePage
import com.chrrissoft.intents.custom.ui.CustomScreenState
import com.chrrissoft.intents.custom.ui.CustomScreenState.Page
import com.chrrissoft.intents.custom.ui.CustomScreenState.Page.*
import com.chrrissoft.intents.screens.NavDestinationStateBased.Common
import com.chrrissoft.intents.screens.NavDestinationStateBased.Custom
import com.chrrissoft.intents.screens.Screens.COMMON_SCREEN
import com.chrrissoft.intents.screens.Screens.CUSTOM_SCREEN
import com.chrrissoft.intents.screens.Screens.Companion.screens
import com.chrrissoft.intents.ui.theme.navigationDrawerItemColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class NavigationState(val nav: NavDestinationStateBased? = null)

sealed interface NavDestinationStateBased {
    object Common : NavDestinationStateBased
    data class Custom(val page: Page) : NavDestinationStateBased
}

@Composable
fun Graph(
    navState: NavigationState,
    commonScreenState: CommonScreenState,
    customScreenState: CustomScreenState,
    onCommonScreenEvent: (CommonScreenEvent) -> Unit,
    onCustomScreenEvent: (CustomScreenEvent) -> Unit,
) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStack.collectAsState()

    LaunchedEffect(navState) {
        if (navState.nav!=null) {
            navController.navigate(CUSTOM_SCREEN.route)
        }
    }


    val drawerController = rememberDrawerState(Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = colorScheme.primaryContainer,
            ) {
                NavigationDrawerIcon()

                screens.forEach {
                    val selected = backStack.lastOrNull()?.destination?.route==it.route
                    NavigationDrawerItem(
                        label = {
                            Text(text = it.label, fontWeight = if (selected) Medium else Normal)
                        },
                        selected = selected,
                        onClick = {
                            drawerController.close(scope)
                            navController.navigate(it.route)
                        },
                        colors = navigationDrawerItemColors,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .padding(horizontal = 10.dp)
                    )
                }
            }
        },
        drawerState = drawerController
    ) {
        LaunchedEffect(navState) {
            when (navState.nav) {
                Common -> navController.navigate(COMMON_SCREEN.route)
                is Custom -> navController.navigate(CUSTOM_SCREEN.route)
                null -> {}
            }
        }

        NavHost(navController = navController, CUSTOM_SCREEN.route) {
            composable(COMMON_SCREEN.route) {
                CommonScreen(
                    state = commonScreenState,
                    onEvent = onCommonScreenEvent,
                    onOpenDrawer = {
                        drawerController.open(scope)
                    }
                )
            }

            composable(CUSTOM_SCREEN.route) {
                LaunchedEffect(navState) {
                    if (navState.nav is Custom) {
                        onCustomScreenEvent(OnChangePage(navState.nav.page))
                    }
                }
                CustomScreen(
                    state = customScreenState,
                    onEvent = onCustomScreenEvent,
                    onOpenDrawer = {
                        drawerController.open(scope)
                    }
                )
            }
        }
    }
}

fun DrawerState.open(scope: CoroutineScope) {
    scope.launch { open() }
}

fun DrawerState.close(scope: CoroutineScope) {
    scope.launch { close() }
}
