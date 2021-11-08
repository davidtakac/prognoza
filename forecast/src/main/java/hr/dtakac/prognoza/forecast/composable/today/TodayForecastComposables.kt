package hr.dtakac.prognoza.forecast.composable.today

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.dtakac.prognoza.core.formatting.ComposeStringFormatting
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.model.ui.WeatherDescription
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.core.utils.shouldShowPrecipitation
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.composable.common.EmptyForecast
import hr.dtakac.prognoza.forecast.composable.common.ExpandableHour
import hr.dtakac.prognoza.forecast.composable.common.MetNorwayOrganizationCredit
import hr.dtakac.prognoza.forecast.composable.common.OutdatedForecastMessage
import hr.dtakac.prognoza.forecast.model.HourUiModel
import hr.dtakac.prognoza.forecast.model.OutdatedForecastUiModel
import hr.dtakac.prognoza.forecast.viewmodel.TodayForecastViewModel
import java.time.ZonedDateTime

@Composable
fun TodayForecast(
    viewModel: TodayForecastViewModel,
    preferredMeasurementUnit: MeasurementUnit
) {

    val forecast by viewModel.forecast
    val outdatedForecast by viewModel.outdatedForecastMessage
    val expandedHourIndices = viewModel.expandedHourIndices
    val isLoading by viewModel.isLoading
    val empty by viewModel.emptyScreen

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            forecast?.let { forecast ->
                item {
                    CurrentHourHeader(
                        currentHour = forecast.currentHour,
                        outdatedForecastUiModel = outdatedForecast,
                        preferredMeasurementUnit = preferredMeasurementUnit
                    )
                }
                itemsIndexed(forecast.otherHours) { index, hour ->
                    ExpandableHour(
                        hour = hour,
                        isExpanded = index in expandedHourIndices,
                        onClick = { viewModel.toggleExpanded(index) },
                        preferredMeasurementUnit = preferredMeasurementUnit
                    )
                    if (index == forecast.otherHours.lastIndex) {
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
        if (empty != null) {
            EmptyForecast(
                reason = empty?.reasonResourceId?.let { stringResource(id = it) }
                    ?: stringResource(id = R.string.error_generic),
                onTryAgainClick = { viewModel.getForecast() },
                isLoading = isLoading
            )
        }
    }
}

@Composable
fun CurrentHourHeader(
    currentHour: HourUiModel,
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
                CurrentHourHeaderTime(time = currentHour.time)
                CurrentHourHeaderTemperature(
                    temperature = currentHour.temperature,
                    unit = preferredMeasurementUnit
                )
                CurrentHourHeaderDescription(
                    precipitation = currentHour.precipitationAmount,
                    weatherDescription = currentHour.weatherDescription,
                    unit = preferredMeasurementUnit
                )
                CurrentHourFeelsLikeTemperature(
                    feelsLike = currentHour.feelsLike,
                    unit = preferredMeasurementUnit
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = currentHour.weatherDescription?.iconResourceId
                            ?: R.drawable.ic_cloud_off
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(size = 86.dp)
                )
                OutdatedForecastMessage(outdatedForecastUiModel = outdatedForecastUiModel)
            }
        }
    }
}

@Composable
fun CurrentHourHeaderTime(time: ZonedDateTime) {
    Text(
        text = ComposeStringFormatting.formatCurrentHourHeaderTime(time = time),
        style = PrognozaTheme.typography.subtitle1,
        color = PrognozaTheme.textColors.highEmphasis
    )
}

@Composable
fun CurrentHourHeaderTemperature(
    temperature: Double?,
    unit: MeasurementUnit
) {
    Text(
        text = ComposeStringFormatting.formatTemperatureValue(
            temperature = temperature,
            unit = unit
        ),
        style = PrognozaTheme.typography.h3,
        color = PrognozaTheme.textColors.highEmphasis
    )
}

@Composable
fun CurrentHourHeaderDescription(
    precipitation: Double?,
    weatherDescription: WeatherDescription?,
    unit: MeasurementUnit
) {
    Text(
        text = if (shouldShowPrecipitation(precipitation)) {
            ComposeStringFormatting.formatPrecipitationValue(
                precipitation = precipitation,
                unit = unit
            )
        } else {
            AnnotatedString(
                text = ComposeStringFormatting.formatWeatherIconDescription(
                    id = weatherDescription?.descriptionResourceId
                )
            )
        },
        style = PrognozaTheme.typography.subtitle1,
        color = PrognozaTheme.textColors.mediumEmphasis
    )
}

@Composable
fun CurrentHourFeelsLikeTemperature(
    feelsLike: Double?,
    unit: MeasurementUnit
) {
    Text(
        text = ComposeStringFormatting.formatFeelsLike(
            feelsLike = feelsLike,
            unit = unit
        ),
        style = PrognozaTheme.typography.subtitle1,
        color = PrognozaTheme.textColors.mediumEmphasis
    )
}