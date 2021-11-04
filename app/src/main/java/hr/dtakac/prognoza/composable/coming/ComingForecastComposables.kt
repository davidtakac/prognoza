package hr.dtakac.prognoza.composable.coming

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowRow
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.common.utils.ComposeStringFormatting
import hr.dtakac.prognoza.common.utils.ComposeStringFormatting.getTomorrowTime
import hr.dtakac.prognoza.composable.common.DaySummaryTime
import hr.dtakac.prognoza.composable.common.DetailsItem
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
    val isLoading by viewModel.isLoading.observeAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {
            forecast?.let { forecast ->
                itemsIndexed(forecast.days) { index, day ->
                    ExpandableDay(
                        isExpanded = index in expandedHourIndices,
                        dayUiModel = day,
                        onClick = { viewModel.toggleExpanded(index) },
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = if (index == forecast.days.lastIndex) 16.dp else 0.dp
                        ),
                        isTomorrow = index == 0
                    )
                }
            }
        }
        if (isLoading == true) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ExpandableDay(
    isTomorrow: Boolean,
    isExpanded: Boolean,
    dayUiModel: DayUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = AppTheme.shapes.medium,
        color = AppTheme.colors.surface,
        contentColor = AppTheme.colors.onSurface,
        elevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .clickable { onClick.invoke() }
                .padding(16.dp)
        ) {
            DaySummary(
                time = dayUiModel.time,
                isTomorrow = isTomorrow,
                representativeWeatherDescription = dayUiModel.representativeWeatherDescription,
                totalPrecipitation = dayUiModel.totalPrecipitationAmount,
                lowestTemperature = dayUiModel.lowTemperature,
                highestTemperature = dayUiModel.highTemperature,
                unit = dayUiModel.displayDataInUnit
            )
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                DayDetails(
                    maxWindSpeed = dayUiModel.maxWindSpeed,
                    maxWindSpeedFromDirection = dayUiModel.windFromCompassDirection,
                    maxHumidity = dayUiModel.maxHumidity,
                    maxPressure = dayUiModel.maxPressure,
                    unit = dayUiModel.displayDataInUnit
                )
            }
        }
    }
}

@Composable
fun DaySummary(
    time: ZonedDateTime,
    isTomorrow: Boolean,
    representativeWeatherDescription: RepresentativeWeatherDescription?,
    totalPrecipitation: Double?,
    lowestTemperature: Double?,
    highestTemperature: Double?,
    unit: MeasurementUnit
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            if (isTomorrow) {
                TomorrowTime()
            } else {
                DaySummaryTime(time = time)
            }
            Spacer(modifier = Modifier.height(4.dp))
            TotalPrecipitation(totalPrecipitation = totalPrecipitation, unit = unit)
            RepresentativeWeatherDescription(representativeWeatherDescription = representativeWeatherDescription)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(
                    data = representativeWeatherDescription?.weatherDescription?.iconResourceId
                        ?: R.drawable.ic_cloud_off
                ),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            DaySummaryLowestAndHighestTemperature(
                lowestTemperature = lowestTemperature,
                highestTemperature = highestTemperature,
                unit = unit
            )
        }
    }
}

@Composable
fun DaySummaryLowestAndHighestTemperature(
    lowestTemperature: Double?,
    highestTemperature: Double?,
    unit: MeasurementUnit
) {
    val typography = AppTheme.typography.subtitle1
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = ComposeStringFormatting.formatTemperatureValue(
                temperature = highestTemperature,
                unit = unit
            ),
            style = typography,
            color = AppTheme.textColors.highEmphasis
        )
        Text(
            text = ComposeStringFormatting.formatTemperatureValue(
                temperature = lowestTemperature,
                unit = unit
            ),
            style = typography,
            color = AppTheme.textColors.mediumEmphasis
        )
    }
}

@Composable
fun TomorrowTime() {
    Text(
        text = getTomorrowTime(),
        style = AppTheme.typography.subtitle1,
        color = AppTheme.textColors.highEmphasis
    )
}

@Composable
fun DayDetails(
    maxWindSpeed: Double?,
    maxWindSpeedFromDirection: Int?,
    maxHumidity: Double?,
    maxPressure: Double?,
    unit: MeasurementUnit
) {
    FlowRow(
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        DetailsItem(
            iconId = R.drawable.ic_air,
            labelId = R.string.max_wind,
            text = ComposeStringFormatting.formatWindWithDirection(
                windSpeed = maxWindSpeed,
                windFromCompassDirection = maxWindSpeedFromDirection,
                windSpeedUnit = unit
            )
        )
        DetailsItem(
            iconId = R.drawable.ic_water_drop,
            labelId = R.string.max_humidity,
            text = ComposeStringFormatting.formatHumidityValue(
                relativeHumidity = maxHumidity
            )
        )
        DetailsItem(
            iconId = R.drawable.ic_speed,
            labelId = R.string.max_pressure,
            text = ComposeStringFormatting.formatPressureValue(
                pressure = maxPressure,
                unit = unit
            )
        )
    }
}