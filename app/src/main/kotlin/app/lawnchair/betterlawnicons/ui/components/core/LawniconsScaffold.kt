package app.lawnchair.betterlawnicons.ui.components.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import app.lawnchair.betterlawnicons.ui.components.home.NavigationIconButton
import app.lawnchair.betterlawnicons.ui.theme.LawniconsTheme
import app.lawnchair.betterlawnicons.ui.theme.adaptiveSurfaceColor
import app.lawnchair.betterlawnicons.ui.util.PreviewLawnicons
import app.lawnchair.betterlawnicons.ui.util.toPaddingValues

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LawniconsScaffold(
    title: String,
    onBack: () -> Unit,
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    containerColor: Color = adaptiveSurfaceColor,
    content: @Composable (PaddingValues) -> Unit,
) {
    val scrollBehavior =
        if (isExpandedScreen) TopAppBarDefaults.pinnedScrollBehavior() else TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = title,
                navigationIcon = {
                    NavigationIconButton(
                        onClick = onBack,
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        size = 40.dp,
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                },
                isExpandedScreen = isExpandedScreen,
                containerColor = containerColor,
            )
        },
        containerColor = containerColor,
    ) {
        Box(
            modifier = Modifier.then(
                if (isExpandedScreen) {
                    Modifier.padding(
                        WindowInsets.navigationBars.toPaddingValues(
                            additionalStart = 32.dp,
                            additionalEnd = 32.dp,
                        ),
                    )
                } else {
                    Modifier
                },
            ),
        ) {
            content(it)
        }
    }
}

@PreviewLawnicons
@Composable
private fun LawniconsScaffoldPreview() {
    LawniconsTheme {
        LawniconsScaffold(
            title = "Example small bar",
            onBack = { },
            isExpandedScreen = false,
            content = {
                Box(
                    modifier = Modifier.padding(it),
                ) {
                    Text("Hello World")
                }
            },
        )
    }
}

@PreviewLawnicons
@Composable
private fun LawniconsScaffoldExpandedPreview() {
    LawniconsTheme {
        LawniconsScaffold(
            title = "Example small bar",
            onBack = { },
            isExpandedScreen = true,
            content = {
                Box(
                    modifier = Modifier.padding(it),
                ) {
                    Text("Hello World")
                }
            },
        )
    }
}
