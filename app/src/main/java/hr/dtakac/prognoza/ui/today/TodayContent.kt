package hr.dtakac.prognoza.ui.today

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.today.TodayHour
import hr.dtakac.prognoza.presentation.today.TodayUiState
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayContent(state: TodayUiState.Success) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(state.placeName.asString())
                }
            )
        },
        content = {
            Column(modifier = Modifier
                .padding(it)
                .padding(16.dp)) {
                CurrentConditionsCard(
                    title = state.time,
                    airTemperature = state.temperature,
                    feelsLike = state.feelsLike,
                    descriptionIcon = state.descriptionIcon,
                    description = state.currentDescription
                )
                Spacer(modifier = Modifier.height(16.dp))
                RestOfDayCard(
                    modifier = Modifier.weight(1f),
                    description = state.restOfDayDescription,
                    hours = state.hours
                )
            }
        }
    )
}

@Composable
private fun CurrentConditionsCard(
    title: TextResource,
    airTemperature: TextResource,
    feelsLike: TextResource,
    @DrawableRes
    descriptionIcon: Int,
    description: TextResource
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(2f)) {
                    Text(
                        title.asString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.alpha(0.6f)
                    )
                    Text(
                        airTemperature.asString(),
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        feelsLike.asString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(description.asString(), style = MaterialTheme.typography.bodyLarge)
                }
                Image(
                    painter = painterResource(id = descriptionIcon),
                    contentDescription = null,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun TodayContentPreview() {
    TodayContent(fakeState())
}

@Composable
private fun RestOfDayCard(
    modifier: Modifier,
    description: TextResource,
    hours: List<TodayHour>
) {
    ElevatedCard(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.rest_of_the_day),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.alpha(0.6f)
            )
            Spacer(Modifier.height(8.dp))
            Text(description.asString(), style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(hours) { hour ->
                    HourRow(hour)
                }
            }
        }
    }
}

@Composable
private fun HourRow(hour: TodayHour) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium) {
            Text(hour.time.asString(), modifier = Modifier.weight(1f))
            Text(hour.temperature.asString(), modifier = Modifier.weight(1f))
            Text(
                hour.precipitation?.asString() ?: "",
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.primary
            )
            WindWithRotatedDirectionIcon(
                modifier = Modifier.weight(1f),
                wind = hour.wind,
                windFromDirection = hour.windIconRotation
            )
            Image(
                painter = painterResource(id = hour.icon),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
private fun WindWithRotatedDirectionIcon(modifier: Modifier, wind: TextResource, windFromDirection: Float) {
    val style = MaterialTheme.typography.bodyMedium
    val placeholderSize = style.fontSize
    val inlineContent = mapOf(
        "icon" to InlineTextContent(
            placeholder = Placeholder(
                width = placeholderSize,
                height = placeholderSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_north),
                colorFilter = ColorFilter.tint(LocalContentColor.current),
                contentDescription = null,
                modifier = Modifier.rotate(windFromDirection)
            )
        },
    )
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            append("${wind.asString()} ")
            appendInlineContent("icon", "[icon]")
        },
        inlineContent = inlineContent,
        style = style
    )
}

@Preview()
@Composable
private fun TodayContentPreviewDark() {
    PrognozaTheme(useDarkTheme = true) {
        TodayContentPreview()
    }
}

private fun fakeState(): TodayUiState.Success = TodayUiState.Success(
    placeName = TextResource.fromText("Tenja"),
    time = TextResource.fromText("September 12, 13:00"),
    temperature = TextResource.fromText("23°"),
    feelsLike = TextResource.fromText("Feels like 28°"),
    descriptionIcon = R.drawable.clearsky_day,
    currentDescription = TextResource.fromText("Clear sky, light breeze from southeast (2 m/s)"),
    restOfDayDescription = TextResource.fromText("High 22, low 18. Heavy rain at 18:00"),
    hours = listOf(
        TodayHour(
            time = TextResource.fromText("14:00"),
            icon = R.drawable.clearsky_day,
            temperature = TextResource.fromText("23°"),
            precipitation = null,
            wind = TextResource.fromText("2 m/s"),
            windIconRotation = 90f
        ),
        TodayHour(
            time = TextResource.fromText("15:00"),
            icon = R.drawable.clearsky_day,
            temperature = TextResource.fromText("25°"),
            precipitation = null,
            wind = TextResource.fromText("2 m/s"),
            windIconRotation = 90f
        ),
        TodayHour(
            time = TextResource.fromText("16:00"),
            icon = R.drawable.partlycloudy_day,
            temperature = TextResource.fromText("26°"),
            precipitation = null,
            wind = TextResource.fromText("2 m/s"),
            windIconRotation = 90f
        ),
        TodayHour(
            time = TextResource.fromText("17:00"),
            icon = R.drawable.cloudy,
            temperature = TextResource.fromText("28°"),
            precipitation = null,
            wind = TextResource.fromText("2 m/s"),
            windIconRotation = 90f
        ),
        TodayHour(
            time = TextResource.fromText("18:00"),
            icon = R.drawable.heavyrain,
            temperature = TextResource.fromText("30°"),
            precipitation = TextResource.fromText("1.8 mm"),
            wind = TextResource.fromText("2 m/s"),
            windIconRotation = 90f
        )
    )
)