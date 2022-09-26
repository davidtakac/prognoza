package hr.dtakac.prognoza.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import hr.dtakac.prognoza.entities.forecast.ForecastDescription

val LocalPrognozaColors = staticCompositionLocalOf {
    PrognozaColors(
        surface = Color.Unspecified,
        onSurface = Color.Unspecified,
        weatherDependentOverlay = Color.Unspecified
    )
}

val LocalPrognozaTypography = staticCompositionLocalOf {
    PrognozaTypography(
        headlineLarge = TextStyle.Default,
        headlineSmall = TextStyle.Default,
        titleLarge = TextStyle.Default,
        titleSmall = TextStyle.Default,
        subtitleLarge = TextStyle.Default,
        subtitleSmall = TextStyle.Default,
        body = TextStyle.Default,
        label = TextStyle.Default
    )
}

val LocalPrognozaContentAlpha = staticCompositionLocalOf {
    PrognozaContentAlpha(
        high = 1f,
        medium = 1f
    )
}

@Composable
fun PrognozaTheme(
    description: ForecastDescription.Short,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val alpha = PrognozaContentAlpha.get()
    val colors = PrognozaColors.get(
        description = description,
        useDarkTheme = useDarkTheme
    )
    val typography = PrognozaTypography.get()

    CompositionLocalProvider(
        LocalPrognozaColors provides colors,
        LocalPrognozaTypography provides typography,
        LocalPrognozaContentAlpha provides alpha,
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

    val alpha: PrognozaContentAlpha
        @Composable
        get() = LocalPrognozaContentAlpha.current
}

object PrognozaRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = LocalContentColor.current,
        lightTheme = !isSystemInDarkTheme()
    )

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        contentColor = LocalContentColor.current,
        lightTheme = !isSystemInDarkTheme()
    )
}