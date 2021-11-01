package hr.dtakac.prognoza.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import hr.dtakac.prognoza.viewmodel.TodayFragmentViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZonedDateTime

class ComposeForecastActivity : ComponentActivity() {
    private val todayViewModel by viewModel<TodayFragmentViewModel>()

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

        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {
            todayForecast?.let { today ->
                item {
                    CurrentHourHeader(currentHour = today.currentHour)
                }
                today.otherHours.forEach {
                    item {
                        ExpandableHour(hour = it)
                    }
                }
            }
        }
    }

    @Composable
    fun CurrentHourHeader(currentHour: HourUiModel) {
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

                    val outdatedForecast by todayViewModel.outdatedForecastMessage.observeAsState()
                    OutdatedForecastMessage(
                        outdatedForecastUiModel = outdatedForecast
                    )
                }
                Image(
                    painter = rememberImagePainter(
                        data = currentHour.weatherDescription?.iconResourceId
                            ?: R.drawable.ic_cloud_off
                    ),
                    contentDescription = "Weather icon",
                    modifier = Modifier.size(size = 86.dp)
                )
            }
        }
    }

    @Composable
    fun ExpandableHour(hour: HourUiModel) {

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
        val openDialog = remember { mutableStateOf(false) }

        outdatedForecastUiModel?.let {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cloud_off),
                    contentDescription = "Outdated forecast icon",
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
                    onClick = { openDialog.value = true },
                    style = AppTheme.typography.subtitle1,
                )
            }
            OutdatedForecastDialog(
                openDialog = openDialog,
                reasonId = it.reason ?: R.string.error_generic
            )
        }
    }

    @Composable
    fun OutdatedForecastDialog(
        openDialog: MutableState<Boolean>,
        reasonId: Int
    ) {
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text(text = stringResource(id = R.string.title_outdated_forecast)) },
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
                            onClick = { openDialog.value = false }
                        ) {
                            Text(stringResource(id = R.string.action_ok))
                        }
                    }
                }
            )
        }
    }
}