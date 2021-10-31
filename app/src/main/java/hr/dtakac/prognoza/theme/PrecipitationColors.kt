package hr.dtakac.prognoza.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PrecipitationColors(
    val significant: Color = Blue700,
    val insignificant: Color = Black60
)

internal val LocalPrecipitationColors = staticCompositionLocalOf { PrecipitationColors() }