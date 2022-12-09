package hr.dtakac.prognoza.ui.theme

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import hr.dtakac.prognoza.shared.entity.Mood

@Immutable
data class Colors(
    val surface1: Color,
    val surface2: Color,
    val surface3: Color,
    val onSurface: Color,
    val inverseSurface1: Color,
    val onInverseSurface: Color
) {
    companion object {
        fun getByMood(
            mood: Mood,
            useDarkColors: Boolean,
            contentAlpha: Float
        ): Colors = get(
            primary = getPrimary(useDarkColors, mood),
            inversePrimary = getPrimary(!useDarkColors, mood),
            useDarkColors = useDarkColors,
            contentAlpha = contentAlpha
        )

        @RequiresApi(api = Build.VERSION_CODES.S)
        fun getByDynamicColor(
            context: Context,
            useDarkColors: Boolean,
            contentAlpha: Float
        ): Colors {
            val dynamicColorScheme = if (useDarkColors) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
            return get(
                primary = dynamicColorScheme.primary,
                inversePrimary = dynamicColorScheme.inversePrimary,
                useDarkColors = useDarkColors,
                contentAlpha = contentAlpha
            )
        }

        private fun get(
            primary: Color,
            inversePrimary: Color,
            useDarkColors: Boolean,
            contentAlpha: Float
        ): Colors {
            val onSurface = if (useDarkColors) Color.White else Color.Black
            val surface = getSurface(useDarkColors)
            val onInverseSurface = if (useDarkColors) Color.Black else Color.White
            val inverseSurface = getSurface(!useDarkColors)

            return Colors(
                surface1 = surface.overlay(with = primary, alpha = surface1Alpha),
                surface2 = surface.overlay(with = primary, alpha = surface2Alpha),
                surface3 = surface.overlay(with = primary, alpha = surface3Alpha),
                onSurface = onSurface.copy(alpha = contentAlpha),
                inverseSurface1 = inverseSurface.overlay(with = inversePrimary, alpha = surface1Alpha),
                onInverseSurface = onInverseSurface.copy(alpha = contentAlpha)
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

private fun getSurface(useDarkColors: Boolean): Color =
    if (useDarkColors) Color(0xFF121212) else Color.White

private fun getPrimary(
    useDarkColors: Boolean,
    mood: Mood
): Color = when (mood) {
    Mood.FOG,
    Mood.CLOUDY ->
        if (useDarkColors) Color(0xFF90A4AE) // Blue Gray 300
        else Color(0xFF546E7A) // Blue Gray 600

    Mood.RAIN ->
        if (useDarkColors) Color(0xFF64B5F6) // Blue 300
        else Color(0xFF1E88E5) // Blue 600

    Mood.SLEET ->
        if (useDarkColors) Color(0xFF4DB6AC) // Teal 300
        else Color(0xFF00897B) // Teal 600

    Mood.DEFAULT,
    Mood.SNOW ->
        if (useDarkColors) Color(0xFFE0E0E0) // Gray 300
        else Color(0xFF757575) // Gray 600

    Mood.CLEAR_NIGHT ->
        if (useDarkColors) Color(0xFF9575CD) // Deep Purple 300
        else Color(0xFF5E35B1) // Deep Purple 600

    Mood.CLEAR_DAY ->
        if (useDarkColors) Color(0xFFFFD54F) // Amber 300
        else Color(0xFFFFB300) // Amber 600
}

private const val surface1Alpha = 0.06f
private const val surface2Alpha = 0.12f
private const val surface3Alpha = 0.16f