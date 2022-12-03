package hr.dtakac.prognoza.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import hr.dtakac.prognoza.shared.entity.Mood

private val LocalColors = staticCompositionLocalOf {
    Colors(
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

private val LocalTypography = staticCompositionLocalOf {
    Typography(
        currentTemperature = TextStyle.Default,
        headlineSmall = TextStyle.Default,
        titleLarge = TextStyle.Default,
        titleMedium = TextStyle.Default,
        subtitleLarge = TextStyle.Default,
        subtitleMedium = TextStyle.Default,
        body = TextStyle.Default,
        titleSmall = TextStyle.Default,
        bodySmall = TextStyle.Default
    )
}

private val LocalContentAlpha = staticCompositionLocalOf {
    ContentAlpha(
        high = 1f,
        medium = 1f,
        disabled = 1f
    )
}

private val LocalWeatherIcons = staticCompositionLocalOf {
    WeatherIcons.get(useDarkTheme = false)
}

private class RippleTheme(
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
fun AppTheme(
    mood: Mood = Mood.DEFAULT,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val alpha = ContentAlpha.get()
    val colors = Colors.get(
        mood = mood,
        darkColors = useDarkTheme,
        contentAlpha = alpha.high
    ).switch()
    val typography = Typography.get()
    val weatherIcons = WeatherIcons.get(useDarkTheme)
    val indication = rememberRipple()
    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        LocalContentAlpha provides alpha,
        LocalRippleTheme provides RippleTheme(useDarkTheme),
        LocalWeatherIcons provides weatherIcons,
        LocalIndication provides indication,
        content = content
    )
}

object PrognozaTheme {
    val colors: Colors
        @Composable
        get() = LocalColors.current

    val typography: Typography
        @Composable
        get() = LocalTypography.current

    val alpha: ContentAlpha
        @Composable
        get() = LocalContentAlpha.current

    val weatherIcons: WeatherIcons
        @Composable
        get() = LocalWeatherIcons.current
}

@Composable
private fun animateColor(target: Color) = animateColorAsState(
    targetValue = target,
    animationSpec = tween(durationMillis = 1000)
).value

@Composable
private fun Colors.switch() = copy(
    surface1 = animateColor(target = surface1),
    surface2 = animateColor(target = surface2),
    surface3 = animateColor(target = surface3),
    onSurface = animateColor(target = onSurface),
    inverseSurface1 = animateColor(target = inverseSurface1),
    onInverseSurface = animateColor(target = onInverseSurface),
    primary = animateColor(target = primary),
    inversePrimary = animateColor(target = inversePrimary)
)