package app.lawnchair.betterlawnicons.ui.components

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb

@Composable
fun ComponentActivity.SetupEdgeToEdge(
    scrimColor: Int,
) {
    val contentColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val isDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(Unit) {
        enableEdgeToEdge(
            navigationBarStyle = if (isDarkTheme) {
                SystemBarStyle.dark(scrimColor)
            } else {
                SystemBarStyle.light(
                    scrimColor,
                    contentColor,
                )
            },
        )
    }
}
