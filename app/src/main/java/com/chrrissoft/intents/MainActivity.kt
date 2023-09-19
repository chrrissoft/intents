package com.chrrissoft.intents

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.chrrissoft.intents.common.CommonScreenViewModel
import com.chrrissoft.intents.custom.Constants
import com.chrrissoft.intents.custom.Constants.COMPANION_APP_CUSTOM_ACTION_BUILD_SEND
import com.chrrissoft.intents.custom.Constants.COMPANION_APP_CUSTOM_ACTION_ONE
import com.chrrissoft.intents.custom.Constants.COMPANION_APP_CUSTOM_ACTION_TWO
import com.chrrissoft.intents.custom.CustomScreenViewModel
import com.chrrissoft.intents.custom.ui.CustomScreenEvent
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.ReplayPageEvent.OnChangeIntent
import com.chrrissoft.intents.custom.ui.CustomScreenState
import com.chrrissoft.intents.custom.ui.CustomScreenState.Page.*
import com.chrrissoft.intents.screens.Graph
import com.chrrissoft.intents.screens.NavDestinationStateBased
import com.chrrissoft.intents.screens.NavDestinationStateBased.Custom
import com.chrrissoft.intents.screens.NavigationState
import com.chrrissoft.intents.screens.Screens
import com.chrrissoft.intents.ui.components.App
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val customScreenViewModel: CustomScreenViewModel by viewModels()
    private val commonScreenViewModel: CommonScreenViewModel by viewModels()

    private val navState = mutableStateOf(NavigationState())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App {
                val commonScreenState by commonScreenViewModel.stateFlow.collectAsState()
                val customScreenState by customScreenViewModel.stateFlow.collectAsState()
                Graph(
                    navState = navState.value,
                    commonScreenState = commonScreenState,
                    customScreenState = customScreenState,
                    onCommonScreenEvent = { commonScreenViewModel.onEvent(it) },
                    onCustomScreenEvent = { customScreenViewModel.onEvent(it) },
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (intent.action == COMPANION_APP_CUSTOM_ACTION_BUILD_SEND) {
            navState.value = NavigationState(Custom(SEND))
            customScreenViewModel.onEvent(OnChangeIntent(intent))
        }

        if (
            intent.action==COMPANION_APP_CUSTOM_ACTION_ONE
            || intent.action==COMPANION_APP_CUSTOM_ACTION_TWO
        ) {
            navState.value = NavigationState(Custom(ROUND_TRIP))
            customScreenViewModel.onEvent(OnChangeIntent(intent))
        }
    }

    companion object {
//        private const val TAG = "MainActivity"

        @SuppressLint("ComposableNaming")
        @Composable
        fun setBarsColors(
            status: Color = MaterialTheme.colorScheme.primaryContainer,
            bottom: Color = MaterialTheme.colorScheme.primaryContainer,
        ) {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

            DisposableEffect(systemUiController, useDarkIcons) {
                systemUiController.setStatusBarColor(status, useDarkIcons)
                systemUiController.setNavigationBarColor(bottom)
                onDispose {}
            }
        }
    }
}
