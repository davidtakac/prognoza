package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import hr.dtakac.prognoza.R

val Manrope = FontFamily(
    Font(R.font.manrope_extrabold, FontWeight.ExtraBold),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_light, FontWeight.Light),
    Font(R.font.manrope_extralight, FontWeight.ExtraLight),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_semibold, FontWeight.SemiBold)
)

@Immutable
data class PrognozaTypography(
    val headlineLarge: TextStyle,
    val headlineSmall: TextStyle,
    val titleLarge: TextStyle,
    val subtitleLarge: TextStyle,
    val titleSmall: TextStyle,
    val subtitleSmall: TextStyle,
    val bodySmall: TextStyle
) {
    companion object {
        fun get(): PrognozaTypography = PrognozaTypography(
            headlineLarge = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 200.sp,
                letterSpacing = (-9).sp
            ),
            headlineSmall = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 42.sp,
                letterSpacing = 0.sp
            ),
            titleLarge = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                letterSpacing = 0.sp,
            ),
            subtitleLarge = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                letterSpacing = 0.sp
            ),
            titleSmall = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = 0.25.sp
            ),
            subtitleSmall = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                letterSpacing = 0.25.sp
            ),
            bodySmall = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                letterSpacing = 0.25.sp
            )
        )
    }
}