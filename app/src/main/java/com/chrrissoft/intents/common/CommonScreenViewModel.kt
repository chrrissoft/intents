package com.chrrissoft.intents.common

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.MATCH_ALL
import android.content.pm.PackageManager.ResolveInfoFlags.of
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrrissoft.intents.common.ui.CommonScreenEvent
import com.chrrissoft.intents.common.ui.CommonScreenEvent.OnChangePage
import com.chrrissoft.intents.common.ui.CommonScreenEvent.ResolveEvent.OnChangeAction
import com.chrrissoft.intents.common.ui.CommonScreenState
import com.chrrissoft.intents.common.ui.CommonScreenState.Page
import com.chrrissoft.intents.common.ui.CommonScreenState.ResolvePage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonScreenViewModel @Inject constructor(
    private val pm: PackageManager
) : ViewModel() {
    private val _state = MutableStateFlow(CommonScreenState())
    val stateFlow = _state.asStateFlow()
    private val state get() = stateFlow.value
    private val _page get() = state.page
    private val _resolve get() = state.resolve

    private val intent = Intent()
    private val handler = EventHandler()

    fun onEvent(event: CommonScreenEvent) {
        event.resolveHandler(handler)
    }

    inner class EventHandler {
        val resolveHandler = ResolveEventHandler()

        fun onEvent(event: OnChangePage) {
            updateState(page = event.page)
        }
    }

    inner class ResolveEventHandler {
        @SuppressLint("QueryPermissionsNeeded")
        fun onEvent(event: OnChangeAction) {
            viewModelScope.launch(Default) {
                intent.action = event.action
                val best = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.resolveActivity(intent, of(MATCH_ALL.toLong()))
                } else {
                    pm.resolveActivity(intent, 0)
                }

                val bests = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.queryIntentActivities(intent, of(MATCH_ALL.toLong()))
                } else {
                    pm.queryIntentActivities(intent, 0)
                }

                val icon = best?.loadIcon(pm)
                val label = best?.loadLabel(pm)?.takeLastWhile { it.toString()!="." }
                val map = bests.associate { info ->
                    info.loadIcon(pm) to info.loadLabel(pm).takeLastWhile { it.toString()!="." }
                }
                val resolveCopy = _resolve
                    .copy(bestIcon = icon, bestLabel = label, resolved = map, action = event.action)
                updateState(resolve = resolveCopy)
            }

        }
    }

    private fun updateState(
        page: Page = _page,
        resolve: ResolvePage = _resolve,
    ) {
        _state.update {
            it.copy(page = page, resolve = resolve)
        }
    }
}
