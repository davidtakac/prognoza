package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import hr.dtakac.prognoza.entities.forecast.ForecastDescription

@Immutable
data class PrognozaColors(
    val surface: Color,
    val onSurface: Color,
    val weatherDependentOverlay: Color
) {
    companion object {
        fun get(
            description: ForecastDescription.Short,
            useDarkTheme: Boolean
        ): PrognozaColors = PrognozaColors(
            surface = if (useDarkTheme) Color(0xFF121212) else Color.White,
            onSurface = if (useDarkTheme) Color.White else Color.Black,
            weatherDependentOverlay = when (description) {
                ForecastDescription.Short.CLOUDY -> Color(0xFF546E7A) // blue gray 600
                ForecastDescription.Short.RAIN -> Color(0xFF1E88E5) // blue 600
                ForecastDescription.Short.SNOW -> Color(0xFF757575) // gray 600
                ForecastDescription.Short.SLEET -> Color(0xFF00897B) // teal 600

                ForecastDescription.Short.CLEAR -> if (useDarkTheme) {
                    Color.Black // Clear night sky is pitch black
                } else
                    Color(0xFFFDD835) // yellow 600
            }
        )
    }
}

@Composable
fun Color.applyOverlay(overlayColor: Color, overlayAlpha: Float = 0.12f): Color {
    return overlayColor.copy(alpha = overlayAlpha).compositeOver(this)
}