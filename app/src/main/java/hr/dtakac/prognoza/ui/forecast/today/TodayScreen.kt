package hr.dtakac.prognoza.ui.forecast.today

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.forecast.TodayUi
import hr.dtakac.prognoza.presentation.forecast.DayHourUi
import hr.dtakac.prognoza.presentation.forecast.DayUi
import hr.dtakac.prognoza.ui.forecast.ForecastToolbar
import hr.dtakac.prognoza.ui.forecast.keyVisibilityPercent
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun TodayScreen(
    todayUi: TodayUi,
    comingUi: List<DayUi>,
    place: TextResource,
    surfaceColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    toolbarSurfaceColor: Color = Color.Unspecified,
    toolbarContentColor: Color = Color.Unspecified,
    onMenuClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column {
            var toolbarPlaceVisible by remember { mutableStateOf(false) }
            var toolbarDateTimeVisible by remember { mutableStateOf(false) }
            var toolbarTemperatureVisible by remember { mutableStateOf(false) }

            ForecastToolbar(
                backgroundColor = toolbarSurfaceColor,
                contentColor = toolbarContentColor,
                onMenuClick = onMenuClick
            ) {
                ToolbarContent(
                    place = place.asString(),
                    placeVisible = toolbarPlaceVisible,
                    dateTime = todayUi.date.asString(),
                    dateTimeVisible = toolbarDateTimeVisible,
                    temperature = todayUi.temperature.asString(),
                    temperatureVisible = toolbarTemperatureVisible
                )
            }

            val listState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceColor)
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
                        text = todayUi.date.asString(),
                        style = PrognozaTheme.typography.subtitleLarge
                    )
                }
                item(key = "temperature") {
                    AutoSizeText(
                        text = todayUi.temperature.asString(),
                        style = PrognozaTheme.typography.headlineLarge,
                        maxFontSize = PrognozaTheme.typography.headlineLarge.fontSize,
                        maxLines = 1
                    )
                }
                item {
                    DescriptionAndLowHighTemperature(
                        description = todayUi.description.asString(),
                        lowHighTemperature = todayUi.lowHighTemperature.asString(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    WindAndPrecipitation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 42.dp),
                        wind = todayUi.wind.asString(),
                        precipitation = todayUi.precipitation.asString()
                    )
                }
                item {
                    HourlyHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 42.dp, bottom = 16.dp)
                    )
                }
                items(todayUi.hourly) { hour ->
                    HourItem(
                        hour = hour,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )
                }
                item {
                    ComingHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp, top = 30.dp)
                    )
                }
                items(comingUi) { day ->
                    ComingItem(
                        day = day,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
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
                        toolbarPlaceVisible = placeVis == 0f
                        toolbarDateTimeVisible = dateTimeVis == 0f
                        toolbarTemperatureVisible = temperatureVis <= 50f
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
                modifier = Modifier.weight(2f),
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
            style = PrognozaTheme.typography.titleSmall,
        )
        Divider(
            color = LocalContentColor.current,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun ComingHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.coming),
            style = PrognozaTheme.typography.titleSmall,
        )
        Divider(
            color = LocalContentColor.current,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun RowScope.ToolbarContent(
    place: String,
    placeVisible: Boolean,
    dateTime: String,
    dateTimeVisible: Boolean,
    temperature: String,
    temperatureVisible: Boolean
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        SlideUpAppearText(
            text = place,
            visible = placeVisible,
            style = PrognozaTheme.typography.titleMedium
        )
        SlideUpAppearText(
            text = dateTime,
            visible = dateTimeVisible,
            style = PrognozaTheme.typography.subtitleMedium
        )
    }
    SlideUpAppearText(
        text = temperature,
        visible = temperatureVisible,
        style = PrognozaTheme.typography.headlineSmall
    )
}

@Composable
private fun SlideUpAppearText(
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
fun HourItem(
    hour: DayHourUi,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.body) {
            Text(
                modifier = Modifier.width(52.dp),
                text = hour.time.asString(),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
            )
            Text(
                modifier = Modifier.width(52.dp),
                text = hour.temperature.asString(),
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Image(
                painter = painterResource(id = hour.icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun ComingItem(
    day: DayUi,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.body) {
            Text(
                modifier = Modifier.weight(2f),
                text = day.date.asString(),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.weight(1f),
                text = day.precipitation.asString(),
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
            )
            Text(
                modifier = Modifier.weight(1f),
                text = day.lowHighTemperature.asString(),
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun TodayScreenPreview() {
    PrognozaTheme(description = ForecastDescription.Short.FAIR) {
        TodayScreen(
            fakeTodayUi(),
            fakeComingUi(),
            place = TextResource.fromText("Helsinki"),
            surfaceColor = PrognozaTheme.colors.surface,
            contentColor = PrognozaTheme.colors.onSurface
        )
    }
}

private fun fakeTodayUi(): TodayUi = TodayUi(
    date = TextResource.fromText("September 12"),
    temperature = TextResource.fromText("1°"),
    feelsLike = TextResource.fromText("Feels like 28°"),
    description = TextResource.fromText("Clear sky, sleet soon"),
    lowHighTemperature = TextResource.fromText("15°—7°"),
    wind = TextResource.fromText("Wind: 15 km/h"),
    precipitation = TextResource.fromText("Precipitation: 0 mm"),
    shortDescription = ForecastDescription.Short.FAIR,
    hourly = mutableListOf<DayHourUi>().apply {
        for (i in 1..15) {
            add(
                DayHourUi(
                    time = TextResource.fromText("14:00"),
                    temperature = TextResource.fromText("23°"),
                    precipitation = TextResource.fromText("1.99 mm"),
                    description = TextResource.fromText("Clear and some more text"),
                    icon = R.drawable.heavysleetshowersandthunder_night
                )
            )
        }
    }
)

private fun fakeComingUi(): List<DayUi> = listOf(
    DayUi(
        date = TextResource.fromText("Thu, Sep 13"),
        lowHighTemperature = TextResource.fromText("16—8"),
        precipitation = TextResource.fromText(""),
        icon = R.drawable.clearsky_day
    ),
    DayUi(
        date = TextResource.fromText("Fri, Sep 14"),
        lowHighTemperature = TextResource.fromText("18—8"),
        precipitation = TextResource.fromText("0.7 mm"),
        icon = R.drawable.rainshowers_day
    ),
    DayUi(
        date = TextResource.fromText("Sat, Sep 15"),
        lowHighTemperature = TextResource.fromText("21—5"),
        precipitation = TextResource.fromText(""),
        icon = R.drawable.cloudy
    ),
)