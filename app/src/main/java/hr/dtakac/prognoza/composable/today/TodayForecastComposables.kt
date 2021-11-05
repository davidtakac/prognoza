package hr.dtakac.prognoza.composable.today

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.common.utils.ComposeStringFormatting
import hr.dtakac.prognoza.common.utils.shouldShowPrecipitation
import hr.dtakac.prognoza.composable.common.EmptyForecast
import hr.dtakac.prognoza.composable.common.ExpandableHour
import hr.dtakac.prognoza.composable.common.OutdatedForecastMessage
import hr.dtakac.prognoza.model.ui.MeasurementUnit
import hr.dtakac.prognoza.model.ui.WeatherDescription
import hr.dtakac.prognoza.model.ui.cell.HourUiModel
import hr.dtakac.prognoza.model.ui.forecast.OutdatedForecastUiModel
import hr.dtakac.prognoza.theme.AppTheme
import hr.dtakac.prognoza.viewmodel.TodayForecastViewModel
import java.time.ZonedDateTime

@Composable
fun TodayForecast(viewModel: TodayForecastViewModel) {

    val forecast by viewModel.forecast.observeAsState()
    val outdatedForecast by viewModel.outdatedForecastMessage.observeAsState()
    val expandedHourIndices = viewModel.expandedHourIndices
    val isLoading by viewModel.isLoading.observeAsState()
    val empty by viewModel.emptyScreen.observeAsState()

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
                        outdatedForecastUiModel = outdatedForecast
                    )
                }
                itemsIndexed(forecast.otherHours) { index, hour ->
                    ExpandableHour(
                        hour = hour,
                        isExpanded = index in expandedHourIndices,
                        onClick = { viewModel.toggleExpanded(index) }
                    )
                    Divider()
                }
            }
        }
        if (isLoading == true) {
            CircularProgressIndicator()
        }
        if (empty != null) {
            EmptyForecast(
                reason = empty?.reasonResourceId?.let { stringResource(id = it) }
                    ?: stringResource(id = R.string.error_generic),
                onTryAgainClick = { viewModel.getForecast() },
                isLoading = isLoading == true
            )
        }
    }
}

@Composable
fun CurrentHourHeader(
    currentHour: HourUiModel,
    outdatedForecastUiModel: OutdatedForecastUiModel?
) {
    Surface(
        shape = AppTheme.shapes.medium,
        color = AppTheme.colors.surface,
        contentColor = AppTheme.colors.onSurface,
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
                    unit = currentHour.displayDataInUnit
                )
                CurrentHourHeaderDescription(
                    precipitation = currentHour.precipitationAmount,
                    weatherDescription = currentHour.weatherDescription,
                    unit = currentHour.displayDataInUnit
                )
                CurrentHourFeelsLikeTemperature(
                    feelsLike = currentHour.feelsLike,
                    unit = currentHour.displayDataInUnit
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
        style = AppTheme.typography.subtitle1,
        color = AppTheme.textColors.highEmphasis
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
        style = AppTheme.typography.h3,
        color = AppTheme.textColors.highEmphasis
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
        style = AppTheme.typography.subtitle1,
        color = AppTheme.textColors.mediumEmphasis
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
        style = AppTheme.typography.subtitle1,
        color = AppTheme.textColors.mediumEmphasis
    )
}