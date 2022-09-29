package hr.dtakac.prognoza.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.ui.theme.cloudy.CloudyDarkColors
import hr.dtakac.prognoza.ui.theme.cloudy.CloudyLightColors
import hr.dtakac.prognoza.ui.theme.fair.FairDarkColors
import hr.dtakac.prognoza.ui.theme.fair.FairLightColors
import hr.dtakac.prognoza.ui.theme.rain.RainDarkColors
import hr.dtakac.prognoza.ui.theme.rain.RainLightColors
import hr.dtakac.prognoza.ui.theme.sleet.SleetDarkColors
import hr.dtakac.prognoza.ui.theme.sleet.SleetLightColors
import hr.dtakac.prognoza.ui.theme.snow.SnowDarkColors
import hr.dtakac.prognoza.ui.theme.snow.SnowLightColors

@Composable
fun MaterialPrognozaTheme(
    description: ForecastDescription.Short,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when (description) {
        ForecastDescription.Short.FAIR -> if (useDarkTheme) FairDarkColors else FairLightColors
        ForecastDescription.Short.SLEET -> if (useDarkTheme) SleetDarkColors else SleetLightColors
        ForecastDescription.Short.CLOUDY,
        ForecastDescription.Short.FOG -> if (useDarkTheme) CloudyDarkColors else CloudyLightColors
        ForecastDescription.Short.RAIN -> if (useDarkTheme) RainDarkColors else RainLightColors
        ForecastDescription.Short.SNOW,
        ForecastDescription.Short.UNKNOWN -> if (useDarkTheme) SnowDarkColors else SnowLightColors
    }.animate()

    MaterialTheme(
        colorScheme = colors,
        typography = PrognozaMaterialTypography,
        content = content
    )
}

// Great answer on this approach: https://stackoverflow.com/a/70945631
@Composable
private fun ColorScheme.animate() = copy(
    primary = animateColor(target = primary),
    onPrimary = animateColor(target = onPrimary),
    primaryContainer = animateColor(target = primaryContainer),
    onPrimaryContainer = animateColor(target = onPrimaryContainer),
    inversePrimary = animateColor(target = inversePrimary),
    secondary = animateColor(target = secondary),
    onSecondary = animateColor(target = onSecondary),
    secondaryContainer = animateColor(target = secondaryContainer),
    onSecondaryContainer = animateColor(target = onSecondaryContainer),
    tertiary = animateColor(target = tertiary),
    onTertiary = animateColor(target = onTertiary),
    tertiaryContainer = animateColor(target = tertiaryContainer),
    onTertiaryContainer = animateColor(target = onTertiaryContainer),
    background = animateColor(target = background),
    onBackground = animateColor(target = onBackground),
    surface = animateColor(target = surface),
    onSurface = animateColor(target = onSurface),
    surfaceVariant = animateColor(target = surfaceVariant),
    onSurfaceVariant = animateColor(target = onSurfaceVariant),
    surfaceTint = animateColor(target = surfaceTint),
    inverseSurface = animateColor(target = inverseSurface),
    inverseOnSurface = animateColor(target = inverseOnSurface),
    error = animateColor(target = error),
    onError = animateColor(target = onError),
    errorContainer = animateColor(target = errorContainer),
    onErrorContainer = animateColor(target = onErrorContainer),
    outline = animateColor(target = outline),
    outlineVariant = animateColor(target = outlineVariant),
    scrim = animateColor(target = scrim)
)

@Composable
private fun animateColor(target: Color) = animateColorAsState(
    targetValue = target,
    animationSpec = colorAnimationSpec
).value

private val colorAnimationSpec: AnimationSpec<Color> = tween(durationMillis = 1000)