package com.chrrissoft.intents.custom.ui

import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.chrrissoft.intents.Util.setBundleDataKeyList
import com.chrrissoft.intents.custom.Constants
import com.chrrissoft.intents.custom.Constants.APP_COMPANION_PACKAGE
import com.chrrissoft.intents.custom.Constants.APP_PACKAGE
import com.chrrissoft.intents.custom.Constants.COMPANION_APP_CUSTOM_ACTION_ONE
import com.chrrissoft.intents.custom.Constants.COMPANION_APP_CUSTOM_ACTION_TWO
import com.chrrissoft.intents.custom.Constants.CUSTOM_ACTION_ONE
import com.chrrissoft.intents.custom.Constants.CUSTOM_ACTION_TWO
import com.chrrissoft.intents.custom.Constants.CUSTOM_SEND_ACTION_ONE
import com.chrrissoft.intents.custom.Constants.CUSTOM_SEND_ACTION_TWO
import com.chrrissoft.intents.custom.Constants.PENDING_INTENT_EXTRA_KEY
import com.chrrissoft.intents.custom.ui.CustomScreenState.IntentBuilder.Component
import com.chrrissoft.intents.custom.ui.CustomScreenState.IntentBuilder.Component.Companion.components
import com.chrrissoft.intents.custom.ui.CustomScreenState.IntentBuilder.Component.Companion.componentsCompanion

