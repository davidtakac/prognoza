package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import hr.dtakac.prognoza.entities.forecast.ShortForecastDescription

private val oldGold = Color(0xFFDCBA31)
private val stPatricksBlue = Color(0xFF123285)
private val blueGray200 = Color(0xFFB0BEC5)
private val gray50 = Color(0xFFFAFAFA)
private val backgroundDark = Color(0xFF121212)

@Immutable
data class PrognozaColors(
    val background: Color,
    val onBackground: Color
) {
    companion object {
        fun get(
            description: ShortForecastDescription,
            useDarkTheme: Boolean
        ): PrognozaColors {
            val lightColors = when (description) {
                ShortForecastDescription.CLEAR -> PrognozaColors(oldGold, Color.Black)
                ShortForecastDescription.RAIN -> PrognozaColors(stPatricksBlue, Color.White)
                ShortForecastDescription.SNOW -> PrognozaColors(gray50, Color.Black)
                ShortForecastDescription.SLEET -> PrognozaColors(gray50, Color.Black)
                ShortForecastDescription.CLOUDY -> PrognozaColors(blueGray200, Color.Black)
            }

            return if (useDarkTheme) {
                PrognozaColors(
                    background = if (description != ShortForecastDescription.CLEAR) {
                        lightColors.background.copy(alpha = 0.12f).compositeOver(backgroundDark)
                    } else backgroundDark,
                    onBackground = Color.White
                )
            } else lightColors
        }
    }
}