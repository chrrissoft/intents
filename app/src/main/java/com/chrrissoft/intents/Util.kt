package com.chrrissoft.intents

import android.R.drawable.sym_def_app_icon
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.chrrissoft.intents.Util.toUiAction
import java.util.ArrayList
import kotlin.random.Random

object Util {
    private fun componentStartedNotification(ctx: Context, title: String): Notification {
        return NotificationCompat.Builder((ctx), IntentsApplication.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(sym_def_app_icon)
            .build()
    }

    fun NotificationManager.notifyComponentStarted(ctx: Context, title: String) {
        notify(Random.nextInt(), componentStartedNotification(ctx, title))
    }

    fun Intent.getBundleData(): List<Pair<String, String>> {
        val map = mutableMapOf<String, String>()
        getStringArrayListExtra(BUNDLE_DATA_KEY_LIST)?.forEach { key ->
            val value = getStringExtra(key) ?: ""
            map[key] = value
        }
        return map.toList()
    }

    fun Intent.setBundleDataKeyList(keys: List<String>): Intent {
        val array = ArrayList<String>().apply {
            keys.forEach { this.add(it) }
        }
        putStringArrayListExtra(BUNDLE_DATA_KEY_LIST, array)
        return this
    }

    fun String.toUiAction(): String {
        return takeLastWhile { it.toString()!="." }
    }

    private const val BUNDLE_DATA_KEY_LIST = "bundle_data_key_list"

}
