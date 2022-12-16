package hr.dtakac.prognoza.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.*

class ForecastWidget : GlanceAppWidget() {
    companion object {
        // https://developer.android.com/develop/ui/views/appwidgets/layouts#anatomy_determining_size
        val tiny = DpSize(width = 57.dp, height = 102.dp) // 1x1
        val small = DpSize(width = 130.dp, height = 102.dp) // 2x1
        val normal = DpSize(width = 130.dp, height = 220.dp) // 2x2
        val normalWide = DpSize(width = 203.dp, height = 220.dp) // 3x2
        val normalExtraWide = DpSize(width = 276.dp, height = 220.dp) // 4x2
        val normalChunky = DpSize(width = 349.dp, height = 220.dp) // 5x2
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(tiny, small, normal, normalWide, normalExtraWide, normalChunky)
    )

    override val stateDefinition = ForecastWidgetStateDefinition

    @Composable
    override fun Content() {
        ForecastWidgetContent()
    }
}