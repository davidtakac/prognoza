package hr.dtakac.prognoza.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.uimodel.cell.HourUiModel
import hr.dtakac.prognoza.uimodel.forecast.OutdatedForecastUiModel
import hr.dtakac.prognoza.utils.ComposeStringFormatting
import hr.dtakac.prognoza.viewmodel.TodayFragmentViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeForecastActivity : ComponentActivity() {
    private val todayViewModel by viewModel<TodayFragmentViewModel>()

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todayViewModel.getForecast()
        setContent {
            Screen()
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

            HorizontalPager(
                count = pages.size,
                state = pagerState
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

        LazyColumn {
            todayForecast?.let {
                item {
                    CurrentHourHeader(currentHour = it.currentHour)
                }
            }
        }
    }

    @Composable
    fun CurrentHourHeader(currentHour: HourUiModel) {
        Row {
            Column {
                Text(
                    text = ComposeStringFormatting.formatCurrentHourHeaderTime(
                        time = currentHour.time
                    )
                )

                Text(
                    text = ComposeStringFormatting.formatTemperatureValue(
                        temperature = currentHour.temperature,
                        unit = currentHour.displayDataInUnit
                    )
                )

                Text(
                    text = if (currentHour.precipitationAmount != null && currentHour.precipitationAmount > 0) {
                        ComposeStringFormatting.formatPrecipitationValue(
                            precipitation = currentHour.precipitationAmount,
                            unit = currentHour.displayDataInUnit
                        )
                    } else {
                        ComposeStringFormatting.formatWeatherIconDescription(
                            id = currentHour.weatherDescription?.descriptionResourceId
                        )
                    }
                )

                Text(
                    text = ComposeStringFormatting.formatFeelsLike(
                        feelsLike = currentHour.feelsLike,
                        unit = currentHour.displayDataInUnit
                    )
                )

                val outdatedForecast by todayViewModel.outdatedForecastMessage.observeAsState()
                OutdatedForecastMessage(
                    outdatedForecastUiModel = outdatedForecast
                )
            }
        }
    }

    @Composable
    fun OutdatedForecastMessage(outdatedForecastUiModel: OutdatedForecastUiModel?) {
        val openDialog = remember { mutableStateOf(false) }

        outdatedForecastUiModel?.let {
            ClickableText(
                text = AnnotatedString(text = stringResource(id = R.string.title_outdated_forecast)),
                onClick = { openDialog.value = true }
            )
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