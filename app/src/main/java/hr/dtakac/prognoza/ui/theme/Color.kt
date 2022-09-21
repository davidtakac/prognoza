package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import hr.dtakac.prognoza.entities.forecast.ForecastDescription

private val oldGold = Color(0xFFDCBA31)
private val oldGoldLight = Color(0xFFe4c95e)

private val stPatricksBlue = Color(0xFF123285)
private val stPatricksBlueLight = Color(0xFF173fa6)

private val gray50 = Color(0xFFFAFAFA)
private val gray50Dark = Color(0xFFe6e6e6)

private val blueGray200 = Color(0xFFB0BEC5)
private val blueGray200Light = Color(0xFFc4cfd4)

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
                    primary = gray50,
                    onPrimary = Color.Black,
                    secondary = gray50Dark,
                    onSecondary = Color.Black
                )
                ForecastDescription.Short.SLEET -> PrognozaColors(
                    primary = cyan900,
                    onPrimary = Color.White,
                    secondary = cyan900Light,
                    onSecondary = Color.White
                )
                ForecastDescription.Short.CLOUDY -> PrognozaColors(
                    primary = blueGray200,
                    onPrimary = Color.Black,
                    secondary = blueGray200Light,
                    onSecondary = Color.Black
                )
            }
    }
}
