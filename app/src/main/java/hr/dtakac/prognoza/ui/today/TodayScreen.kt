package hr.dtakac.prognoza.ui.today

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.units.Temperature
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.today.TodayContent
import hr.dtakac.prognoza.presentation.today.TodayHour
import hr.dtakac.prognoza.presentation.today.TodayUiState
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun TodayScreen(state: TodayUiState) {
    if (state.content != null) {
        PrognozaTheme(temperature = state.content.temperatureValue) {
            Content(state.content)
        }
    }
}

@Composable
private fun Content(content: TodayContent) {
    CompositionLocalProvider(
        LocalContentColor provides PrognozaTheme.colors.onBackground,
        LocalTextStyle provides PrognozaTheme.typography.normal
    ) {
        val background by animateColorAsState(targetValue = PrognozaTheme.colors.background)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    text = content.placeName.asString(),
                    style = PrognozaTheme.typography.prominent,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = content.time.asString())
                ResponsiveText(
                    text = content.temperature.asString(),
                    style = PrognozaTheme.typography.prominent,
                    targetHeight = 250.sp,
                )
                Row(modifier = Modifier.fillMaxWidth(),) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = content.description.asString(),
                        style = PrognozaTheme.typography.prominent,
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = content.lowHighTemperature.asString(),
                        style = PrognozaTheme.typography.prominent,
                        textAlign = TextAlign.End
                    )
                }
                Spacer(modifier = Modifier.height(42.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = content.wind.asString(),
                        style = PrognozaTheme.typography.small,
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = content.precipitation.asString(),
                        style = PrognozaTheme.typography.small,
                        textAlign = TextAlign.End
                    )
                }
                Spacer(modifier = Modifier.height(42.dp))
                Text(
                    text = stringResource(id = R.string.hourly),
                    style = PrognozaTheme.typography.small,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = PrognozaTheme.colors.onBackground)
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(content.hours) { hour ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.width(52.dp),
                        text = hour.time.asString(),
                        style = PrognozaTheme.typography.small,
                        textAlign = TextAlign.Start,
                        maxLines = 1
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = hour.description.asString(),
                        style = PrognozaTheme.typography.small,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier.width(88.dp),
                        text = hour.precipitation.asString(),
                        style = PrognozaTheme.typography.small,
                        textAlign = TextAlign.End,
                        maxLines = 1
                    )
                    Text(
                        modifier = Modifier.width(52.dp),
                        text = hour.temperature.asString(),
                        style = PrognozaTheme.typography.small,
                        textAlign = TextAlign.End,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun TodayScreenPreview() {
    val state = TodayUiState().copy(
        content = fakeContent(),
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
    temperatureValue = Temperature(value = 70.0, unit = TemperatureUnit.F),
    placeName = TextResource.fromText("Helsinki"),
    time = TextResource.fromText("September 12"),
    temperature = TextResource.fromText("1°"),
    feelsLike = TextResource.fromText("Feels like 28°"),
    description = TextResource.fromText("Clear sky, sleet soon"),
    lowHighTemperature = TextResource.fromText("15°—7°"),
    wind = TextResource.fromText("Wind: 15 km/h"),
    precipitation = TextResource.fromText("Precipitation: 0 mm"),
    hours = listOf(
        TodayHour(
            time = TextResource.fromText("14:00"),
            temperature = TextResource.fromText("23°"),
            precipitation = TextResource.fromText("0 mm"),
            description = TextResource.fromText("Clear")
        ),
        TodayHour(
            time = TextResource.fromText("15:00"),
            temperature = TextResource.fromText("25°"),
            precipitation = TextResource.fromText("0 mm"),
            description = TextResource.fromText("Partly cloudy")
        ),
        TodayHour(
            time = TextResource.fromText("16:00"),
            temperature = TextResource.fromText("26°"),
            precipitation = TextResource.fromText("0 mm"),
            description = TextResource.fromText("Cloudy")
        ),
        TodayHour(
            time = TextResource.fromText("17:00"),
            temperature = TextResource.fromText("28°"),
            precipitation = TextResource.fromText("0 mm"),
            description = TextResource.fromText("Cloudy")
        ),
        TodayHour(
            time = TextResource.fromText("18:00"),
            temperature = TextResource.fromText("128°"),
            precipitation = TextResource.fromText("1.55 mm"),
            description = TextResource.fromText("Heavy rain")
        )
    )
)