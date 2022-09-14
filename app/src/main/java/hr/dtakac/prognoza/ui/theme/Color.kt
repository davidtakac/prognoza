package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import hr.dtakac.prognoza.entities.forecast.ShortForecastDescription

private val mustard = Color(0xFFFFDD4A)
private val yaleBlue = Color(0xFF084887)
private val blueGray100 = Color(0xFFCFD8DC)
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
                ShortForecastDescription.CLEAR -> PrognozaColors(mustard, Color.Black)
                ShortForecastDescription.RAIN -> PrognozaColors(yaleBlue, Color.White)
                ShortForecastDescription.SNOW -> PrognozaColors(gray50, Color.Black)
                ShortForecastDescription.SLEET -> PrognozaColors(gray50, Color.Black)
                ShortForecastDescription.CLOUDY -> PrognozaColors(blueGray100, Color.Black)
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