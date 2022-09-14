package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import hr.dtakac.prognoza.entities.forecast.ShortForecastDescription

private val mustard = Color(0xFFFFDD4A)
private val queenBlue = Color(0xFF3C6997)
private val snow = Color(0xFFFFF9FB)
private val lightGray = Color(0xFFCED4DA)
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
                ShortForecastDescription.CLEAR -> PrognozaColors(mustard, Color.Black)
                ShortForecastDescription.RAIN -> PrognozaColors(queenBlue, Color.White)
                ShortForecastDescription.SNOW -> PrognozaColors(snow, Color.Black)
                ShortForecastDescription.SLEET -> PrognozaColors(snow, Color.Black)
                ShortForecastDescription.CLOUDY -> PrognozaColors(lightGray, Color.Black)
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