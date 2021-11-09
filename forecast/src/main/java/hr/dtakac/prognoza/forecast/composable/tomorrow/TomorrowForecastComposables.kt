package hr.dtakac.prognoza.forecast.composable.tomorrow

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.dtakac.prognoza.core.formatting.formatTemperatureValue
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.composable.common.*
import hr.dtakac.prognoza.forecast.model.DayUiModel
import hr.dtakac.prognoza.forecast.model.EmptyForecastUiModel
import hr.dtakac.prognoza.forecast.model.OutdatedForecastUiModel
import hr.dtakac.prognoza.forecast.model.TomorrowForecastUiModel

@Composable
fun TomorrowForecast(
    forecast: TomorrowForecastUiModel?,
    outdatedForecast: OutdatedForecastUiModel?,
    isLoading: Boolean,
    emptyForecast: EmptyForecastUiModel?,
    expandedHourIndices: List<Int>,
    onHourClicked: (Int) -> Unit,
    onTryAgainClicked: () -> Unit,
    preferredMeasurementUnit: MeasurementUnit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (forecast != null) {
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                item {
                    TomorrowSummaryHeader(
                        dayUiModel = forecast.summary,
                        outdatedForecastUiModel = outdatedForecast,
                        preferredMeasurementUnit = preferredMeasurementUnit
                    )
                }
                itemsIndexed(forecast.hours) { index, hour ->
                    ExpandableHour(
                        hour = hour,
                        isExpanded = index in expandedHourIndices,
                        onClick = { onHourClicked(index) },
                        preferredMeasurementUnit = preferredMeasurementUnit
                    )
                    if (index == forecast.hours.lastIndex) {
                        MetNorwayOrganizationCredit()
                    } else {
                        Divider()
                    }
                }
            }
        }
        if (isLoading) {
            CircularProgressIndicator()
        }
        if (emptyForecast != null) {
            EmptyForecast(
                reason = emptyForecast.reasonResourceId?.let { stringResource(id = it) }
                    ?: stringResource(id = R.string.error_generic),
                onTryAgainClicked = onTryAgainClicked,
                isLoading = isLoading
            )
        }
    }
}

@Composable
fun TomorrowSummaryHeader(
    dayUiModel: DayUiModel,
    outdatedForecastUiModel: OutdatedForecastUiModel?,
    preferredMeasurementUnit: MeasurementUnit
) {
    Surface(
        shape = PrognozaTheme.shapes.medium,
        color = PrognozaTheme.colors.surface,
        contentColor = PrognozaTheme.colors.onSurface,
        elevation = 2.dp,
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                DaySummaryTime(time = dayUiModel.time)
                TomorrowLowestAndHighestTemperature(
                    lowestTemperature = dayUiModel.lowTemperature,
                    highestTemperature = dayUiModel.highTemperature,
                    unit = preferredMeasurementUnit
                )
                TotalPrecipitation(
                    totalPrecipitation = dayUiModel.totalPrecipitationAmount,
                    unit = preferredMeasurementUnit
                )
                RepresentativeWeatherDescription(
                    representativeWeatherDescription = dayUiModel.representativeWeatherDescription
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = dayUiModel.representativeWeatherDescription?.weatherDescription?.iconResourceId
                            ?: R.drawable.ic_cloud_off
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(size = 86.dp)
                )
                if (outdatedForecastUiModel != null) {
                    var showDialog by remember { mutableStateOf(false) }
                    OutdatedForecastMessage(
                        outdatedForecastUiModel = outdatedForecastUiModel,
                        showDialog = showDialog,
                        modifier = Modifier.clickable { showDialog = true },
                        onDialogConfirm = { showDialog = false },
                        onDialogDismiss = { showDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun TomorrowLowestAndHighestTemperature(
    lowestTemperature: Double?,
    highestTemperature: Double?,
    unit: MeasurementUnit
) {
    Row {
        Text(
            text = formatTemperatureValue(
                temperature = highestTemperature,
                unit = unit
            ),
            style = PrognozaTheme.typography.h4,
            color = PrognozaTheme.textColors.highEmphasis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = formatTemperatureValue(
                temperature = lowestTemperature,
                unit = unit
            ),
            style = PrognozaTheme.typography.h4,
            color = PrognozaTheme.textColors.mediumEmphasis
        )
    }
}