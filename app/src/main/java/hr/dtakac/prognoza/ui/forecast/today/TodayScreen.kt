package hr.dtakac.prognoza.ui.forecast.today

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.TodayContent
import hr.dtakac.prognoza.presentation.TodayHour
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.math.max

@Composable
fun TodayScreen(
    state: TodayContent,
    modifier: Modifier = Modifier,
    onPlaceVisibilityChange: (Float) -> Unit = {},
    onDateTimeVisibilityChange: (Float) -> Unit = {},
    onTemperatureVisibilityChange: (Float) -> Unit = {}
) {
    Column(modifier = modifier) {
        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            item(key = "place") {
                Text(
                    text = state.place.asString(),
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
                HourlyItem(
                    hour = hour,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
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
        CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.bodySmall) {
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
            style = PrognozaTheme.typography.bodySmall,
        )
        Divider(color = LocalContentColor.current, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
private fun HourlyItem(
    hour: TodayHour,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.bodySmall) {
            Text(
                modifier = Modifier.width(52.dp),
                text = hour.time.asString(),
                textAlign = TextAlign.Start,
                maxLines = 1
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
                text = hour.description.asString(),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.width(88.dp),
                text = hour.precipitation.asString(),
                textAlign = TextAlign.End,
                maxLines = 1
            )
            Text(
                modifier = Modifier.width(52.dp),
                text = hour.temperature.asString(),
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
private fun TodayScreenClearPreview() {
    val state = fakeContent().copy(shortDescription = ForecastDescription.Short.CLEAR)
    TodayScreen(state)
}

@Preview
@Composable
private fun TodayScreenRainPreview() {
    val state = fakeContent().copy(shortDescription = ForecastDescription.Short.RAIN)
    TodayScreen(state)
}

@Preview
@Composable
private fun TodayScreenSnowPreview() {
    val state = fakeContent().copy(shortDescription = ForecastDescription.Short.SNOW)
    TodayScreen(state)
}

@Preview
@Composable
private fun TodayScreenSleetPreview() {
    val state = fakeContent().copy(shortDescription = ForecastDescription.Short.SLEET)
    TodayScreen(state)
}

@Preview
@Composable
private fun TodayScreenCloudyPreview() {
    val state = fakeContent().copy(shortDescription = ForecastDescription.Short.CLOUDY)
    TodayScreen(state)
}

private fun fakeContent(): TodayContent = TodayContent(
    place = TextResource.fromText("Helsinki"),
    time = TextResource.fromText("September 12"),
    temperature = TextResource.fromText("1°"),
    feelsLike = TextResource.fromText("Feels like 28°"),
    description = TextResource.fromText("Clear sky, sleet soon"),
    lowHighTemperature = TextResource.fromText("15°—7°"),
    wind = TextResource.fromText("Wind: 15 km/h"),
    precipitation = TextResource.fromText("Precipitation: 0 mm"),
    shortDescription = ForecastDescription.Short.CLEAR,
    hourly = mutableListOf<TodayHour>().apply {
        for (i in 1..100) {
            add(
                TodayHour(
                    time = TextResource.fromText("14:00"),
                    temperature = TextResource.fromText("23°"),
                    precipitation = TextResource.fromText("1.99 mm"),
                    description = TextResource.fromText("Clear")
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