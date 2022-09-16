package hr.dtakac.prognoza.ui.today

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.today.TodayContent
import hr.dtakac.prognoza.presentation.today.TodayHour
import hr.dtakac.prognoza.presentation.today.TodayUiState
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import kotlin.math.max

@Composable
fun TodayScreen(state: TodayUiState) {
    if (state.content != null) {
        PrognozaTheme(state.content.shortDescription) {
            Content(state.content)
        }
    }
}

@Composable
private fun Content(content: TodayContent) {
    val contentColor by animateColorAsState(
        targetValue = PrognozaTheme.colors.onBackground.copy(
            alpha = 0.87f
        )
    )
    val backgroundColor by animateColorAsState(
        targetValue = PrognozaTheme.colors.background
    )

    StatusAndNavigationBars(backgroundColor)
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(horizontal = 24.dp)
        ) {
            val state = rememberLazyListState()
            var placeNameVisible by remember { mutableStateOf(false) }
            var timeVisible by remember { mutableStateOf(false) }
            var temperatureVisible by remember { mutableStateOf(false) }

            Toolbar(
                placeName = content.placeName.asString(),
                placeNameVisible = placeNameVisible,
                time = content.time.asString(),
                timeVisible = timeVisible,
                temperature = content.temperature.asString(),
                temperatureVisible = temperatureVisible
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = state
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item(key = "place") {
                    Text(
                        text = content.placeName.asString(),
                        style = PrognozaTheme.typography.titleLarge,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item(key = "time") {
                    Text(
                        text = content.time.asString(),
                        style = PrognozaTheme.typography.subtitleLarge
                    )
                }
                item(key = "temperature") {
                    AutoSizeText(
                        text = content.temperature.asString(),
                        style = PrognozaTheme.typography.headlineLarge,
                        maxFontSize = PrognozaTheme.typography.headlineLarge.fontSize,
                        maxLines = 1
                    )
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = content.description.asString(),
                            style = PrognozaTheme.typography.titleLarge,
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = content.lowHighTemperature.asString(),
                            style = PrognozaTheme.typography.titleLarge,
                            textAlign = TextAlign.End
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 42.dp)) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = content.wind.asString(),
                            style = PrognozaTheme.typography.bodySmall,
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = content.precipitation.asString(),
                            style = PrognozaTheme.typography.bodySmall,
                            textAlign = TextAlign.End
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.hourly),
                        style = PrognozaTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 42.dp)
                    )
                    Divider(color = LocalContentColor.current, modifier = Modifier.padding(vertical = 16.dp))
                }
                items(content.hourly) { hour ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.width(52.dp),
                            text = hour.time.asString(),
                            style = PrognozaTheme.typography.bodySmall,
                            textAlign = TextAlign.Start,
                            maxLines = 1
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = hour.description.asString(),
                            style = PrognozaTheme.typography.bodySmall,
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            modifier = Modifier.width(88.dp),
                            text = hour.precipitation.asString(),
                            style = PrognozaTheme.typography.bodySmall,
                            textAlign = TextAlign.End,
                            maxLines = 1
                        )
                        Text(
                            modifier = Modifier.width(52.dp),
                            text = hour.temperature.asString(),
                            style = PrognozaTheme.typography.bodySmall,
                            textAlign = TextAlign.End,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            placeNameVisible = state.visibleItems(0f).none { it.key == "place" }
            timeVisible = state.visibleItems(0f).none { it.key == "time" }
            temperatureVisible = state.visibleItems(50f).none { it.key == "temperature" }
        }
    }
}

@Composable
private fun StatusAndNavigationBars(color: Color) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color)
    systemUiController.setNavigationBarColor(color)
}

@Composable
private fun Toolbar(
    placeName: String,
    placeNameVisible: Boolean,
    time: String,
    timeVisible: Boolean,
    temperature: String,
    temperatureVisible: Boolean,
    onMenuClicked: () -> Unit = {}
) {
    Column(modifier = Modifier.background(PrognozaTheme.colors.background)) {
        Row(
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onMenuClicked,
                modifier = Modifier.size(42.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    repeat(3) {
                        Divider(
                            color = LocalContentColor.current,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = placeNameVisible,
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                ) {
                    Text(
                        text = placeName,
                        style = PrognozaTheme.typography.titleSmall
                    )
                }
                AnimatedVisibility(
                    visible = timeVisible,
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                ) {
                    Text(
                        text = time,
                        style = PrognozaTheme.typography.subtitleSmall
                    )
                }
            }
            AnimatedVisibility(
                visible = temperatureVisible,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                Text(
                    text = temperature,
                    style = PrognozaTheme.typography.headlineSmall
                )
            }
        }
        Divider(color = LocalContentColor.current)
    }
}

@Preview
@Composable
private fun TodayScreenClearPreview() {
    val state = TodayUiState().copy(
        content = fakeContent().copy(shortDescription = ForecastDescription.Short.CLEAR),
        isLoading = true,
        error = TextResource.fromText("Error test")
    )
    TodayScreen(state)
}

@Preview
@Composable
private fun TodayScreenRainPreview() {
    val state = TodayUiState().copy(
        content = fakeContent().copy(shortDescription = ForecastDescription.Short.RAIN),
        isLoading = true,
        error = TextResource.fromText("Error test")
    )
    TodayScreen(state)
}

@Preview
@Composable
private fun TodayScreenSnowPreview() {
    val state = TodayUiState().copy(
        content = fakeContent().copy(shortDescription = ForecastDescription.Short.SNOW),
        isLoading = true,
        error = TextResource.fromText("Error test")
    )
    TodayScreen(state)
}

@Preview
@Composable
private fun TodayScreenSleetPreview() {
    val state = TodayUiState().copy(
        content = fakeContent().copy(shortDescription = ForecastDescription.Short.SLEET),
        isLoading = true,
        error = TextResource.fromText("Error test")
    )
    TodayScreen(state)
}

@Preview
@Composable
private fun TodayScreenCloudyPreview() {
    val state = TodayUiState().copy(
        content = fakeContent().copy(shortDescription = ForecastDescription.Short.CLOUDY),
        isLoading = true,
        error = TextResource.fromText("Error test")
    )
    TodayScreen(state)
}

@Preview
@Composable
private fun TodayScreenLoadingPreview() {
    TodayScreen(TodayUiState().copy(isLoading = true))
}

private fun fakeContent(): TodayContent = TodayContent(
    placeName = TextResource.fromText("Helsinki"),
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
private fun LazyListState.visibleItems(itemVisiblePercentThreshold: Float) =
    layoutInfo.visibleItemsInfo.filter { visibilityPercent(it) >= itemVisiblePercentThreshold }

private fun LazyListState.visibilityPercent(info: LazyListItemInfo): Float {
    val cutTop = max(0, layoutInfo.viewportStartOffset - info.offset)
    val cutBottom = max(0, info.offset + info.size - layoutInfo.viewportEndOffset)
    return max(0f, 100f - (cutTop + cutBottom) * 100f / info.size)
}