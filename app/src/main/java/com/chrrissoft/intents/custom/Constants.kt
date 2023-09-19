package com.chrrissoft.intents.custom

import com.chrrissoft.intents.MainActivity
import com.chrrissoft.intents.MainReceiver
import com.chrrissoft.intents.MainService

object Constants {
    const val COMPANION_APP_CUSTOM_ACTION_ONE = "com.chrrissoft.intentscompanion.ACTION_ONE"
    const val COMPANION_APP_CUSTOM_ACTION_TWO = "com.chrrissoft.intentscompanion.ACTION_TWO"

    const val COMPANION_APP_CUSTOM_ACTION_BUILD_SEND = "com.chrrissoft.intentscompanion.CUSTOM_ACTION_BUILD_SEND"

    const val CUSTOM_ACTION_ONE = "com.chrrissoft.intents.ACTION_ONE"
    const val CUSTOM_ACTION_TWO = "com.chrrissoft.intents.ACTION_TWO"

    const val CUSTOM_SEND_ACTION_ONE = "com.chrrissoft.intents.CUSTOM_SEND_ACTION_ONE"
    const val CUSTOM_SEND_ACTION_TWO = "com.chrrissoft.intents.CUSTOM_SEND_ACTION_TWO"

    const val APP_COMPANION_ACTIVITY = "com.chrrissoft.intentscompanion.MainActivity"
    const val APP_COMPANION_SERVICE = "com.chrrissoft.intentscompanion.MainService"
    const val APP_COMPANION_RECEIVER = "com.chrrissoft.intentscompanion.MainReceiver"
    const val APP_COMPANION_PACKAGE = "com.chrrissoft.intentscompanion"

    val APP_ACTIVITY: String = MainActivity::class.java.name
    val APP_SERVICE: String = MainService::class.java.name
    val APP_RECEIVER: String = MainReceiver::class.java.name
    const val APP_PACKAGE = "com.chrrissoft.intents"

    const val PENDING_INTENT_EXTRA_KEY = "PENDING_INTENT_EXTRA_KEY"
}
