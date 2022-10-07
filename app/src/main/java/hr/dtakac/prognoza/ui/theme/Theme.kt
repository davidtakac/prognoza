package hr.dtakac.prognoza.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import hr.dtakac.prognoza.entities.forecast.ForecastDescription

private val LocalPrognozaColors = staticCompositionLocalOf {
    PrognozaColors(
        surface1 = Color.Unspecified,
        surface2 = Color.Unspecified,
        surface3 = Color.Unspecified,
        onSurface = Color.Unspecified,
        inverseSurface1 = Color.Unspecified,
        primary = Color.Unspecified,
        onInverseSurface = Color.Unspecified,
        inversePrimary = Color.Unspecified
    )
}

private val LocalPrognozaTypography = staticCompositionLocalOf {
    PrognozaTypography(
        headlineLarge = TextStyle.Default,
        headlineSmall = TextStyle.Default,
        titleLarge = TextStyle.Default,
        titleMedium = TextStyle.Default,
        subtitleLarge = TextStyle.Default,
        subtitleMedium = TextStyle.Default,
        body = TextStyle.Default,
        titleSmall = TextStyle.Default
    )
}

private val LocalPrognozaContentAlpha = staticCompositionLocalOf {
    PrognozaContentAlpha(
        high = 1f,
        medium = 1f,
        disabled = 1f
    )
}

private class PrognozaRippleTheme(
    private val useDarkTheme: Boolean
) : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = LocalContentColor.current,
        lightTheme = !useDarkTheme
    )

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        contentColor = LocalContentColor.current,
        lightTheme = !useDarkTheme
    )
}

@Composable
fun PrognozaTheme(
    description: ForecastDescription.Short = ForecastDescription.Short.UNKNOWN,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val alpha = PrognozaContentAlpha.get()
    val colors = PrognozaColors.get(
        shortForecastDescription = description,
        darkColors = useDarkTheme
    ).switch()
    val typography = PrognozaTypography.get()
    CompositionLocalProvider(
        LocalPrognozaColors provides colors,
        LocalPrognozaTypography provides typography,
        LocalPrognozaContentAlpha provides alpha,
        LocalRippleTheme provides PrognozaRippleTheme(useDarkTheme),
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

@Composable
private fun animateColor(target: Color) = animateColorAsState(
    targetValue = target,
    animationSpec = tween(durationMillis = 1000)
).value

@Composable
private fun PrognozaColors.switch() = copy(
    surface1 = animateColor(target = surface1),
    surface2 = animateColor(target = surface2),
    surface3 = animateColor(target = surface3),
    onSurface = animateColor(target = onSurface),
    inverseSurface1 = animateColor(target = inverseSurface1),
    onInverseSurface = animateColor(target = onInverseSurface),
    primary = animateColor(target = primary),
    inversePrimary = animateColor(target = inversePrimary)
)