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
    val prominentLarge: TextStyle,
    val prominentSmall: TextStyle,
    val normalLarge: TextStyle,
    val normalSmall: TextStyle
) {
    companion object {
        fun get(): PrognozaTypography = PrognozaTypography(
            prominentLarge = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                letterSpacing = (-0.25).sp,
            ),
            prominentSmall = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = -(0.25).sp
            ),
            normalLarge = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                letterSpacing = (-0.25).sp,
            ),
            normalSmall = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            )
        )
    }
}