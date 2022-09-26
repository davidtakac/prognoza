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
            defaultAlpha: Float,
            description: ForecastDescription.Short,
            useDarkTheme: Boolean
        ): PrognozaColors = PrognozaColors(
            surface = if (useDarkTheme) {
                Color(0xFF121212)
            } else {
                Color.White
            }.copy(alpha = defaultAlpha),

            onSurface = if (useDarkTheme) {
                Color.White
            } else {
                Color.Black
            }.copy(alpha = defaultAlpha),

            weatherDependentOverlay = when (description) {
                ForecastDescription.Short.CLOUDY -> Color(0xFF546E7A).copy(alpha = defaultAlpha) // Blue Gray 600
                ForecastDescription.Short.RAIN -> Color(0xFF1E88E5).copy(alpha = defaultAlpha) // Blue 600
                ForecastDescription.Short.SLEET -> Color(0xFF00897B).copy(alpha = defaultAlpha) // Teal 600

                ForecastDescription.Short.SNOW -> if (useDarkTheme) {
                    Color.White
                } else {
                    Color(0xFF757575) // Gray 600
                }.copy(alpha = defaultAlpha)

                ForecastDescription.Short.CLEAR -> if (useDarkTheme) {
                    Color(0xFF3949AB) // Indigo 600
                } else {
                    Color(0xFFFFB300) // Amber 600
                }.copy(alpha = defaultAlpha)
            }
        )
    }
}

@Composable
fun Color.applyOverlay(overlayColor: Color, overlayAlpha: Float = 0.12f): Color {
    return overlayColor.copy(alpha = overlayAlpha).compositeOver(this)
}