package app.lawnchair.betterlawnicons.model

data class SearchInfo(
    val iconInfo: IconInfo,
    val indexOfMatch: Int,
    val matchAtWordStart: Boolean,
)

enum class SearchMode {
    LABEL,
    COMPONENT,
    DRAWABLE,
}
