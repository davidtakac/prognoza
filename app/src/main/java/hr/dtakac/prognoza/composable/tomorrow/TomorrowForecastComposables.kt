package hr.dtakac.prognoza.composable.tomorrow

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import hr.dtakac.prognoza.composable.common.ExpandableHour
import hr.dtakac.prognoza.model.ui.cell.DayUiModel
import hr.dtakac.prognoza.model.ui.forecast.OutdatedForecastUiModel
import hr.dtakac.prognoza.viewmodel.TomorrowForecastViewModel

@Composable
fun TomorrowForecast(viewModel: TomorrowForecastViewModel) {

    val forecast by viewModel.forecast.observeAsState()
    val outdatedForecast by viewModel.outdatedForecastMessage.observeAsState()
    val expandedHourIndices = viewModel.expandedHourIndices

    LazyColumn(
        modifier = Modifier.fillMaxHeight()
    ) {
        forecast?.let { forecast ->
            item {
                SummaryHeader(
                    dayUiModel = forecast.summary,
                    outdatedForecastUiModel = outdatedForecast
                )
            }
            forecast.hours.forEachIndexed { index, hour ->
                item {
                    ExpandableHour(
                        hour = hour,
                        isExpanded = index in expandedHourIndices,
                        onClick = { viewModel.toggleHour(index) }
                    )
                }
                item {
                    Divider()
                }
            }
        }
    }
}

@Composable
fun SummaryHeader(
    dayUiModel: DayUiModel,
    outdatedForecastUiModel: OutdatedForecastUiModel?
) {

}