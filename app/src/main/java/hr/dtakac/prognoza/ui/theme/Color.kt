package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import hr.dtakac.prognoza.entities.forecast.ForecastDescription

private val oldGold = Color(0xFFDCBA31)
private val oldGoldLight = Color(0xFFe4c95e)

private val stPatricksBlue = Color(0xFF123285)
private val stPatricksBlueLight = Color(0xFF173fa6)

private val gray200 = Color(0xFFEEEEEE)
private val gray200Light = Color.White

private val blueGray700 = Color(0xFF455A64)
private val blueGray700Light = Color(0xFF526a75)

private val cyan900 = Color(0xFF006064)
private val cyan900Light = Color(0xFF007b80)

private val almostBlack = Color(0xFF121212)
private val almostAlmostBlack = Color(0xFF262626)

@Immutable
data class PrognozaColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color
) {
    companion object {
        fun get(
            description: ForecastDescription.Short,
            useDarkTheme: Boolean
        ): PrognozaColors =
            if (useDarkTheme) {
                PrognozaColors(
                    primary = almostBlack,
                    onPrimary = Color.White,
                    secondary = almostAlmostBlack,
                    onSecondary = Color.White,
                )
            } else when (description) {
                ForecastDescription.Short.CLEAR -> PrognozaColors(
                    primary = oldGold,
                    onPrimary = Color.Black,
                    secondary = oldGoldLight,
                    onSecondary = Color.Black,
                )
                ForecastDescription.Short.RAIN -> PrognozaColors(
                    primary = stPatricksBlue,
                    onPrimary = Color.White,
                    secondary = stPatricksBlueLight,
                    onSecondary = Color.White
                )
                ForecastDescription.Short.SNOW -> PrognozaColors(
                    primary = gray200,
                    onPrimary = Color.Black,
                    secondary = gray200Light,
                    onSecondary = Color.Black
                )
                ForecastDescription.Short.SLEET -> PrognozaColors(
                    primary = cyan900,
                    onPrimary = Color.White,
                    secondary = cyan900Light,
                    onSecondary = Color.White
                )
                ForecastDescription.Short.CLOUDY -> PrognozaColors(
                    primary = blueGray700,
                    onPrimary = Color.White,
                    secondary = blueGray700Light,
                    onSecondary = Color.White
                )
            }
    }
}
