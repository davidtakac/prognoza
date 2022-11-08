package hr.dtakac.prognoza.ui.forecast

import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.Mood
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
            title = state.forecast?.current?.place?.asString() ?: "",
            subtitle = state.forecast?.current?.date?.asString() ?: "",
            end = state.forecast?.current?.temperature?.asString() ?: "",
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
    // Horizontal padding isn't included in contentPadding because the click ripple on the Coming
    // day items looks better when it goes edge-to-edge
    val itemPadding = PaddingValues(horizontal = 24.dp)
    val hourItemDimensions = rememberHourItemDimensions(hours = forecast.today?.hourly ?: listOf())
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(vertical = 24.dp),
        modifier = modifier
    ) {
        item(key = "place") {
            Text(
                text = forecast.current.place.asString(),
                style = PrognozaTheme.typography.titleLarge,
                modifier = Modifier.padding(itemPadding)
            )
        }
        item(key = "place-time-spacer") {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item(key = "time") {
            Text(
                text = forecast.current.date.asString(),
                style = PrognozaTheme.typography.subtitleLarge,
                modifier = Modifier.padding(itemPadding)
            )
        }
        item(key = "temperature") {
            AutoSizeText(
                text = forecast.current.temperature.asString(),
                style = PrognozaTheme.typography.headlineLarge,
                maxFontSize = PrognozaTheme.typography.headlineLarge.fontSize,
                maxLines = 1,
                modifier = Modifier.padding(itemPadding)
            )
        }
        item(key = "description-and-precipitation") {
            DescriptionAndPrecipitation(
                description = forecast.current.description.asString(),
                icon = forecast.current.icon,
                precipitation = forecast.current.precipitation.asString(),
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
                feelsLike = forecast.current.feelsLike.asString(),
                wind = forecast.current.wind.asString()
            )
        }
        forecast.today?.hourly?.let { hours ->
            item(key = "hourly-header") {
                HourlyHeader(
                    lowHighTemperature = forecast.today.lowHighTemperature.asString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(itemPadding)
                        .padding(top = 42.dp, bottom = 16.dp)
                )
            }
            itemsIndexed(hours) { idx, hour ->
                HourItem(
                    hour = hour,
                    dimensions = hourItemDimensions,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(itemPadding)
                        .padding(bottom = if (idx == forecast.today.hourly.lastIndex) 0.dp else 12.dp),
                )
            }
        }
        forecast.coming?.let {
            item(key = "coming-header") {
                ComingHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(itemPadding)
                        .padding(top = 42.dp, bottom = 6.dp)
                )
            }
            items(it) { day ->
                var isExpanded by remember { mutableStateOf(false) }
                ComingItem(
                    day = day,
                    isExpanded = isExpanded,
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(itemPadding)
                        .padding(vertical = 10.dp)
                )
            }
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
private fun DescriptionAndPrecipitation(
    description: String,
    @DrawableRes
    icon: Int,
    precipitation: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val annotatedString = buildAnnotatedString {
            append("$description ")
            appendInlineContent(id = "icon")
        }
        val inlineContentMap = mapOf(
            "icon" to InlineTextContent(
                Placeholder(
                    36.sp,
                    36.sp,
                    PlaceholderVerticalAlign.TextCenter
                )
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = icon),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        )
        ProvideTextStyle(value = PrognozaTheme.typography.titleLarge) {
            Text(
                text = annotatedString,
                inlineContent = inlineContentMap,
                modifier = Modifier.weight(3f),
            )
            precipitation.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun WindAndFeelsLike(
    feelsLike: String,
    wind: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProvideTextStyle(PrognozaTheme.typography.body) {
            Text(text = wind, modifier = Modifier.weight(2f))
            Text(text = feelsLike, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
        }
    }
}

@Composable
private fun HourlyHeader(
    lowHighTemperature: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProvideTextStyle(PrognozaTheme.typography.titleSmall) {
                Text(stringResource(id = R.string.hourly))
                Text(lowHighTemperature)
            }
        }
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ComingItem(
    day: DayUi,
    isExpanded: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val expandTransition = updateTransition(label = "Expand coming item", targetState = isExpanded)
    val chevronRotation by expandTransition.animateFloat(label = "Rotate chevron") {
        if (it) 180f else 0f
    }
    Column(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            )
            .then(modifier)
    ) {
        ProvideTextStyle(PrognozaTheme.typography.body) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = day.date.asString(),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                day.precipitation.asString().takeIf { it.isNotBlank() }?.let {
                    Text(
                        modifier = Modifier.width(88.dp),
                        text = it,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
                    )
                }
                Text(
                    modifier = Modifier.width(88.dp),
                    text = day.lowHighTemperature.asString(),
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_expand_more),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer { rotationZ = chevronRotation },
                    colorFilter = ColorFilter.tint(LocalContentColor.current)
                )
            }
        }
        ProvideTextStyle(PrognozaTheme.typography.bodySmall) {
            expandTransition.AnimatedVisibility(
                visible = { targetIsExpanded -> targetIsExpanded },
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
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
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Image(
                                painter = rememberAsyncImagePainter(model = hour.icon),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = hour.time.asString(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 4.dp),
                                color = LocalContentColor.current
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
    PrognozaTheme(description = Mood.SUNNY) {
        Box(modifier = Modifier.background(PrognozaTheme.colors.surface1)) {
            DataList(
                forecast = ForecastUi(
                    current = fakeCurrentUi(),
                    today = fakeTodayUi(),
                    coming = fakeComingUi()
                )
            )
        }
    }
}

@Preview
@Composable
private fun TodayScreenJustCurrentPreview() {
    PrognozaTheme(description = Mood.SUNNY) {
        Box(modifier = Modifier.background(PrognozaTheme.colors.surface1)) {
            DataList(
                forecast = ForecastUi(
                    current = fakeCurrentUi(),
                    today = null,
                    coming = null
                )
            )
        }
    }
}

@Preview
@Composable
private fun TodayScreenCurrentPlusTodayPreview() {
    PrognozaTheme(description = Mood.SUNNY) {
        Box(modifier = Modifier.background(PrognozaTheme.colors.surface1)) {
            DataList(
                forecast = ForecastUi(
                    current = fakeCurrentUi(),
                    today = fakeTodayUi(),
                    coming = null
                )
            )
        }
    }
}

@Preview
@Composable
private fun TodayScreenCurrentPlusComingPreview() {
    PrognozaTheme(description = Mood.SUNNY) {
        Box(modifier = Modifier.background(PrognozaTheme.colors.surface1)) {
            DataList(
                forecast = ForecastUi(
                    current = fakeCurrentUi(),
                    today = null,
                    coming = fakeComingUi()
                )
            )
        }
    }
}

@Preview
@Composable
private fun ToolbarWithLoadingIndicatorPreview() {
    PrognozaTheme(description = Mood.SUNNY) {
        ToolbarWithLoadingIndicator(
            title = "Helsinki",
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

private fun fakeCurrentUi(): CurrentUi = CurrentUi(
    place = TextResource.fromText("Tenja"),
    date = TextResource.fromText("September 12"),
    temperature = TextResource.fromText("1°"),
    description = TextResource.fromText("Clear sky, sleet soon"),
    icon = R.drawable.clearsky_day,
    wind = TextResource.fromText("Wind: 15 km/h"),
    feelsLike = TextResource.fromText("Feels like: -21°"),
    mood = Mood.SUNNY,
    precipitation = TextResource.fromText("12 mm")
)

private fun fakeTodayUi(): TodayUi = TodayUi(
    lowHighTemperature = TextResource.fromText("135°—197°"),
    hourly = mutableListOf<DayHourUi>().apply {
        for (i in 1..4) {
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
        precipitation = TextResource.empty(),
        hours = mutableListOf<ComingHourUi>().apply {
            for (i in 1..12) {
                add(
                    ComingHourUi(
                        time = TextResource.fromText("$i:00"),
                        temperature = TextResource.fromText("20"),
                        icon = R.drawable.partlycloudy_day
                    )
                )
            }
        }
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
        precipitation = TextResource.empty(),
        hours = listOf()
    ),
)