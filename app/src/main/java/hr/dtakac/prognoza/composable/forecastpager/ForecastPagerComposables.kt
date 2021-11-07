package hr.dtakac.prognoza.composable.forecastpager

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.composable.coming.ComingForecast
import hr.dtakac.prognoza.composable.today.TodayForecast
import hr.dtakac.prognoza.composable.tomorrow.TomorrowForecast
import hr.dtakac.prognoza.model.ui.MeasurementUnit
import hr.dtakac.prognoza.theme.AppTheme
import hr.dtakac.prognoza.viewmodel.ComingForecastViewModel
import hr.dtakac.prognoza.viewmodel.ForecastPagerViewModel
import hr.dtakac.prognoza.viewmodel.TodayForecastViewModel
import hr.dtakac.prognoza.viewmodel.TomorrowForecastViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun ForecastTabbedPager(
    todayForecastViewModel: TodayForecastViewModel,
    tomorrowForecastViewModel: TomorrowForecastViewModel,
    comingForecastViewModel: ComingForecastViewModel,
    forecastPagerViewModel: ForecastPagerViewModel
) {
    val pagerState = rememberPagerState()
    val pages = stringArrayResource(id = R.array.forecast_tab_names)

    val selectedUnit by forecastPagerViewModel.selectedUnit.observeAsState()

    PageChangedListener(pagerState = pagerState) { page ->
        when (page) {
            0 -> todayForecastViewModel
            1 -> tomorrowForecastViewModel
            2 -> comingForecastViewModel
            else -> throw IllegalStateException("No composable for page $page.")
        }.getForecast()
    }

    Column {
        val backgroundColor =
            if (isSystemInDarkTheme()) AppTheme.colors.surface else AppTheme.colors.primary
        Surface(
            elevation = 4.dp,
            color = backgroundColor,
            contentColor = contentColorFor(backgroundColor = backgroundColor)
        ) {
            Column {
                ForecastTopAppBar(
                    title = "Osijek",
                    onSearchClicked = { /*TODO*/ },
                    onUnitChanged = { forecastPagerViewModel.changeSelectedUnit() },
                    selectedUnit = selectedUnit ?: MeasurementUnit.METRIC
                )
                ForecastTabRow(
                    pagerState = pagerState,
                    pages = pages
                )
            }
        }
        ForecastPager(
            count = pages.size,
            pagerState = pagerState,
            firstPage = { TodayForecast(viewModel = todayForecastViewModel) },
            secondPage = { TomorrowForecast(viewModel = tomorrowForecastViewModel) },
            thirdPage = { ComingForecast(viewModel = comingForecastViewModel) }
        )
    }
}

@ExperimentalPagerApi
@Composable
fun ForecastTabRow(
    pagerState: PagerState,
    pages: Array<String>
) {
    val scope = rememberCoroutineScope()
    val backgroundColor =
        if (isSystemInDarkTheme()) AppTheme.colors.surface else AppTheme.colors.primary

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
            )
        },
        backgroundColor = backgroundColor,
        contentColor = contentColorFor(backgroundColor = backgroundColor),
    ) {
        pages.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = pagerState.currentPage == index,
                onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
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
        modifier = Modifier.background(
            color = AppTheme.colors.background
        )
    ) { page ->
        when (page) {
            0 -> firstPage.invoke()
            1 -> secondPage.invoke()
            2 -> thirdPage.invoke()
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
            onPageChanged.invoke(it)
        }
    }
}

@Composable
fun ForecastTopAppBar(
    title: String,
    selectedUnit: MeasurementUnit,
    onSearchClicked: () -> Unit,
    onUnitChanged: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(text = title)
        },
        elevation = 0.dp,
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
                            onUnitChanged.invoke()
                        }
                    ) {
                        Text(
                            text = stringResource(
                                id = when (selectedUnit) {
                                    MeasurementUnit.IMPERIAL -> R.string.change_to_metric
                                    MeasurementUnit.METRIC -> R.string.change_to_imperial
                                }
                            )
                        )
                    }
                }
            }
        }
    )
}