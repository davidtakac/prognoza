package hr.dtakac.prognoza.ui.theme

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import hr.dtakac.prognoza.shared.entity.Mood
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
    mood: Mood? = null,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val alpha = ContentAlpha.get()
    val colors = if (mood == null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getByDynamicColor(
                context = LocalContext.current,
                useDarkColors = useDarkTheme
            )
        } else {
            getByMood(
                mood = Mood.DEFAULT,
                useDarkColors = useDarkTheme
            )
        }
    } else {
        getByMood(
            mood = mood,
            useDarkColors = useDarkTheme
        )
    }.animate()
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

fun getByMood(
    mood: Mood,
    useDarkColors: Boolean
): ColorScheme = when (mood) {
    Mood.CLEAR_DAY -> if (useDarkColors) YellowColorSchemeDark else YellowColorSchemeLight
    Mood.CLEAR_NIGHT -> if (useDarkColors) DeepPurpleColorSchemeDark else DeepPurpleColorSchemeLight
    Mood.RAIN -> if (useDarkColors) BlueColorSchemeDark else BlueColorSchemeLight
    Mood.SLEET -> if (useDarkColors) TealColorSchemeDark else TealColorSchemeLight
    Mood.SNOW -> if (useDarkColors) CyanColorSchemeDark else CyanColorSchemeLight

    Mood.CLOUDY,
    Mood.FOG,
    Mood.DEFAULT -> if (useDarkColors) GreenColorSchemeDark else GreenColorSchemeLight
}

@RequiresApi(api = Build.VERSION_CODES.S)
fun getByDynamicColor(
    context: Context,
    useDarkColors: Boolean
): ColorScheme {
    return if (useDarkColors) dynamicDarkColorScheme(context)
    else dynamicLightColorScheme(context)
}