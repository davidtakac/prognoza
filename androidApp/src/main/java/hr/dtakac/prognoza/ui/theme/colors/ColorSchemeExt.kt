package hr.dtakac.prognoza.ui.theme.colors

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ColorScheme.animate() = copy(
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
    // Not used on Android, scrim is provided by system
    //scrim = animateColor(target = scrim)
)

@Composable
private fun animateColor(target: Color) = animateColorAsState(
    targetValue = target,
    animationSpec = tween(durationMillis = 700)
).value