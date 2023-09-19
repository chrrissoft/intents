package com.chrrissoft.intents.common.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector


data class CommonScreenState(
    val page: Page = Page.BUILD,
    val resolve: ResolvePage = ResolvePage()
) {
    enum class Page(val label: String, val icon: ImageVector) {
        BUILD(label = "Build", icon = Rounded.Build),
        RESOLVE(label = "Resolve", icon = Rounded.Search);

        companion object {
            val pages = listOf(BUILD, RESOLVE)
        }
    }

    data class ResolvePage(
        val action: String? = null,
        val bestIcon: Drawable? = null,
        val bestLabel: CharSequence? = null,
        val resolved: Map<Drawable, CharSequence> = emptyMap(),
    ) {
        companion object {
            val actions = buildList {
                add(null)
                add(Intent.ACTION_SEND)
                add(Intent.ACTION_SEARCH)
                add(Intent.ACTION_CALL)
                add(Intent.ACTION_DIAL)
            }
        }
    }
}
