package mozilla.components.feature.prompts.identitycredential.previews

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DialogPreviewMaterialTheme(content: @Composable () -> Unit) {
    val colors = if (!isSystemInDarkTheme()) lightColors() else darkColors(background = Color(0xFF42414D))
    MaterialTheme(colors = colors) {
        content()
    }
}