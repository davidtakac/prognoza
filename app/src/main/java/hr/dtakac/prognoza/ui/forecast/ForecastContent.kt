package hr.dtakac.prognoza.ui.forecast

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.forecast.*
import hr.dtakac.prognoza.ui.common.*
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun ForecastContent(
    state: ForecastState,
    onMenuClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrognozaTheme.colors.surface1)
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        var toolbarPlaceVisible by remember { mutableStateOf(false) }
        var toolbarDateVisible by remember { mutableStateOf(false) }
        var toolbarTemperatureVisible by remember { mutableStateOf(false) }

        ToolbarWithLoadingIndicator(
            title = state.forecast?.place?.asString() ?: "",
            subtitle = state.forecast?.today?.date?.asString() ?: "",
            end = state.forecast?.today?.temperature?.asString() ?: "",
            titleVisible = toolbarPlaceVisible,
            subtitleVisible = toolbarDateVisible,
            endVisible = toolbarTemperatureVisible,
            isLoading = state.isLoading,
            onMenuClick = onMenuClick
        )

        if (state.forecast == null) {
            if (state.error != null) {
                FullScreenError(
                    error = state.error.asString(),
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize()
                )
            }
        } else {
            Box {
                DataList(
                    forecast = state.forecast,
                    isPlaceVisible = { toolbarPlaceVisible = !it },
                    isDateVisible = { toolbarDateVisible = !it },
                    isTemperatureVisible = { toolbarTemperatureVisible = !it }
                )

                if (state.error != null) {
                    SnackBarError(
                        error = state.error.asString(),
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolbarWithLoadingIndicator(
    title: String,
    subtitle: String,
    end: String,
    titleVisible: Boolean,
    subtitleVisible: Boolean,
    endVisible: Boolean,
    isLoading: Boolean,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier) {
        PrognozaToolbar(
            title = { Text(title) },
            subtitle = { Text(subtitle) },
            end = { Text(end) },
            navigationIcon = {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = null
                    )
                }
            },
            titleVisible = titleVisible,
            subtitleVisible = subtitleVisible,
            endVisible = endVisible
        )

        ContentLoadingIndicatorHost(isLoading = isLoading) { isVisible ->
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = PrognozaTheme.colors.onSurface,
                    trackColor = Color.Transparent
                )
            }
        }
    }
}

@Composable
private fun FullScreenError(
    error: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = error,
            style = PrognozaTheme.typography.subtitleMedium,
            color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SnackBarError(
    error: String,
    modifier: Modifier = Modifier
) {
    val snackBarState = remember { PrognozaSnackBarState() }
    LaunchedEffect(error) {
        snackBarState.showSnackBar(error)
    }

    PrognozaSnackBar(
        modifier = modifier,
        state = snackBarState,
        backgroundColor = PrognozaTheme.colors.inverseSurface1,
        contentColor = PrognozaTheme.colors.onInverseSurface
    )
}

@Composable
private fun DataList(
    forecast: ForecastUi,
    isPlaceVisible: (Boolean) -> Unit = {},
    isDateVisible: (Boolean) -> Unit = {},
    isTemperatureVisible: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(24.dp),
        modifier = modifier
    ) {
        item(key = "place") {
            Text(
                text = forecast.place.asString(),
                style = PrognozaTheme.typography.titleLarge
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item(key = "time") {
            Text(
                text = forecast.today.date.asString(),
                style = PrognozaTheme.typography.subtitleLarge
            )
        }
        item(key = "temperature") {
            AutoSizeText(
                text = forecast.today.temperature.asString(),
                style = PrognozaTheme.typography.headlineLarge,
                maxFontSize = PrognozaTheme.typography.headlineLarge.fontSize,
                maxLines = 1
            )
        }
        item {
            DescriptionAndLowHighTemperature(
                description = forecast.today.description.asString(),
                lowHighTemperature = forecast.today.lowHighTemperature.asString(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            WindAndPrecipitation(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 42.dp),
                wind = forecast.today.wind.asString(),
                precipitation = forecast.today.precipitation.asString()
            )
        }
        item {
            Header(
                text = stringResource(id = R.string.hourly),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 42.dp, bottom = 16.dp)
            )
        }
        itemsIndexed(forecast.today.hourly) { idx, hour ->
            HourItem(
                hour = hour,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (idx == forecast.today.hourly.lastIndex) 0.dp else 12.dp)
            )
        }
        item {
            Header(
                text = stringResource(id = R.string.coming),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 42.dp, bottom = 6.dp)
            )
        }
        items(forecast.coming) { day ->
            var isExpanded by remember { mutableStateOf(false) }
            ComingItem(
                day = day,
                isExpanded = isExpanded,
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
            )
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .distinctUntilChanged()
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

@Composable
private fun DescriptionAndLowHighTemperature(
    description: String,
    lowHighTemperature: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        ProvideTextStyle(PrognozaTheme.typography.titleLarge) {
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
        ProvideTextStyle(PrognozaTheme.typography.body) {
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
private fun Header(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = text,
            style = PrognozaTheme.typography.titleSmall,
        )
        Divider(
            color = LocalContentColor.current,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun HourItem(
    hour: DayHourUi,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        ProvideTextStyle(PrognozaTheme.typography.body) {
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
private fun ComingItem(
    day: DayUi,
    isExpanded: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ProvideTextStyle(PrognozaTheme.typography.body) {
        Column(
            modifier = Modifier.clickable(
                onClick = onClick,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            ).then(modifier)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
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

            AnimatedVisibility(visible = isExpanded,) {
                LazyRow(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    itemsIndexed(day.hours) { idx, hour ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = when (idx) {
                                0 -> Modifier.padding(end = 8.dp)
                                day.hours.lastIndex -> Modifier.padding(start = 8.dp)
                                else -> Modifier.padding(horizontal = 8.dp)
                            }
                        ) {
                            Text(
                                text = hour.temperature.asString(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            Image(
                                painter = painterResource(id = hour.icon),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = hour.time.asString(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 2.dp),
                                color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TodayScreenPreview() {
    PrognozaTheme(description = ForecastDescription.Short.FAIR) {
        DataList(
            forecast = ForecastUi(
                place = TextResource.fromText("Helsinki"),
                today = fakeTodayUi(),
                coming = fakeComingUi()
            )
        )
    }
}

@Preview
@Composable
private fun ToolbarWithLoadingIndicatorPreview() {
    PrognozaTheme(description = ForecastDescription.Short.FAIR) {
        ToolbarWithLoadingIndicator(
            title = "Tenja",
            subtitle = "September 29",
            end = "23",
            titleVisible = true,
            subtitleVisible = true,
            endVisible = true,
            isLoading = true,
            onMenuClick = {}
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
                    icon = if (i % 2 == 0) R.drawable.heavysleetshowersandthunder_night else R.drawable.ic_question_mark
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
        hours = listOf()
    ),
    DayUi(
        date = TextResource.fromText("Fri, Sep 14"),
        lowHighTemperature = TextResource.fromText("18—8"),
        precipitation = TextResource.fromText("0.7 mm"),
        hours = listOf()
    ),
    DayUi(
        date = TextResource.fromText("Sat, Sep 15"),
        lowHighTemperature = TextResource.fromText("21—5"),
        precipitation = TextResource.fromText(""),
        hours = listOf()
    ),
)