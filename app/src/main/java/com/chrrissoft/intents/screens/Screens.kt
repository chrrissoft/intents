package com.chrrissoft.intents.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screens(val route: String, val label: String, val icon: ImageVector) {
    COMMON_SCREEN("common_screen", "Common Intents", Icons.Rounded.TaskAlt),
    CUSTOM_SCREEN("custom_screen", "Custom Intents", Icons.Rounded.Tune);

    companion object {
        val screens = listOf(COMMON_SCREEN, CUSTOM_SCREEN)
    }
}
