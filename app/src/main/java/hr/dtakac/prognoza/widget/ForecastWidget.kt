package hr.dtakac.prognoza.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.color.dynamicThemeColorProviders
import androidx.glance.layout.*
import hr.dtakac.prognoza.presentation.asGlanceString
import hr.dtakac.prognoza.presentation.forecast.*
import hr.dtakac.prognoza.ui.MainActivity
import java.lang.IllegalStateException

class ForecastWidget : GlanceAppWidget() {

    companion object {
        // https://developer.android.com/develop/ui/views/appwidgets/layouts#anatomy_determining_size
        private val tiny = DpSize(width = 57.dp, height = 102.dp) // 1x1
        private val small = DpSize(width = 130.dp, height = 102.dp) // 2x1
        private val normal = DpSize(width = 130.dp, height = 220.dp) // 2x2
        private val normalWide = DpSize(width = 203.dp, height = 220.dp) // 3x2
        private val normalExtraWide = DpSize(width = 276.dp, height = 220.dp) // 4x2
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(tiny, small, normal, normalWide, normalExtraWide)
    )

    override val stateDefinition = ForecastWidgetStateDefinition

    @Composable
    override fun Content() {
        val colors = dynamicThemeColorProviders()
        val state = currentState<ForecastWidgetState>()
        val size = LocalSize.current

        Box(
            modifier = GlanceModifier
                .appWidgetBackgroundRadius()
                .background(colors.surface)
                .appWidgetBackground()
                .padding(16.dp)
                .fillMaxSize()
                .clickable(actionStartActivity<MainActivity>()),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                ForecastWidgetState.Error -> EmptyWidget(colors)
                ForecastWidgetState.Loading -> LoadingWidget()
                ForecastWidgetState.Unavailable -> EmptyWidget(colors) // todo: make special widget for this
                is ForecastWidgetState.Success -> {
                    val placeName = state.placeName
                    val temperatureUnit = state.temperatureUnit
                    val icon = state.description.toDrawableId()
                    val currentTemperature = getTemperature(
                        temperature = state.temperature,
                        unit = temperatureUnit
                    ).asGlanceString()

                    when (size) {
                        tiny -> PlaceTempWidget(
                            placeName = placeName,
                            currentTemperature = currentTemperature,
                            colors = colors,
                        )
                        small -> PlaceTempIconWidget(
                            placeName = placeName,
                            currentTemperature = currentTemperature,
                            iconResId = icon,
                            colors = colors,
                        )
                        else -> PlaceTempIconHoursWidget(
                            placeName = placeName,
                            currentTemperature = currentTemperature,
                            iconResId = icon,
                            hours = state.hours.take(
                                when (size) {
                                    normal -> 3
                                    normalWide -> 5
                                    normalExtraWide -> 7
                                    else -> throw IllegalStateException("Unsupported widget size.")
                                }
                            ),
                            temperatureUnit = temperatureUnit,
                            colors = colors,
                        )
                    }
                }
            }
        }
    }
}