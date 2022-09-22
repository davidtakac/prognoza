package hr.dtakac.prognoza.ui.forecast.coming

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.presentation.forecast.DayHourUi
import hr.dtakac.prognoza.presentation.forecast.DayUi
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.ui.forecast.ForecastToolbar
import hr.dtakac.prognoza.ui.forecast.HourItem
import hr.dtakac.prognoza.ui.forecast.keyVisibilityPercent
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun ComingScreen(
    place: TextResource,
    state: List<DayUi>,
    surfaceColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    toolbarSurfaceColor: Color = Color.Unspecified,
    toolbarContentColor: Color = Color.Unspecified,
    onMenuClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column {
            val toolbarData = state.map { dayUi ->
                ToolbarDatum(
                    date = dayUi.date.asString(),
                    low = dayUi.lowTemperature.asString(),
                    high = dayUi.highTemperature.asString()
                )
            }
            var toolbarPlaceVisible by remember { mutableStateOf(false) }
            var indexOfVisibleToolbarDatum by remember { mutableStateOf(-1) }

            ForecastToolbar(
                backgroundColor = toolbarSurfaceColor,
                contentColor = toolbarContentColor,
                onMenuClick = onMenuClick
            ) {
                ToolbarContent(
                    place = place.asString(),
                    placeVisible = toolbarPlaceVisible,
                    datum = toolbarData,
                    indexOfVisibleDatum = indexOfVisibleToolbarDatum
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
                state.forEachIndexed { index, dayUi ->
                    if (index != 0) {
                        item { Spacer(modifier = Modifier.height(24.dp)) }
                    }
                    item(key = index) {
                        DateAndLowHighTemperature(
                            date = dayUi.date.asString(),
                            lowHighTemperature = dayUi.lowHighTemperature.asString()
                        )
                    }
                    item {
                        Divider(
                            modifier = Modifier.padding(vertical = 20.dp),
                            color = LocalContentColor.current
                        )
                    }
                    itemsIndexed(dayUi.hourly) { itemIndex, hour ->
                        HourItem(
                            hour = hour,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = if (itemIndex == dayUi.hourly.lastIndex) 0.dp else 12.dp)
                        )
                    }
                }
            }

            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo }
                    .distinctUntilChanged()
                    .map { layoutInfo ->
                        val placeVis = layoutInfo.keyVisibilityPercent("place")
                        Pair(placeVis, -1)
                    }
                    .distinctUntilChanged()
                    .collect { (placeVis, toolbarDatumIndex) ->
                        toolbarPlaceVisible = placeVis == 0f
                        //indexOfVisibleToolbarDatum = toolbarDatumIndex
                    }
            }
        }
    }
}

@Composable
private fun DateAndLowHighTemperature(
    date: String,
    lowHighTemperature: String
) {
    Row {
        Text(
            modifier = Modifier.weight(2f),
            text = date,
            style = PrognozaTheme.typography.subtitleLarge
        )
        Text(
            modifier = Modifier.weight(1f),
            text = lowHighTemperature,
            textAlign = TextAlign.End,
            style = PrognozaTheme.typography.titleLarge
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun RowScope.ToolbarContent(
    place: String,
    placeVisible: Boolean,
    datum: List<ToolbarDatum>,
    indexOfVisibleDatum: Int
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = placeVisible,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            Text(
                text = place,
                style = PrognozaTheme.typography.titleSmall
            )
        }
        AnimatedContent(
            targetState = indexOfVisibleDatum,
            transitionSpec = {
                if (targetState > initialState) {
                    // If the target number is larger, it slides up and fades in
                    // while the initial (smaller) number slides up and fades out.
                    slideInVertically() + fadeIn() with
                            slideOutVertically() + fadeOut()
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    slideInVertically() + fadeIn() with
                            slideOutVertically() + fadeOut()
                }.using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = false)
                )
            }
        ) { targetIndex ->
            if (targetIndex >= 0) {
                Text(
                    text = datum[targetIndex].date,
                    style = PrognozaTheme.typography.subtitleSmall
                )
            }
        }

    }
    /*Column(horizontalAlignment = Alignment.End) {
        Text("16°", style = PrognozaTheme.typography.titleSmall)
        Text(
            text = "8°",
            style = PrognozaTheme.typography.titleSmall,
            color = LocalContentColor.current.copy(alpha = 0.6f)
        )
    }*/
}

data class ToolbarDatum(
    val date: String,
    val low: String,
    val high: String
)

@Preview
@Composable
fun ComingScreenPreview() {
    PrognozaTheme(description = ForecastDescription.Short.CLOUDY) {
        ComingScreen(
            state = fakeState(),
            place = TextResource.fromText("Tenja"),
            surfaceColor = PrognozaTheme.colors.surface,
            contentColor = PrognozaTheme.colors.onSurface
        )
    }
}

private fun fakeState() = listOf(
    DayUi(
        date = TextResource.fromText("September 22"),
        lowHighTemperature = TextResource.fromText("16—8"),
        lowTemperature = TextResource.fromText("18"),
        highTemperature = TextResource.fromText("19"),
        hourly = mutableListOf<DayHourUi>().apply {
            for (i in 0..20) {
                add(
                    DayHourUi(
                        time = TextResource.fromText("7:00"),
                        temperature = TextResource.fromText("16"),
                        precipitation = TextResource.fromText("0.22 mm"),
                        description = TextResource.fromText("Partly cloudy"),
                        icon = R.drawable.partlycloudy_day
                    )
                )
            }
        }
    ),
    DayUi(
        date = TextResource.fromText("September 23"),
        lowHighTemperature = TextResource.fromText("23—9"),
        lowTemperature = TextResource.fromText("18"),
        highTemperature = TextResource.fromText("19"),
        hourly = mutableListOf<DayHourUi>().apply {
            for (i in 0..20) {
                add(
                    DayHourUi(
                        time = TextResource.fromText("9:00"),
                        temperature = TextResource.fromText("22"),
                        precipitation = TextResource.fromText("1.88 mm"),
                        description = TextResource.fromText("Cloudy"),
                        icon = R.drawable.cloudy
                    )
                )
            }
        }
    ),
    DayUi(
        date = TextResource.fromText("September 24"),
        lowHighTemperature = TextResource.fromText("19—18"),
        lowTemperature = TextResource.fromText("18"),
        highTemperature = TextResource.fromText("19"),
        hourly = mutableListOf<DayHourUi>().apply {
            for (i in 0..20) {
                add(
                    DayHourUi(
                        time = TextResource.fromText("8:00"),
                        temperature = TextResource.fromText("18"),
                        precipitation = TextResource.fromText(""),
                        description = TextResource.fromText("Clear"),
                        icon = R.drawable.clearsky_day
                    )
                )
            }
        }
    )
)