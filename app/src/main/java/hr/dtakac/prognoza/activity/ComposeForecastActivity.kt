package hr.dtakac.prognoza.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.theme.AppTheme
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.WeatherDescription
import hr.dtakac.prognoza.uimodel.cell.HourUiModel
import hr.dtakac.prognoza.uimodel.forecast.OutdatedForecastUiModel
import hr.dtakac.prognoza.utils.ComposeStringFormatting
import hr.dtakac.prognoza.viewmodel.TodayForecastViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZonedDateTime

class ComposeForecastActivity : ComponentActivity() {
    private val todayViewModel by viewModel<TodayForecastViewModel>()

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todayViewModel.getForecast()
        setContent {
            AppTheme {
                Screen()
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    fun Screen() {
        Box {
            TabbedPager()
        }
    }

    @ExperimentalPagerApi
    @Composable
    fun TabbedPager() {
        Column {
            val pagerState = rememberPagerState()
            val scope = rememberCoroutineScope()
            val pages = stringArrayResource(id = R.array.forecast_tab_names)

            Surface(
                shape = AppTheme.shapes.large,
                color = AppTheme.colors.surface,
                contentColor = AppTheme.colors.onSurface,
                elevation = 4.dp
            ) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                        )
                    }
                ) {
                    pages.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = pagerState.currentPage == index,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        )
                    }
                }
            }

            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier.background(
                    color = AppTheme.colors.background
                )
            ) { page ->
                when (page) {
                    0 -> TodayForecast()
                    else -> Text("Page ${pages[page]}")
                }
            }
        }
    }

    @Composable
    fun TodayForecast() {
        val todayForecast by todayViewModel.forecast.observeAsState()
        val outdatedForecast by todayViewModel.outdatedForecastMessage.observeAsState()
        val expandedHourIndices = todayViewModel.expandedHourIndices

        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {
            todayForecast?.let { today ->
                item {
                    CurrentHourHeader(
                        currentHour = today.currentHour,
                        outdatedForecast = outdatedForecast
                    )
                }
                today.otherHours.forEachIndexed { index, hour ->
                    item {
                        ExpandableHour(
                            hour = hour,
                            isExpanded = index in expandedHourIndices,
                            onClick = { todayViewModel.toggleHour(index) }
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
    fun CurrentHourHeader(
        currentHour: HourUiModel,
        outdatedForecast: OutdatedForecastUiModel?
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
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
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
                    OutdatedForecastMessage(
                        outdatedForecastUiModel = outdatedForecast
                    )
                }
                Image(
                    painter = rememberImagePainter(
                        data = currentHour.weatherDescription?.iconResourceId
                            ?: R.drawable.ic_cloud_off
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(size = 86.dp)
                )
            }
        }
    }

    @Composable
    fun ExpandableHour(
        isExpanded: Boolean,
        hour: HourUiModel,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .clickable { onClick.invoke() }
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
        ) {
            HourSummary(
                modifier = Modifier.fillMaxWidth(),
                time = hour.time,
                precipitation = hour.precipitationAmount,
                temperature = hour.temperature,
                weatherDescription = hour.weatherDescription,
                unit = hour.displayDataInUnit
            )
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                HourDetails(
                    feelsLike = hour.feelsLike,
                    windSpeed = hour.windSpeed,
                    windFromCompassDirection = hour.windFromCompassDirection,
                    pressure = hour.airPressureAtSeaLevel,
                    humidity = hour.relativeHumidity,
                    unit = hour.displayDataInUnit
                )
            }
        }
    }

    @Composable
    fun HourSummary(
        modifier: Modifier,
        time: ZonedDateTime,
        precipitation: Double?,
        temperature: Double?,
        weatherDescription: WeatherDescription?,
        unit: MeasurementUnit
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ComposeStringFormatting.formatHourTime(time = time),
                    style = AppTheme.typography.subtitle1,
                    color = AppTheme.textColors.highEmphasis
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = ComposeStringFormatting.formatPrecipitationValue(
                        precipitation = precipitation,
                        unit = unit
                    ),
                    style = AppTheme.typography.subtitle2
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ComposeStringFormatting.formatTemperatureValue(
                        temperature = temperature,
                        unit = unit
                    ),
                    style = AppTheme.typography.subtitle1,
                    color = AppTheme.textColors.highEmphasis
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = rememberImagePainter(
                        data = weatherDescription?.iconResourceId
                            ?: R.drawable.ic_cloud_off
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(size = 36.dp)
                )
            }
        }
    }

    @Composable
    fun HourDetails(
        feelsLike: Double?,
        windSpeed: Double?,
        windFromCompassDirection: Int?,
        pressure: Double?,
        humidity: Double?,
        unit: MeasurementUnit
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            FlowRow(
                mainAxisSpacing = 4.dp,
                crossAxisSpacing = 4.dp
            ) {
                DetailsItem(
                    iconId = R.drawable.ic_thermostat,
                    labelId = R.string.feels_like,
                    text = ComposeStringFormatting.formatTemperatureValue(
                        temperature = feelsLike,
                        unit = unit
                    )
                )
                DetailsItem(
                    iconId = R.drawable.ic_air,
                    labelId = R.string.wind,
                    text = ComposeStringFormatting.formatWindWithDirection(
                        windSpeed = windSpeed,
                        windFromCompassDirection = windFromCompassDirection,
                        windSpeedUnit = unit
                    )
                )
                DetailsItem(
                    iconId = R.drawable.ic_water_drop,
                    labelId = R.string.humidity,
                    text = ComposeStringFormatting.formatHumidityValue(
                        relativeHumidity = humidity
                    )
                )
                DetailsItem(
                    iconId = R.drawable.ic_speed,
                    labelId = R.string.pressure,
                    text = ComposeStringFormatting.formatPressureValue(
                        pressure = pressure,
                        unit = unit
                    )
                )
            }
        }
    }

    @Composable
    fun DetailsItem(
        @DrawableRes iconId: Int,
        @StringRes labelId: Int,
        text: String
    ) {
        Surface(
            shape = AppTheme.shapes.small,
            color = AppTheme.colors.surface,
            contentColor = AppTheme.colors.onSurface,
            elevation = 2.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DetailsIcon(id = iconId)
                    Spacer(modifier = Modifier.width(4.dp))
                    DetailsLabel(id = labelId)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = text,
                    style = AppTheme.typography.subtitle2,
                    color = AppTheme.textColors.highEmphasis
                )
            }
        }
    }

    @Composable
    fun DetailsIcon(@DrawableRes id: Int) {
        Image(
            painter = rememberImagePainter(data = id),
            contentDescription = null,
            modifier = Modifier.size(size = 18.dp),
            colorFilter = ColorFilter.tint(color = AppTheme.textColors.highEmphasis)
        )
    }

    @Composable
    fun DetailsLabel(@StringRes id: Int) {
        Text(
            text = stringResource(id = id),
            style = AppTheme.typography.subtitle2,
            color = AppTheme.textColors.mediumEmphasis
        )
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
        val isShowingPrecipitation = precipitation != null && precipitation > 0
        Text(
            text = if (isShowingPrecipitation) {
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

    @Composable
    fun OutdatedForecastMessage(outdatedForecastUiModel: OutdatedForecastUiModel?) {
        var showDialog by remember { mutableStateOf(false) }

        outdatedForecastUiModel?.let {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cloud_off),
                    contentDescription = null,
                    modifier = Modifier.size(size = 18.dp),
                    colorFilter = ColorFilter.tint(color = AppTheme.textColors.mediumEmphasis)
                )
                Spacer(modifier = Modifier.width(4.dp))
                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = AppTheme.textColors.mediumEmphasis)) {
                            append(text = stringResource(id = R.string.notify_cached_data))
                        }
                    },
                    onClick = { showDialog = true },
                    style = AppTheme.typography.subtitle1,
                )
            }
            OutdatedForecastDialog(
                showDialog = showDialog,
                reasonId = it.reason ?: R.string.error_generic,
                onConfirmRequest = { showDialog = false },
                onDismissRequest = { showDialog = false }
            )
        }
    }

    @Composable
    fun OutdatedForecastDialog(
        showDialog: Boolean,
        reasonId: Int,
        onDismissRequest: () -> Unit,
        onConfirmRequest: () -> Unit
    ) {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = onDismissRequest,
                title = {
                    Text(
                        text = stringResource(id = R.string.title_outdated_forecast)
                    )
                },
                text = {
                    Text(
                        text = stringResource(
                            id = R.string.template_content_outdated_forecast,
                            stringResource(id = reasonId)
                        )
                    )
                },
                buttons = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            modifier = Modifier.wrapContentSize(),
                            onClick = { onConfirmRequest.invoke() }
                        ) {
                            Text(stringResource(id = R.string.action_ok))
                        }
                    }
                }
            )
        }
    }
}