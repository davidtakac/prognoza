package hr.dtakac.prognoza.composable.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.composable.today.TodayForecast
import hr.dtakac.prognoza.theme.AppTheme
import hr.dtakac.prognoza.viewmodel.TodayForecastViewModel
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun TabbedForecastPager(
    todayForecastViewModel: TodayForecastViewModel
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val pages = stringArrayResource(id = R.array.forecast_tab_names)

    Column {
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
                0 -> TodayForecast(todayForecastViewModel = todayForecastViewModel)
                else -> Text("Page ${pages[page]}")
            }
        }
    }
}
