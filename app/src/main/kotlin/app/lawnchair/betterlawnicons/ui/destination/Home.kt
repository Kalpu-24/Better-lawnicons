package app.lawnchair.betterlawnicons.ui.destination

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarExitDirection
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.lawnchair.betterlawnicons.model.IconInfo
import app.lawnchair.betterlawnicons.repository.preferenceManager
import app.lawnchair.betterlawnicons.ui.components.home.DebugMenu
import app.lawnchair.betterlawnicons.ui.components.home.HomeBottomToolbar
import app.lawnchair.betterlawnicons.ui.components.home.HomeTopBar
import app.lawnchair.betterlawnicons.ui.components.home.IconRequestFAB
import app.lawnchair.betterlawnicons.ui.components.home.NewIconsCard
import app.lawnchair.betterlawnicons.ui.components.home.PlaceholderUI
import app.lawnchair.betterlawnicons.ui.components.home.iconpreview.AppBarListItem
import app.lawnchair.betterlawnicons.ui.components.home.iconpreview.IconPreviewGrid
import app.lawnchair.betterlawnicons.ui.components.home.iconpreview.IconPreviewGridPadding
import app.lawnchair.betterlawnicons.ui.components.home.search.PlaceholderSearchBar
import app.lawnchair.betterlawnicons.ui.theme.LawniconsTheme
import app.lawnchair.betterlawnicons.ui.util.PreviewLawnicons
import app.lawnchair.betterlawnicons.viewmodel.DummyLawniconsViewModel
import app.lawnchair.betterlawnicons.viewmodel.LawniconsViewModel
import app.lawnchair.betterlawnicons.viewmodel.LawniconsViewModelImpl
import kotlinx.serialization.Serializable

@Serializable
data object Home

fun NavGraphBuilder.homeDestination(
    isExpandedScreen: Boolean,
    isIconPicker: Boolean,
    onNavigateToAbout: () -> Unit,
    onNavigateToNewIcons: () -> Unit,
    onSendResult: (IconInfo) -> Unit,
) {
    composable<Home> {
        Home(
            onNavigateToAbout = onNavigateToAbout,
            onNavigateToNewIcons = onNavigateToNewIcons,
            isExpandedScreen = isExpandedScreen,
            isIconPicker = isIconPicker,
            onSendResult = onSendResult,
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Home(
    onNavigateToAbout: () -> Unit,
    onNavigateToNewIcons: () -> Unit,
    onSendResult: (IconInfo) -> Unit,
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    isIconPicker: Boolean = false,
    lawniconsViewModel: LawniconsViewModel = hiltViewModel<LawniconsViewModelImpl>(),
) {
    with(lawniconsViewModel) {
        val iconInfoModel by iconInfoModel.collectAsStateWithLifecycle()
        val searchedIconInfoModel by searchedIconInfoModel.collectAsStateWithLifecycle()
        val iconRequestModel by iconRequestModel.collectAsStateWithLifecycle()
        val newIconsInfoModel by newIconsInfoModel.collectAsStateWithLifecycle()
        val context = LocalContext.current

        val lazyGridState = rememberLazyGridState()
        val snackbarHostState = remember { SnackbarHostState() }

        val prefs = preferenceManager(context)

        val scrollBehavior = FloatingToolbarDefaults.exitAlwaysScrollBehavior(
            FloatingToolbarExitDirection.Bottom,
        )

        Crossfade(
            modifier = modifier,
            targetState = iconInfoModel.iconCount > 0,
            label = "",
        ) { visible ->
            if (visible) {
                Scaffold(
                    topBar = {
                        HomeTopBar(
                            textFieldState = searchTermTextState,
                            mode = searchMode,
                            onBack = {
                                expandSearch = false
                            },
                            onNavigate = onNavigateToAbout,
                            onModeChange = ::changeMode,
                            expandSearch = expandSearch,
                            isExpandedScreen = isExpandedScreen,
                            isIconPicker = isIconPicker,
                            onSendResult = onSendResult,
                            iconInfoModel = searchedIconInfoModel,
                        )
                    },
                    floatingActionButton = {
                        if (isExpandedScreen) {
                            IconRequestFAB(
                                iconRequestsEnabled = iconRequestsEnabled,
                                iconRequestModel = iconRequestModel,
                                lazyGridState = lazyGridState,
                                snackbarHostState = snackbarHostState,
                            )
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                        ) {
                            val offset by animateDpAsState(
                                if (scrollBehavior.state.offset != 0F) 0.dp else FloatingToolbarDefaults.ContainerSize,
                            )
                            Snackbar(
                                it,
                                modifier = Modifier.padding(bottom = offset),
                            )
                        }
                    },
                    modifier = Modifier.nestedScroll(scrollBehavior),
                ) {
                    Box {
                        IconPreviewGrid(
                            iconInfo = iconInfoModel.iconInfo,
                            onSendResult = onSendResult,
                            contentPadding = if (isExpandedScreen) IconPreviewGridPadding.ExpandedSize else IconPreviewGridPadding.Defaults,
                            isIconPicker = isIconPicker,
                            gridState = lazyGridState,
                        ) {
                            if (!isExpandedScreen) {
                                item(
                                    span = { GridItemSpan(maxLineSpan) },
                                ) {
                                    AppBarListItem()
                                }
                            }
                            if (newIconsInfoModel.iconCount != 0) {
                                item(
                                    span = { GridItemSpan(maxLineSpan) },
                                ) {
                                    NewIconsCard(onNavigateToNewIcons)
                                }
                            }
                        }
                        if (!isExpandedScreen) {
                            HomeBottomToolbar(
                                context = context,
                                scrollBehavior = scrollBehavior,
                                iconRequestsEnabled = iconRequestsEnabled,
                                iconRequestModel = iconRequestModel,
                                snackbarHostState = snackbarHostState,
                                onNavigate = onNavigateToAbout,
                                onExpandSearch = { expandSearch = true },
                            )
                        }
                    }
                }
            } else {
                if (isExpandedScreen) {
                    PlaceholderSearchBar()
                } else {
                    PlaceholderUI()
                }
            }
        }

        LaunchedEffect(searchTermTextState.text) {
            searchIcons(searchTermTextState.text.toString())
        }

        LaunchedEffect(iconRequestsEnabled) {
            prefs.iconRequestsEnabled.set(iconRequestsEnabled)
        }

        if (prefs.showDebugMenu.asState().value) {
            DebugMenu(
                iconInfoModel,
                iconRequestModel,
                newIconsInfoModel,
            )
        }
    }
}

@PreviewLawnicons
@Composable
private fun HomePreview() {
    LawniconsTheme {
        Surface(Modifier.fillMaxSize()) {
            Home(
                onNavigateToAbout = {},
                onNavigateToNewIcons = {},
                isExpandedScreen = true,
                onSendResult = {},
                lawniconsViewModel = DummyLawniconsViewModel(),
            )
        }
    }
}
