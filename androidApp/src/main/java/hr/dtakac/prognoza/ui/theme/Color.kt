package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import hr.dtakac.prognoza.shared.entity.Mood

@Immutable
data class PrognozaColors(
    val surface1: Color,
    val surface2: Color,
    val surface3: Color,
    val onSurface: Color,
    val inverseSurface1: Color,
    val onInverseSurface: Color,
    val primary: Color,
    val inversePrimary: Color
) {
    companion object {
        fun get(
            mood: Mood,
            darkColors: Boolean,
            contentAlpha: Float
        ): PrognozaColors {
            val onSurface = if (darkColors) Color.White else Color.Black
            val surface = getSurface(darkColors)
            val onInverseSurface = if (darkColors) Color.Black else Color.White
            val inverseSurface = getSurface(!darkColors)
            val primary = getPrimary(darkColors, mood)
            val inversePrimary = getPrimary(!darkColors, mood)

            return PrognozaColors(
                surface1 = surface.overlay(with = primary, alpha = surface1Alpha),
                surface2 = surface.overlay(with = primary, alpha = surface2Alpha),
                surface3 = surface.overlay(with = primary, alpha = surface3Alpha),
                onSurface = onSurface.copy(alpha = contentAlpha),
                inverseSurface1 = inverseSurface.overlay(with = inversePrimary, alpha = surface1Alpha),
                onInverseSurface = onInverseSurface.copy(alpha = contentAlpha),
                primary = primary,
                inversePrimary = inversePrimary
            )
        }
    }
}

private fun Color.overlay(
    with: Color,
    alpha: Float = surface1Alpha
): Color {
    return with.copy(alpha = alpha).compositeOver(this)
}

private fun getSurface(darkColors: Boolean): Color =
    if (darkColors) Color(0xFF121212) else Color.White

private fun getPrimary(
    darkColors: Boolean,
    mood: Mood
): Color = when (mood) {
    Mood.FOG,
    Mood.CLOUDY ->
        if (darkColors) Color(0xFF90A4AE) // Blue Gray 300
        else Color(0xFF546E7A) // Blue Gray 600

    Mood.RAIN ->
        if (darkColors) Color(0xFF64B5F6) // Blue 300
        else Color(0xFF1E88E5) // Blue 600

    Mood.SLEET ->
        if (darkColors) Color(0xFF4DB6AC) // Teal 300
        else Color(0xFF00897B) // Teal 600

    Mood.DEFAULT,
    Mood.SNOW ->
        if (darkColors) Color.White
        else Color(0xFF757575) // Gray 600

    Mood.CLEAR ->
        if (darkColors) Color(0xFF9575CD) // Deep Purple 300
        else Color(0xFFFFB300) // Amber 600
}

private const val surface1Alpha = 0.06f
private const val surface2Alpha = 0.12f
private const val surface3Alpha = 0.16f