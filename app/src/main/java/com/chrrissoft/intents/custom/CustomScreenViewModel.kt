package com.chrrissoft.intents.custom

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.chrrissoft.intents.custom.ui.CustomScreenEvent
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.*
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.ReplayPageEvent.OnChangeIntent
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.ReplayPageEvent.OnChangeRoundTripPage
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.RoundTripPageEvent.*
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.SendPageEvent.OnChangeIntentBuilder
import com.chrrissoft.intents.custom.ui.CustomScreenState
import com.chrrissoft.intents.custom.ui.CustomScreenState.IntentBuilder.Component
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CustomScreenViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(CustomScreenState())
    val stateFlow = _state.asStateFlow()

    private val state get() = stateFlow.value

    private val eventHandler = EventHandler()

    fun onEvent(event: CustomScreenEvent) {
        Intent().apply {
            this.component
        }
        eventHandler.onEvent(event)
    }

    private fun updateState(
        page: CustomScreenState.Page = state.page,
        send: CustomScreenState.SendPageState = state.send,
        replay: CustomScreenState.ReplayPageState = state.replay,
        roundTrip: CustomScreenState.RoundTripPageState = state.roundTrip,
        resolve: CustomScreenState.ResolvePageState = state.resolve,
    ) {
        _state.update {
            it.copy(
                page = page,
                send = send,
                replay = replay,
                roundTrip = roundTrip,
                resolve = resolve
            )
        }
    }

    private inner class EventHandler {
        private val sendEventHandler = SendPageEventHandler()
        private val replayEventHandler = ReplayPageEventHandler()
        private val roundTripEventHandler = RoundTripPageEventHandler()

        fun onEvent(event: CustomScreenEvent) {
            when (event) {
                is OnChangePage -> onEvent(event)
                is SendPageEvent -> { sendEventHandler.onEvent(event) }
                is ReplayPageEvent -> { replayEventHandler.onEvent(event) }
                is RoundTripPageEvent -> { roundTripEventHandler.onEvent(event) }
                is OnCreatePendingIntent -> {
                    val builder = state.roundTrip.pendingIntentBuilder.copy(pendingIntent = event.intent)
                    updateState(roundTrip = state.roundTrip.copy(pendingIntentBuilder = builder))
                }
            }
        }

        private fun onEvent(event: OnChangePage) {
            updateState(page = event.page)
        }
    }

    private inner class SendPageEventHandler {
        fun onEvent(event: SendPageEvent) {
            when (event) {
                is OnChangeIntentBuilder -> onEvent(event)
            }
        }

        fun onEvent(event: OnChangeIntentBuilder) {
            val sendCopy = state.send.copy(builder = event.builder)
            updateState(send = sendCopy)
        }
    }

    private inner class ReplayPageEventHandler {
        fun onEvent(event: ReplayPageEvent) {
            when (event) {
                is OnChangeRoundTripPage -> onEvent(event)
                is OnChangeIntent -> onEvent(event)
            }
        }

        private fun onEvent(event: OnChangeRoundTripPage) {
            updateState(page = event.page)
        }

        private fun onEvent(event: OnChangeIntent) {
            updateState(replay = state.replay.copy(intent = event.intent))
        }
    }

    private inner class RoundTripPageEventHandler {
        private val pageState get() = state.roundTrip
        private val nestedIntent get() = pageState.nestedIntentBuilder
        private val pendingIntent get() = pageState.pendingIntentBuilder
        private val startIntent get() = pageState.startIntent

        private fun updateNestedIntentState(
            action: String = nestedIntent.action,
            component: Component = nestedIntent.component,
            bundleData: Map<String, String> = nestedIntent.bundleData,
        ) {
            val state = nestedIntent.copy(
                action = action,
                component = component,
                bundleData = bundleData,
            )
            updateState(roundTrip = pageState.copy(nestedIntentBuilder = state))
        }

        private fun updatePendingIntentState(
            mutability: Boolean = pendingIntent.mutability,
            flags: List<Triple<Int, String, Boolean>> = pendingIntent.flags,
        ) {
            val state = pendingIntent.copy(
                flags = flags,
                mutability = mutability,
            )
            updateState(roundTrip = pageState.copy(pendingIntentBuilder = state))
        }

        private fun updateStartIntentState(
            action: String = startIntent.action,
            component: Component = startIntent.component,
            bundleData: Map<String, String> = startIntent.bundleData,
        ) {
            val state = startIntent.copy(
                action = action,
                component = component,
                bundleData = bundleData,
            )
            updateState(roundTrip = pageState.copy(startIntent = state))
        }

        fun onEvent(event: RoundTripPageEvent) {
            when (event) {
                is StartIntentBuilderEvent -> onEvent(event)
                is NestedIntentBuilderEvent -> onEvent(event)
                is PendingIntentEvent -> onEvent(event)
            }
        }

        private fun onEvent(event: NestedIntentBuilderEvent) {
            when (event) {
                is NestedIntentBuilderEvent.OnChangeComponent -> {
                    updateNestedIntentState(component = event.component)
                }
                is NestedIntentBuilderEvent.OnAddBundleData -> {
                    val new = nestedIntent.bundleData.plus(event.data)
                    updateNestedIntentState(bundleData = new)
                }
                is NestedIntentBuilderEvent.OnRemoveBundleData -> {
                    val new = nestedIntent.bundleData.minus(event.key)
                    updateNestedIntentState(bundleData = new)
                }
                is NestedIntentBuilderEvent.OnChangeAction -> {
                    updateNestedIntentState(action = event.action)
                }
            }
        }

        private fun onEvent(event: PendingIntentEvent) {
            when (event) {
                is PendingIntentEvent.OnChangeMutability -> {
                    updatePendingIntentState(mutability = event.mutability)
                }
                is PendingIntentEvent.OnCancelPendingIntent -> {
                    event.intent.cancel()
                }
                is PendingIntentEvent.OnToggleFlags -> {
                    val flags = pendingIntent.flags.map {
                        if (it.first == event.flag.first) event.flag else it
                    }
                    updatePendingIntentState(flags = flags)
                }
            }
        }

        private fun onEvent(event: StartIntentBuilderEvent) {
            when (event) {
                is StartIntentBuilderEvent.OnChangeComponent -> {
                    updateStartIntentState(component = event.component)
                }
                is StartIntentBuilderEvent.OnAddBundleData -> {
                    val new = startIntent.bundleData.plus(event.data)
                    updateStartIntentState(bundleData = new)
                }
                is StartIntentBuilderEvent.OnRemoveBundleData -> {
                    val new = startIntent.bundleData.minus(event.key)
                    updateStartIntentState(bundleData = new)
                }
                is StartIntentBuilderEvent.OnChangeAction -> {
                    updateStartIntentState(action = event.action)
                }
            }
        }
    }
}
