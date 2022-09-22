package hr.dtakac.prognoza.ui.forecast.coming

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.presentation.forecast.DayHourUi
import hr.dtakac.prognoza.presentation.forecast.DayUi
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.ui.forecast.HourItem
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComingScreen(
    state: List<DayUi>,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    onBackgroundColor: Color = Color.Unspecified,
) {
    CompositionLocalProvider(LocalContentColor provides onBackgroundColor) {
        Box(modifier = modifier) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(horizontal = 24.dp)
            ) {
                state.forEach { dayUi ->
                    stickyHeader {
                        Column(modifier = Modifier.background(backgroundColor)) {
                            DateAndLowHighTemperature(
                                date = dayUi.date.asString(),
                                lowHighTemperature = dayUi.lowHighTemperature.asString(),
                                modifier = Modifier.padding(top = 16.dp)
                            )
                            Divider(
                                color = LocalContentColor.current,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    items(dayUi.hourly) { hour ->
                        HourItem(
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
}

@Composable
private fun DateAndLowHighTemperature(
    date: String,
    lowHighTemperature: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier.weight(1f),
            text = date,
            style = PrognozaTheme.typography.subtitleSmall
        )
        Text(
            modifier = Modifier.weight(1f),
            text = lowHighTemperature,
            textAlign = TextAlign.End,
            style = PrognozaTheme.typography.titleSmall
        )
    }
}

@Preview
@Composable
fun ComingScreenPreview() {
    PrognozaTheme(description = ForecastDescription.Short.CLOUDY) {
        ComingScreen(state = fakeState())
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
                        description = TextResource.fromText("Partly cloudy")
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
                        description = TextResource.fromText("Cloudy")
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
                        description = TextResource.fromText("Clear")
                    )
                )
            }
        }
    )
)