package hr.dtakac.prognoza.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    small = RoundedCornerShape(percent = 50),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

internal val LocalShapes = staticCompositionLocalOf { AppShapes }