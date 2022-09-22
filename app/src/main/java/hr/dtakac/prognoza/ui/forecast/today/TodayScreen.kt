package hr.dtakac.prognoza.ui.forecast.today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.forecast.TodayUi
import hr.dtakac.prognoza.presentation.forecast.DayHourUi
import hr.dtakac.prognoza.ui.forecast.HourItem
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.math.max

@Composable
fun TodayScreen(
    state: TodayUi,
    place: TextResource,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    onBackgroundColor: Color = Color.Unspecified,
    onPlaceVisibilityChange: (Float) -> Unit = {},
    onDateTimeVisibilityChange: (Float) -> Unit = {},
    onTemperatureVisibilityChange: (Float) -> Unit = {}
) {
    CompositionLocalProvider(LocalContentColor provides onBackgroundColor) {
        Box(modifier = modifier) {
            val listState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(horizontal = 24.dp),
                state = listState
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item(key = "place") {
                    Text(
                        text = place.asString(),
                        style = PrognozaTheme.typography.titleLarge,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item(key = "time") {
                    Text(
                        text = state.time.asString(),
                        style = PrognozaTheme.typography.subtitleLarge
                    )
                }
                item(key = "temperature") {
                    AutoSizeText(
                        text = state.temperature.asString(),
                        style = PrognozaTheme.typography.headlineLarge,
                        maxFontSize = PrognozaTheme.typography.headlineLarge.fontSize,
                        maxLines = 1
                    )
                }
                item {
                    DescriptionAndLowHighTemperature(
                        description = state.description.asString(),
                        lowHighTemperature = state.lowHighTemperature.asString(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    WindAndPrecipitation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 42.dp),
                        wind = state.wind.asString(),
                        precipitation = state.precipitation.asString()
                    )
                }
                item {
                    HourlyHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 42.dp, bottom = 16.dp)
                    )
                }
                items(state.hourly) { hour ->
                    HourItem(
                        hour = hour,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }
            }

            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo }
                    .distinctUntilChanged()
                    .map { layoutInfo ->
                        Triple(
                            layoutInfo.keyVisibilityPercent("place"),
                            layoutInfo.keyVisibilityPercent("time"),
                            layoutInfo.keyVisibilityPercent("temperature")
                        )
                    }
                    .distinctUntilChanged()
                    .collect { (placeVis, dateTimeVis, temperatureVis) ->
                        onPlaceVisibilityChange(placeVis)
                        onDateTimeVisibilityChange(dateTimeVis)
                        onTemperatureVisibilityChange(temperatureVis)
                    }
            }
        }
    }
}

@Composable
private fun DescriptionAndLowHighTemperature(
    description: String,
    lowHighTemperature: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.titleLarge) {
            Text(
                modifier = Modifier.weight(1f),
                text = description
            )
            Text(
                modifier = Modifier.weight(1f),
                text = lowHighTemperature,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun WindAndPrecipitation(
    wind: String,
    precipitation: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.body) {
            Text(
                modifier = Modifier.weight(1f),
                text = wind,
            )
            Text(
                modifier = Modifier.weight(1f),
                text = precipitation,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun HourlyHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.hourly),
            style = PrognozaTheme.typography.label,
        )
        Divider(color = LocalContentColor.current, modifier = Modifier.padding(top = 16.dp))
    }
}

@Preview
@Composable
private fun TodayScreenPreview() {
    PrognozaTheme(description = ForecastDescription.Short.CLEAR) {
        TodayScreen(fakeContent(), place = TextResource.fromText("Helsinki"))
    }
}

private fun fakeContent(): TodayUi = TodayUi(
    time = TextResource.fromText("September 12"),
    temperature = TextResource.fromText("1°"),
    feelsLike = TextResource.fromText("Feels like 28°"),
    description = TextResource.fromText("Clear sky, sleet soon"),
    lowHighTemperature = TextResource.fromText("15°—7°"),
    wind = TextResource.fromText("Wind: 15 km/h"),
    precipitation = TextResource.fromText("Precipitation: 0 mm"),
    shortDescription = ForecastDescription.Short.CLEAR,
    hourly = mutableListOf<DayHourUi>().apply {
        for (i in 1..100) {
            add(
                DayHourUi(
                    time = TextResource.fromText("14:00"),
                    temperature = TextResource.fromText("23°"),
                    precipitation = TextResource.fromText("1.99 mm"),
                    description = TextResource.fromText("Clear and some more text"),
                    icon = R.drawable.heavyrainshowersandthunder_day
                )
            )
        }
    }
)


// https://stackoverflow.com/a/69267808
private fun LazyListLayoutInfo.keyVisibilityPercent(key: String): Float =
    visibleItemsInfo.firstOrNull { it.key == key }?.let {
        val cutTop = max(0, viewportStartOffset - it.offset)
        val cutBottom = max(0, it.offset + it.size - viewportEndOffset)
        max(0f, 100f - (cutTop + cutBottom) * 100f / it.size)
    } ?: 0f