package hr.dtakac.prognoza.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class TextColor(
    val highEmphasis: Color = Black87,
    val mediumEmphasis: Color = Black60
)

val LocalTextColor = staticCompositionLocalOf { TextColor() }