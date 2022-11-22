package hr.dtakac.prognoza.ui.forecast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.forecast.ForecastUi
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.theme.asWeatherIconResId
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.map

@Composable
fun ForecastDataList(
    data: ForecastUi,
    isPlaceVisible: (Boolean) -> Unit = {},
    isDateVisible: (Boolean) -> Unit = {},
    isTemperatureVisible: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // The caller needs info on whether certain parts of the list are visible or not so it can
    // update the toolbar.
    val listState = rememberLazyListState()
    ItemVisibilityChanges(
        listState = listState,
        isPlaceVisible = isPlaceVisible,
        isDateVisible = isDateVisible,
        isTemperatureVisible = isTemperatureVisible
    )
    // Hour and coming parts of the UI are like tables where some columns need to be as wide as
    // the widest one in the list.
    val hourItemDimensions = rememberDayHourDimensions(hours = data.today?.hourly ?: listOf())
    val comingItemDimensions = rememberHeaderDimensions(days = data.coming ?: listOf())
    // Horizontal padding isn't included in contentPadding because the click ripple on the Coming
    // day items looks better when it goes edge-to-edge
    val itemPadding = remember { PaddingValues(horizontal = 24.dp) }
    // Expanded coming items get reset when they get out of view otherwise
    var expandedComingItems: Set<Int> by remember(data.coming) { mutableStateOf(setOf()) }
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(vertical = 24.dp),
        modifier = modifier
    ) {
        item(key = "place") {
            Text(
                text = data.current.place.asString(),
                style = PrognozaTheme.typography.titleLarge,
                modifier = Modifier.padding(itemPadding)
            )
        }
        item(key = "place-time-spacer") {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item(key = "time") {
            Text(
                text = data.current.date.asString(),
                style = PrognozaTheme.typography.subtitleLarge,
                modifier = Modifier.padding(itemPadding)
            )
        }
        item(key = "temperature") {
            AutoSizeText(
                text = data.current.temperature.asString(),
                style = PrognozaTheme.typography.headlineLarge,
                maxFontSize = PrognozaTheme.typography.headlineLarge.fontSize,
                maxLines = 1,
                modifier = Modifier.padding(itemPadding)
            )
        }
        item(key = "description-and-precipitation") {
            DescriptionAndPrecipitation(
                description = data.current.description.asString(),
                icon = data.current.weatherIconDescription.asWeatherIconResId(),
                precipitation = data.current.precipitation.asString(),
                modifier = Modifier
                    .padding(itemPadding)
                    .fillMaxWidth()
            )
        }
        item(key = "wind-and-feels-like") {
            WindAndFeelsLike(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(itemPadding)
                    .padding(top = 42.dp),
                feelsLike = data.current.feelsLike.asString(),
                wind = data.current.wind.asString()
            )
        }
        data.today?.hourly?.let { hours ->
            item(key = "hourly-header") {
                HourlyHeader(
                    lowHighTemperature = data.today.lowHighTemperature.asString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(itemPadding)
                        .padding(top = 42.dp, bottom = 16.dp)
                )
            }
            itemsIndexed(hours) { idx, hour ->
                DayHour(
                    data = hour,
                    dimensions = hourItemDimensions,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(itemPadding)
                        .padding(bottom = if (idx == data.today.hourly.lastIndex) 0.dp else 12.dp),
                )
            }
        }
        data.coming?.let {
            item(key = "coming-header") {
                ComingHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(itemPadding)
                        .padding(top = 42.dp, bottom = 6.dp)
                )
            }
            itemsIndexed(it) { idx, day ->
                ComingDay(
                    data = day,
                    dimensions = comingItemDimensions,
                    isExpanded = idx in expandedComingItems,
                    onClick = {
                        expandedComingItems = expandedComingItems.toMutableSet().apply {
                            if (idx in expandedComingItems) remove(idx)
                            else add(idx)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ItemVisibilityChanges(
    listState: LazyListState,
    isPlaceVisible: (Boolean) -> Unit,
    isDateVisible: (Boolean) -> Unit,
    isTemperatureVisible: (Boolean) -> Unit
) {
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .distinctUntilChanged()
            .dropWhile { it.totalItemsCount == 0 }
            .map { layoutInfo ->
                Triple(
                    layoutInfo.keyVisibilityPercent("place") != 0f,
                    layoutInfo.keyVisibilityPercent("time") != 0f,
                    layoutInfo.keyVisibilityPercent("temperature") > 50f
                )
            }
            .distinctUntilChanged()
            .collect { (isPlaceVisible, isDateVisible, isTemperatureVisible) ->
                isPlaceVisible(isPlaceVisible)
                isDateVisible(isDateVisible)
                isTemperatureVisible(isTemperatureVisible)
            }
    }
}