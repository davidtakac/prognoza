package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val colorStops = arrayOf(
    0f to Color(0xFFC6D8F0),
    0.18f to Color(0xFF92B0D6),
    0.5f to Color(0xFF284571),
    0.63f to Color(0xFF266D88),
    0.66f to Color(0xFF32798B),
    0.72f to Color(0xFF7E9784),
    0.79f to Color(0xFFC1A76E),
    0.88f to Color(0xFFC0754E),
    0.93f to Color(0xFFA4374B),
    1f to Color(0xFF871F40)
)

@Composable
fun OverviewTemperatureBar(
    startPercent: Float,
    endPercent: Float,
    windowStartPercent: Float,
    windowEndPercent: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .height(4.dp)
            .width(120.dp)
            .background(
                Brush.horizontalGradient(
                    colorStops = colorStops/*.map { it.copy(first = it.first * 100) }.toTypedArray()*/,
                    startX = 100f,
                    endX = 350f
                )
            )
            .then(modifier)
    )
}

@Preview
@Composable
private fun OverviewTemperatureBarPreview() {
    OverviewTemperatureBar(
        startPercent = 50f,
        endPercent = 80f,
        windowStartPercent = 0f,
        windowEndPercent = 0f,
    )
}