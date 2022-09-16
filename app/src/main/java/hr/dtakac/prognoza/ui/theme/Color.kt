package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import hr.dtakac.prognoza.entities.forecast.ForecastDescription

private val oldGold = Color(0xFFDCBA31)
private val stPatricksBlue = Color(0xFF123285)
private val blueGray200 = Color(0xFFB0BEC5)
private val gray50 = Color(0xFFFAFAFA)
private val cyan900 = Color(0xFF006064)
private val almostBlack = Color(0xFF121212)

@Immutable
data class PrognozaColors(
    val background: Color,
    val onBackground: Color
) {
    companion object {
        fun get(
            description: ForecastDescription.Short,
            useDarkTheme: Boolean
        ): PrognozaColors =
            if (useDarkTheme) {
                PrognozaColors(
                    background = almostBlack,
                    onBackground = Color.White
                )
            } else when (description) {
                ForecastDescription.Short.CLEAR -> PrognozaColors(oldGold, Color.Black)
                ForecastDescription.Short.RAIN -> PrognozaColors(stPatricksBlue, Color.White)
                ForecastDescription.Short.SNOW -> PrognozaColors(gray50, Color.Black)
                ForecastDescription.Short.SLEET -> PrognozaColors(cyan900, Color.White)
                ForecastDescription.Short.CLOUDY -> PrognozaColors(blueGray200, Color.Black)
            }
    }
}
