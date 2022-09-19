package hr.dtakac.prognoza.ui.today

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
            val colorAnimationSpec = tween<Color>(durationMillis = 1000)
            val contentColor by animateColorAsState(
                targetValue = PrognozaTheme.colors.onBackground.copy(
                    alpha = 0.87f
                ),
                animationSpec = colorAnimationSpec
            )
            val backgroundColor by animateColorAsState(
                targetValue = PrognozaTheme.colors.background,
                animationSpec = colorAnimationSpec
            )

            StatusAndNavigationBarColors(backgroundColor)
            Content(
                state = state.content,
                contentColor = contentColor,
                backgroundColor = backgroundColor,
                onMenuClicked = { /*todo*/ },
            )
        }
    }
}

@Composable
private fun Content(
    state: TodayContent,
    contentColor: Color = LocalContentColor.current,
    backgroundColor: Color = Color.Unspecified,
    onMenuClicked: () -> Unit = {}
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(horizontal = 24.dp)
        ) {
            val listState = rememberLazyListState()
            val placeVisible by rememberPlaceVisible(listState)
            val timeVisible by rememberTimeVisible(listState)
            val temperatureVisible by rememberTemperatureVisible(listState)

            Toolbar(
                modifier = Modifier.background(backgroundColor),
                place = state.place.asString(),
                placeVisible = placeVisible?.let { !it } ?: false,
                time = state.time.asString(),
                timeVisible = timeVisible?.let { !it } ?: false,
                temperature = state.temperature.asString(),
                temperatureVisible = temperatureVisible?.let { !it } ?: false,
                onMenuClicked = onMenuClicked
            )

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
        }
    }
}

@Composable
private fun StatusAndNavigationBarColors(color: Color) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color)
    systemUiController.setNavigationBarColor(color)
}

@Composable
private fun Toolbar(
    place: String,
    placeVisible: Boolean,
    time: String,
    timeVisible: Boolean,
    temperature: String,
    temperatureVisible: Boolean,
    onMenuClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HamburgerButton(
                onClick = onMenuClicked,
                modifier = Modifier.size(42.dp)
            )
            Column(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                SlideUpAppearText(
                    text = place,
                    visible = placeVisible,
                    style = PrognozaTheme.typography.titleSmall
                )
                SlideUpAppearText(
                    text = time,
                    visible = timeVisible,
                    style = PrognozaTheme.typography.subtitleSmall
                )
            }
            SlideUpAppearText(
                text = temperature,
                visible = temperatureVisible,
                style = PrognozaTheme.typography.headlineSmall
            )
        }
        Divider(color = LocalContentColor.current)
    }
}

@Composable
private fun HamburgerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
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
}

@Composable
fun SlideUpAppearText(
    text: String,
    visible: Boolean,
    style: TextStyle = LocalTextStyle.current
) {
    val enter = fadeIn() + expandVertically(expandFrom = Alignment.Top)
    val exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
    AnimatedVisibility(
        visible = visible,
        enter = enter,
        exit = exit
    ) {
        Text(text = text, style = style)
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
                modifier = Modifier.weight(1f),
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

@Composable
fun rememberPlaceVisible(listState: LazyListState) = remember {
    derivedStateOf {
        if (listState.layoutInfo.visibleItemsInfo.isEmpty()) null
        else listState.layoutInfo.isKeyVisible(key = "place")
    }
}

@Composable
fun rememberTimeVisible(listState: LazyListState) = remember {
    derivedStateOf {
        if (listState.layoutInfo.visibleItemsInfo.isEmpty()) null
        else listState.layoutInfo.isKeyVisible(key = "time")
    }
}

@Composable
fun rememberTemperatureVisible(listState: LazyListState) = remember {
    derivedStateOf {
        if (listState.layoutInfo.visibleItemsInfo.isEmpty()) null
        else listState.layoutInfo.isKeyVisible(
            key = "temperature",
            visiblePercent = 50f
        )
    }
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
private fun LazyListLayoutInfo.isKeyVisible(key: String, visiblePercent: Float = 0f): Boolean =
    visibleItemsInfo.filter {
        val cutTop = max(0, viewportStartOffset - it.offset)
        val cutBottom = max(0, it.offset + it.size - viewportEndOffset)
        max(0f, 100f - (cutTop + cutBottom) * 100f / it.size) >= visiblePercent
    }.any { it.key == key }