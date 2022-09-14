package hr.dtakac.prognoza.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import hr.dtakac.prognoza.entities.forecast.units.Temperature

@Immutable
data class PrognozaTypography(
    val prominent: TextStyle,
    val normal: TextStyle,
    val small: TextStyle
)

val LocalPrognozaColors = staticCompositionLocalOf {
    PrognozaColors(
        background = Color.Unspecified,
        onBackground = Color.Unspecified
    )
}

val LocalPrognozaTypography = staticCompositionLocalOf {
    PrognozaTypography(
        prominent = TextStyle.Default,
        normal = TextStyle.Default,
        small = TextStyle.Default
    )
}

@Composable
fun PrognozaTheme(
    temperature: Temperature,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    var colors = PrognozaColors.forTemperature(temperature)
    if (useDarkTheme) {
        colors = PrognozaColors(
            // Simulates elevation overlay for dark themes
            background = colors.background
                .copy(alpha = 0.12f)
                .compositeOver(background_dark),
            onBackground = white
        )
    }

    val typography = PrognozaTypography(
        prominent = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            letterSpacing = (-0.25).sp,
        ),
        normal = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            letterSpacing = (-0.25).sp,
        ),
        small = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )
    )

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