data class CustomScreenState(
    val page: Page = Page.ROUND_TRIP,
    val send: SendPageState = SendPageState(),
    val replay: ReplayPageState = ReplayPageState(),
    val roundTrip: RoundTripPageState = RoundTripPageState(),
    val resolve: ResolvePageState = ResolvePageState(),
) {
    enum class Page(
        val icon: ImageVector,
        val label: String,
        val floatingActionButtonIcon: ImageVector,
        val floatingActionButtonLabel: String,
    ) {
        SEND(
            icon = Icons.Rounded.Send,
            label = "Send",
            floatingActionButtonIcon = Icons.Rounded.Favorite,
            floatingActionButtonLabel = "Start companion component",
        ),
        REPLAY(
            icon = Icons.Rounded.Replay,
            label = "Replay",
            floatingActionButtonIcon = Icons.Rounded.Favorite,
            floatingActionButtonLabel = "Build component to companion"
        ),
        ROUND_TRIP(
            icon = Icons.Rounded.Sync,
            label = "Round trip",
            floatingActionButtonIcon = Icons.Rounded.Favorite,
            floatingActionButtonLabel = "start round trip"
        );

        companion object {
            val pages = listOf(SEND, REPLAY, ROUND_TRIP)
        }
    }

    data class SendPageState(
        val builder: IntentBuilder = IntentBuilder(
            action = CUSTOM_ACTION_ONE,
            component = componentsCompanion[0]
        ),
    )

    data class ReplayPageState(val intent: Intent? = null)

    data class RoundTripPageState(
        val startIntent: IntentBuilder = IntentBuilder(action = CUSTOM_ACTION_ONE, component = componentsCompanion.first()),
        val nestedIntentBuilder: IntentBuilder = IntentBuilder(action = COMPANION_APP_CUSTOM_ACTION_ONE, component = components.first()),
        val pendingIntentBuilder: PendingIntentBuilder = PendingIntentBuilder(),
    ) {
        data class PendingIntentBuilder(
            val pendingIntent: PendingIntent? = null,
            val mutability: Boolean = true,
            val flags: List<Triple<Int, String, Boolean>> = Flags.PendingIntentFlags.flags,
        ) {
            fun build(
                ctx: Context,
                intent: Intent,
                component: Component,
            ): PendingIntent {
                val selectedFlags = flags.filter { it.third }
                var mutabilityFlag =
                    if (SDK_INT >= S && mutability) FLAG_MUTABLE else FLAG_IMMUTABLE
                for (index in 1 until selectedFlags.size) {
                    mutabilityFlag = selectedFlags[index].first or mutabilityFlag
                }

                return when (component) {
                    is Component.Activity -> getActivity(ctx, (0), intent, mutabilityFlag)
                    is Component.Receiver -> getBroadcast(ctx, (0), intent, mutabilityFlag)
                    is Component.Service -> getService(ctx, (0), intent, mutabilityFlag)
                }
            }
        }
    }

    data class ResolvePageState(val data: String = "")

    data class IntentBuilder(
        val action: String,
        val component: Component,
        val bundleData: Map<String, String> = mapOf(),
    ) {

        sealed interface Component {
            val label: String
            val icon: ImageVector
            val cls: String
            val `package`: String

            fun startComponent(ctx: Context, intent: Intent) {
                when (this) {
                    is Activity -> ctx.startActivity(intent)
                    is Receiver -> ctx.sendBroadcast(intent)
                    is Service -> ctx.startService(intent)
                }
            }

            data class Activity(
                override val cls: String,
                override val `package`: String,
                override val label: String = "Activity",
                override val icon: ImageVector = Icons.Rounded.Favorite,
            ) : Component

            data class Service(
                override val cls: String,
                override val `package`: String,
                override val label: String = "Service",
                override val icon: ImageVector = Icons.Rounded.Favorite,
            ) : Component

            data class Receiver(
                override val cls: String,
                override val `package`: String,
                override val label: String = "Receiver",
                override val icon: ImageVector = Icons.Rounded.Favorite,
            ) : Component

            companion object {
                val components = listOf(
                    Activity(Constants.APP_ACTIVITY, APP_PACKAGE),
                    Service(Constants.APP_SERVICE, APP_PACKAGE),
                    Receiver(Constants.APP_RECEIVER, APP_PACKAGE),
                )

                val componentsCompanion = listOf(
                    Activity(Constants.APP_COMPANION_ACTIVITY, APP_COMPANION_PACKAGE),
                    Service(Constants.APP_COMPANION_SERVICE, APP_COMPANION_PACKAGE),
                    Receiver(Constants.APP_COMPANION_RECEIVER, APP_COMPANION_PACKAGE),
                )
            }
        }

        fun build(
            pendingIntent: PendingIntent? = null,
            nestedIntent: Intent? = null,
        ): Intent {
            val intent = Intent()
            bundleData.forEach { (key, value) ->
                intent.putExtra(key, value)
            }
            intent.action = action
            intent.setBundleDataKeyList(bundleData.map { it.key })
            intent.component = ComponentName(component.`package`, component.cls)
            if (pendingIntent != null && nestedIntent != null) {
                println("----------------------------------------------------")
                intent.putExtra(PENDING_INTENT_EXTRA_KEY, pendingIntent)
            }
            return intent
        }

        fun startComponent(ctx: Context, intent: Intent) {
            intent.addFlags(FLAG_ACTIVITY_NEW_DOCUMENT)
            component.startComponent(ctx, intent)
        }
    }

    object Flags {
        object PendingIntentFlags {
            private val noCreate = Triple(
                FLAG_NO_CREATE,
                "FLAG_NO_CREATE",
                false
            )

            private val oneShot = Triple(
                FLAG_ONE_SHOT,
                "FLAG_ONE_SHOT",
                false
            )

            private val cancelCurrent = Triple(
                FLAG_CANCEL_CURRENT,
                "FLAG_CANCEL_CURRENT",
                false
            )

            private val updateCurrent = Triple(
                first = FLAG_UPDATE_CURRENT,
                second = "FLAG_UPDATE_CURRENT",
                third = false
            )

            val flags = buildList {
                add(noCreate)
                add(oneShot)
                add(cancelCurrent)
                add(updateCurrent)
            }
        }
    }

    object Actions {
        val actions = listOf(CUSTOM_ACTION_ONE, CUSTOM_ACTION_TWO)
        val sendActions = listOf(CUSTOM_SEND_ACTION_ONE, CUSTOM_SEND_ACTION_TWO)
        val companionActions = listOf(COMPANION_APP_CUSTOM_ACTION_ONE, COMPANION_APP_CUSTOM_ACTION_TWO)
    }
}
