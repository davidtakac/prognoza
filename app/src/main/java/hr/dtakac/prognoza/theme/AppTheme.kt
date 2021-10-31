package hr.dtakac.prognoza.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val precipitationColor = PrecipitationColor(
        significant = if (darkTheme) Blue300 else Blue700,
        insignificant = if (darkTheme) White60 else Black60
    )

    val textColor = TextColor(
        highEmphasis = if (darkTheme) White87 else Black87,
        mediumEmphasis = if (darkTheme) White60 else White87
    )

    CompositionLocalProvider(
        LocalPrecipitationColor provides precipitationColor,
        LocalTextColor provides textColor
    ) {
        MaterialTheme(
            colors = if (darkTheme) DarkColors else LightColors,
            content = content
        )
    }
}

object AppTheme {
    val colors: Colors
        @Composable
        get() = MaterialTheme.colors

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    val precipitationColor: PrecipitationColor
        @Composable
        get() = LocalPrecipitationColor.current

    val textColor: TextColor
        @Composable
        get() = LocalTextColor.current
}