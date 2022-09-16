package hr.dtakac.prognoza.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import hr.dtakac.prognoza.entities.forecast.ForecastDescription

val LocalPrognozaColors = staticCompositionLocalOf {
    PrognozaColors(
        background = Color.Unspecified,
        onBackground = Color.Unspecified
    )
}

val LocalPrognozaTypography = staticCompositionLocalOf {
    PrognozaTypography(
        jumbo = TextStyle.Default,
        prominentLarge = TextStyle.Default,
        prominentSmall = TextStyle.Default,
        normalLarge = TextStyle.Default,
        normalSmall = TextStyle.Default
    )
}

@Composable
fun PrognozaTheme(
    description: ForecastDescription.Short,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = PrognozaColors.get(description, useDarkTheme)
    val typography = PrognozaTypography.get()
    CompositionLocalProvider(
        LocalPrognozaColors provides colors,
        LocalPrognozaTypography provides typography,
        content = content
    )
}

object PrognozaTheme {
    val colors: PrognozaColors
        @Composable
        get() = LocalPrognozaColors.current

    val typography: PrognozaTypography
        @Composable
        get() = LocalPrognozaTypography.current
}