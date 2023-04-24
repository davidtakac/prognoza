package hr.dtakac.prognoza.ui.theme

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import hr.dtakac.prognoza.ui.theme.colors.*

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
        bodySmall = TextStyle.Default,
        label = TextStyle.Default
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

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val alpha = ContentAlpha.get()
    val colors = if (useDarkTheme) darkColorScheme() else lightColorScheme()
    val typography = Typography.get()
    val weatherIcons = WeatherIcons.get(useDarkTheme)
    val indication = rememberRipple()
    CompositionLocalProvider(
        LocalTypography provides typography,
        LocalContentAlpha provides alpha,
        LocalWeatherIcons provides weatherIcons,
        LocalIndication provides indication
    ) {
        MaterialTheme(
            colorScheme = colors,
            content = content
        )
    }
}

object PrognozaTheme {
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