package app.lawnchair.betterlawnicons.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import app.lawnchair.betterlawnicons.ui.components.core.SimpleListRow
import app.lawnchair.betterlawnicons.ui.theme.LawniconsTheme
import app.lawnchair.betterlawnicons.ui.util.PreviewLawnicons
import app.lawnchair.betterlawnicons.ui.util.visitUrl

@Composable
fun ExternalLinkRow(
    name: String,
    url: String,
    modifier: Modifier = Modifier,
    divider: Boolean = true,
    background: Boolean = false,
    first: Boolean = false,
    last: Boolean = false,
    startIcon: @Composable (() -> Unit)? = null,
) {
    val context = LocalContext.current

    SimpleListRow(
        modifier = modifier,
        background = background,
        first = first,
        last = last,
        divider = divider,
        label = name,
        onClick = { context.visitUrl(url) },
        startIcon = startIcon,
    )
}

@PreviewLawnicons
@Composable
private fun ExternalLinkRowPreview() {
    LawniconsTheme {
        ExternalLinkRow(
            name = "User",
            url = "https://lawnchair.app/",
        )
    }
}
