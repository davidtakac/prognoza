package hr.dtakac.prognoza.forecast.composable.forecastpager

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.composable.coming.ComingForecast
import hr.dtakac.prognoza.forecast.composable.today.TodayForecast
import hr.dtakac.prognoza.forecast.composable.tomorrow.TomorrowForecast
import hr.dtakac.prognoza.forecast.viewmodel.ComingForecastViewModel
import hr.dtakac.prognoza.forecast.viewmodel.ForecastPagerViewModel
import hr.dtakac.prognoza.forecast.viewmodel.TodayForecastViewModel
import hr.dtakac.prognoza.forecast.viewmodel.TomorrowForecastViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun ForecastTabbedPager(
    todayForecastViewModel: TodayForecastViewModel,
    tomorrowForecastViewModel: TomorrowForecastViewModel,
    comingForecastViewModel: ComingForecastViewModel,
    forecastPagerViewModel: ForecastPagerViewModel,
    onSearchClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onPageChanged: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    // Pager
    val pagerState = rememberPagerState()
    val pages = stringArrayResource(id = R.array.forecast_tab_names)

    // Forecast pager data
    val preferredUnit by forecastPagerViewModel.preferredUnit
    val placeName by forecastPagerViewModel.placeName

    // Today forecast data
    val todayForecast by todayForecastViewModel.forecast
    val outdatedTodayForecast by todayForecastViewModel.outdatedForecast
    val isTodayForecastLoading by todayForecastViewModel.isLoading
    val emptyTodayForecast by todayForecastViewModel.emptyForecast
    val expandedTodayForecastHourIndices = todayForecastViewModel.expandedHourIndices

    // Tomorrow forecast data
    val tomorrowForecast by tomorrowForecastViewModel.forecast
    val outdatedTomorrowForecast by tomorrowForecastViewModel.outdatedForecast
    val isTomorrowForecastLoading by tomorrowForecastViewModel.isLoading
    val emptyTomorrowForecast by tomorrowForecastViewModel.emptyForecast
    val expandedTomorrowForecastHourIndices = tomorrowForecastViewModel.expandedHourIndices

    // Coming forecast data
    val comingForecast by comingForecastViewModel.forecast
    val outdatedComingForecast by comingForecastViewModel.outdatedForecast
    val isComingForecastLoading by comingForecastViewModel.isLoading
    val emptyComingForecast by comingForecastViewModel.emptyForecast
    val expandedComingForecastDayIndices = comingForecastViewModel.expandedDayIndices

    PageChangedListener(
        pagerState = pagerState,
        onPageChanged = onPageChanged
    )

    Column {
        ForecastHeader(
            currentPlaceName = placeName,
            pagerState = pagerState,
            pages = pages,
            onSearchClicked = onSearchClicked,
            onSettingsClicked = onSettingsClicked,
            onTabClicked = { scope.launch { pagerState.animateScrollToPage(it) } }
        )
        ForecastPager(
            count = pages.size,
            pagerState = pagerState,
            firstPage = {
                TodayForecast(
                    forecast = todayForecast,
                    outdatedForecast = outdatedTodayForecast,
                    emptyForecast = emptyTodayForecast,
                    isLoading = isTodayForecastLoading,
                    expandedHourIndices = expandedTodayForecastHourIndices,
                    onHourClicked = { index: Int -> todayForecastViewModel.toggleExpanded(index) },
                    onTryAgainClicked = { todayForecastViewModel.getForecast() },
                    preferredMeasurementUnit = preferredUnit
                )
            },
            secondPage = {
                TomorrowForecast(
                    forecast = tomorrowForecast,
                    outdatedForecast = outdatedTomorrowForecast,
                    emptyForecast = emptyTomorrowForecast,
                    isLoading = isTomorrowForecastLoading,
                    expandedHourIndices = expandedTomorrowForecastHourIndices,
                    onHourClicked = { index: Int -> tomorrowForecastViewModel.toggleExpanded(index) },
                    onTryAgainClicked = { tomorrowForecastViewModel.getForecast() },
                    preferredMeasurementUnit = preferredUnit
                )
            },
            thirdPage = {
                ComingForecast(
                    forecast = comingForecast,
                    outdatedForecast = outdatedComingForecast,
                    emptyForecast = emptyComingForecast,
                    isLoading = isComingForecastLoading,
                    expandedHourIndices = expandedComingForecastDayIndices,
                    onDayClicked = { index: Int -> comingForecastViewModel.toggleExpanded(index) },
                    onTryAgainClicked = { comingForecastViewModel.getForecast() },
                    preferredMeasurementUnit = preferredUnit
                )
            }
        )
    }
}

@ExperimentalPagerApi
@Composable
fun ForecastHeader(
    currentPlaceName: String,
    pagerState: PagerState,
    pages: Array<String>,
    onSearchClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onTabClicked: (Int) -> Unit
) {
    val backgroundColor = if (isSystemInDarkTheme()) {
        PrognozaTheme.colors.surface
    } else {
        PrognozaTheme.colors.primary
    }
    Surface(
        elevation = 4.dp,
        color = backgroundColor,
        contentColor = contentColorFor(backgroundColor)
    ) {
        Column {
            ForecastTopAppBar(
                title = currentPlaceName,
                onSearchClicked = onSearchClicked,
                onSettingsClicked = onSettingsClicked
            )
            ForecastTabRow(
                pagerState = pagerState,
                pages = pages,
                onTabClicked = onTabClicked,
                backgroundColor = backgroundColor
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun ForecastTabRow(
    pagerState: PagerState,
    pages: Array<String>,
    backgroundColor: Color,
    onTabClicked: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.pagerTabIndicatorOffset(
                    pagerState = pagerState,
                    tabPositions = tabPositions
                ),
            )
        },
        backgroundColor = backgroundColor,
        contentColor = contentColorFor(backgroundColor)
    ) {
        pages.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = pagerState.currentPage == index,
                onClick = { onTabClicked(index) }
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun ForecastPager(
    count: Int,
    pagerState: PagerState,
    firstPage: @Composable () -> Unit,
    secondPage: @Composable () -> Unit,
    thirdPage: @Composable () -> Unit
) {
    HorizontalPager(
        count = count,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrognozaTheme.colors.background)
    ) { page ->
        when (page) {
            0 -> firstPage()
            1 -> secondPage()
            2 -> thirdPage()
        }
    }
}

@ExperimentalPagerApi
@Composable
fun PageChangedListener(
    pagerState: PagerState,
    onPageChanged: (Int) -> Unit
) {
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            onPageChanged(it)
        }
    }
}

@Composable
fun ForecastTopAppBar(
    title: String,
    onSearchClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(text = title)
        },
        elevation = 0.dp, // because elevation is set on the parent surface
        actions = {
            IconButton(onClick = onSearchClicked) {
                Icon(
                    painter = rememberImagePainter(data = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp)
                )
            }
            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                Icon(
                    painter = rememberImagePainter(data = R.drawable.ic_more),
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp)
                )
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier.width(128.dp)
                ) {
                    DropdownMenuItem(
                        onClick = {
                            isMenuExpanded = false
                            onSettingsClicked()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.settings))
                    }
                }
            }
        }
    )
}