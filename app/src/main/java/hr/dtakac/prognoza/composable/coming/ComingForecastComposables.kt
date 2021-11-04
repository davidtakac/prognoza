package hr.dtakac.prognoza.composable.coming

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.composable.common.DaySummaryTime
import hr.dtakac.prognoza.composable.common.RepresentativeWeatherDescription
import hr.dtakac.prognoza.composable.common.TotalPrecipitation
import hr.dtakac.prognoza.model.ui.MeasurementUnit
import hr.dtakac.prognoza.model.ui.RepresentativeWeatherDescription
import hr.dtakac.prognoza.model.ui.cell.DayUiModel
import hr.dtakac.prognoza.theme.AppTheme
import hr.dtakac.prognoza.viewmodel.ComingForecastViewModel
import java.time.ZonedDateTime

@Composable
fun ComingForecast(viewModel: ComingForecastViewModel) {

    val forecast by viewModel.forecast.observeAsState()
    val outdatedForecast by viewModel.outdatedForecastMessage.observeAsState()
    val expandedHourIndices = viewModel.expandedHourIndices

    LazyColumn(
        modifier = Modifier.fillMaxHeight()
    ) {
        forecast?.let { forecast ->
            itemsIndexed(forecast.days) { index, day ->
                ExpandableDay(
                    isExpanded = index in expandedHourIndices,
                    dayUiModel = day,
                    onClick = { viewModel.toggleExpanded(index) }
                )
            }
        }
    }
}

@Composable
fun ExpandableDay(
    isExpanded: Boolean,
    dayUiModel: DayUiModel,
    onClick: () -> Unit
) {
    Surface(
        shape = AppTheme.shapes.medium,
        color = AppTheme.colors.surface,
        contentColor = AppTheme.colors.onSurface,
        elevation = 2.dp,
        modifier = Modifier
            .animateContentSize()
            .clickable { onClick.invoke() }
            .padding(16.dp)
    ) {
        ExpandableDaySummary(
            time = dayUiModel.time,
            representativeWeatherDescription = dayUiModel.representativeWeatherDescription,
            totalPrecipitation = dayUiModel.totalPrecipitationAmount,
            lowestTemperature = dayUiModel.lowTemperature,
            highestTemperature = dayUiModel.highTemperature,
            unit = dayUiModel.displayDataInUnit
        )
    }
}

@Composable
fun ExpandableDaySummary(
    time: ZonedDateTime,
    representativeWeatherDescription: RepresentativeWeatherDescription?,
    totalPrecipitation: Double?,
    lowestTemperature: Double?,
    highestTemperature: Double?,
    unit: MeasurementUnit
) {
    Row(modifier = Modifier.height(IntrinsicSize.Max)) {
        Column(horizontalAlignment = Alignment.Start) {
            DaySummaryTime(time = time)
            TotalPrecipitation(totalPrecipitation = totalPrecipitation, unit = unit)
            RepresentativeWeatherDescription(representativeWeatherDescription = representativeWeatherDescription)
        }

    }
}