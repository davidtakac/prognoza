package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
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

@Immutable
data class PrognozaContentAlpha(
    val high: Float,
    val medium: Float,
    val disabled: Float
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

val LocalContentAlpha = staticCompositionLocalOf {
    PrognozaContentAlpha(
        high = 1f,
        medium = 1f,
        disabled = 1f
    )
}

@Composable
fun PrognozaTheme(
    currentTemperature: Temperature,
    content: @Composable() () -> Unit
) {
    val colors = PrognozaColors.forTemperature(currentTemperature)

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

    val alpha = PrognozaContentAlpha(
        high = 0.87f,
        medium = 0.6f,
        disabled = 0.38f
    )

    CompositionLocalProvider(
        LocalPrognozaColors provides colors,
        LocalPrognozaTypography provides typography,
        LocalContentAlpha provides alpha,
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
        get() = LocalContentAlpha.current
}