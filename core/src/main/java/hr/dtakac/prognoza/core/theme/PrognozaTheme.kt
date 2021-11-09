package hr.dtakac.prognoza.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun PrognozaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val precipitationColors = PrecipitationColors(
        significant = if (darkTheme) Blue300 else Blue700,
        insignificant = if (darkTheme) White60 else Black60
    )

    val textColors = TextColors(
        highEmphasis = if (darkTheme) White87 else Black87,
        mediumEmphasis = if (darkTheme) White60 else Black60
    )

    val shapes = AppShapes

    CompositionLocalProvider(
        LocalPrecipitationColors provides precipitationColors,
        LocalTextColors provides textColors,
        LocalShapes provides shapes
    ) {
        MaterialTheme(
            colors = if (darkTheme) DarkColors else LightColors,
            content = content
        )
    }
}

object PrognozaTheme {
    val colors: Colors
        @Composable
        get() = MaterialTheme.colors

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = LocalShapes.current

    val precipitationColors: PrecipitationColors
        @Composable
        get() = LocalPrecipitationColors.current

    val textColors: TextColors
        @Composable
        get() = LocalTextColors.current
}