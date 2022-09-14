package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import hr.dtakac.prognoza.entities.forecast.units.Temperature

val white = Color.White
val black = Color.Black

@Immutable
data class PrognozaColors(
    val background: Color,
    val onBackground: Color
) {
    companion object {
        fun forTemperature(temperature: Temperature): PrognozaColors {
            val celsius = temperature.celsius
            // https://hslpicker.com/
            return when {
                celsius < -25.0 -> PrognozaColors(
                    background = Color(0xFF82b9c9),
                    onBackground = black
                )
                celsius < -20.0 -> PrognozaColors(
                    background = Color(0xFF79bed2),
                    onBackground = black
                )
                celsius < -15.0 -> PrognozaColors(
                    background = Color(0xFF70c2db),
                    onBackground = black
                )
                celsius < -10.0 -> PrognozaColors(
                    background = Color(0xFF67c7e4),
                    onBackground = black
                )
                celsius < -5.0 -> PrognozaColors(
                    background = Color(0xFF5ecced),
                    onBackground = black
                )
                celsius < 0.0 -> PrognozaColors(
                    background = Color(0xFF4dd5ff),
                    onBackground = black
                )
                celsius < 5.0 -> PrognozaColors(
                    background = Color(0xFF4dedff),
                    onBackground = black
                )
                celsius < 10.0 -> PrognozaColors(
                    background = Color(0xFF4dffe4),
                    onBackground = black
                )
                celsius < 15.0 -> PrognozaColors(
                    background = Color(0xFF4dffb8),
                    onBackground = black
                )
                celsius < 20.0 -> PrognozaColors(
                    background = Color(0xFF4dff7c),
                    onBackground = black
                )
                celsius < 25.0 -> PrognozaColors(
                    background = Color(0xFF70ff4d),
                    onBackground = black
                )
                celsius < 30.0 -> PrognozaColors(
                    background = Color(0xFFc9ff4d),
                    onBackground = black
                )
                celsius < 35.0 -> PrognozaColors(
                    background = Color(0xFFffa64d),
                    onBackground = black
                )
                celsius < 40.0 -> PrognozaColors(
                    background = Color(0xFFff884d),
                    onBackground = black
                )
                else -> PrognozaColors(
                    background = Color(0xFFff4d4d),
                    onBackground = black
                )
            }
        }
    }
}