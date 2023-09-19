package com.chrrissoft.intents.custom.ui

import android.app.PendingIntent
import android.content.Intent
import com.chrrissoft.intents.custom.ui.CustomScreenEvent.RoundTripPageEvent.NestedIntentBuilderEvent
import com.chrrissoft.intents.custom.ui.CustomScreenState.IntentBuilder
import com.chrrissoft.intents.custom.ui.CustomScreenState.IntentBuilder.Component
import com.chrrissoft.intents.custom.ui.CustomScreenState.Page


sealed interface CustomScreenEvent {
    data class OnChangePage(val page: Page) : CustomScreenEvent

    data class OnCreatePendingIntent(val intent: PendingIntent) : CustomScreenEvent

    sealed interface SendPageEvent : CustomScreenEvent {
        data class OnChangeIntentBuilder(val builder: IntentBuilder): SendPageEvent
    }

    sealed interface ReplayPageEvent : CustomScreenEvent {
        data class OnChangeRoundTripPage(val page: Page = Page.ROUND_TRIP) : ReplayPageEvent

        data class OnChangeIntent(val intent: Intent) : ReplayPageEvent
    }

    sealed interface RoundTripPageEvent : CustomScreenEvent {
        sealed interface NestedIntentBuilderEvent : RoundTripPageEvent {
            data class OnChangeComponent(val component: Component) : NestedIntentBuilderEvent

            data class OnAddBundleData(val data: Pair<String, String>) : NestedIntentBuilderEvent

            data class OnRemoveBundleData(val key: String) : NestedIntentBuilderEvent

            data class OnChangeAction(val action: String) : NestedIntentBuilderEvent
        }

        sealed interface PendingIntentEvent : RoundTripPageEvent {
            data class OnToggleFlags(val flag: Triple<Int, String, Boolean>) : PendingIntentEvent

            data class OnChangeMutability(val mutability: Boolean) : PendingIntentEvent

            data class OnCancelPendingIntent(val intent: PendingIntent) : PendingIntentEvent
        }

        sealed interface StartIntentBuilderEvent : RoundTripPageEvent {
            data class OnChangeComponent(val component: Component) : StartIntentBuilderEvent

            data class OnAddBundleData(val data: Pair<String, String>) : StartIntentBuilderEvent

            data class OnRemoveBundleData(val key: String) : StartIntentBuilderEvent

            data class OnChangeAction(val action: String) :StartIntentBuilderEvent
        }
    }
}
