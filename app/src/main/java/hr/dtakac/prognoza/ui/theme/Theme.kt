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
    val contentProminent: TextStyle,
    val contentNormal: TextStyle,
    val contentSmall: TextStyle
)

val LocalPrognozaColors = staticCompositionLocalOf {
    PrognozaColors(
        background = Color.Unspecified,
        onBackground = Color.Unspecified
    )
}

val LocalPrognozaTypography = staticCompositionLocalOf {
    PrognozaTypography(
        contentProminent = TextStyle.Default,
        contentNormal = TextStyle.Default,
        contentSmall = TextStyle.Default
    )
}

@Composable
fun PrognozaTheme(
    currentTemperature: Temperature,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    var colors = PrognozaColors.forTemperature(currentTemperature)
    if (useDarkTheme) {
        colors = PrognozaColors(
            background = colors.background
                .copy(alpha = 0.12f)
                .compositeOver(background_dark),
            onBackground = white
        )
    }

    val typography = PrognozaTypography(
        contentProminent = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            letterSpacing = (-0.25).sp,
        ),
        contentNormal = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            letterSpacing = (-0.25).sp,
        ),
        contentSmall = TextStyle(
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