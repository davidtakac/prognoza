package hr.dtakac.prognoza.ui.forecast.coming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import hr.dtakac.prognoza.ui.forecast.HourItem
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun ComingScreen(
    place: TextResource,
    state: List<DayUi>,
    surfaceColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceColor)
                .padding(horizontal = 24.dp)
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
                item(key = "time-and-low-high-$index") {
                    if (index != 0) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    DateAndLowHighTemperature(
                        date = dayUi.date.asString(),
                        lowHighTemperature = dayUi.lowHighTemperature.asString()
                    )
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