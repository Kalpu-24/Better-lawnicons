package app.lawnchair.betterlawnicons.ui.util

import androidx.annotation.DrawableRes

data class ExternalLink(
    @DrawableRes val iconResId: Int,
    val name: Int,
    val url: String,
)
