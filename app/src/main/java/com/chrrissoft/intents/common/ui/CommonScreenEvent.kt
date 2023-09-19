package com.chrrissoft.intents.common.ui

import com.chrrissoft.intents.common.CommonScreenViewModel.EventHandler
import com.chrrissoft.intents.common.CommonScreenViewModel.ResolveEventHandler
import com.chrrissoft.intents.common.ui.CommonScreenState.Page

sealed interface CommonScreenEvent {
    fun resolveHandler(handler: EventHandler) {
        when (this) {
            is OnChangePage -> handler.onEvent(event = this)
            is ResolveEvent.OnChangeAction -> resolve(handler.resolveHandler)
        }
    }

    data class OnChangePage(val page: Page) : CommonScreenEvent

    sealed interface ResolveEvent : CommonScreenEvent {
        fun resolve(handler: ResolveEventHandler) {
            when (this) {
                is OnChangeAction -> handler.onEvent(event = this)
            }
        }

        data class OnChangeAction(val action: String?) : ResolveEvent
    }
}
