package hr.dtakac.prognoza.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class PrognozaColors(
    val background: Color,
    val onBackground: Color
)

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
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = PrognozaColors(
        background = if (useDarkTheme) prognoza_theme_dark_primary else prognoza_theme_light_primary,
        onBackground = if (useDarkTheme) prognoza_theme_dark_onPrimary else prognoza_theme_light_onPrimary
    )

    val typography = PrognozaTypography(
        contentProminent = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Black,
            fontSize = 24.sp,
            letterSpacing = (-0.25).sp
        ),
        contentNormal = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        ),
        contentSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
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