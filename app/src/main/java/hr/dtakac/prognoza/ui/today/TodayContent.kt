package hr.dtakac.prognoza.ui.today

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.today.TodayHour
import hr.dtakac.prognoza.presentation.today.TodayUiState
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayContent(state: TodayUiState.Success) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CurrentConditionsCard(
                title = state.title,
                airTemperature = state.temperature,
                feelsLike = state.feelsLike,
                wind = state.wind,
                descriptionIcon = state.descriptionIcon,
                description = state.description
            )
            Spacer(modifier = Modifier.height(24.dp))
            RestOfDayCard(
                modifier = Modifier.weight(1f),
                dayPrecipitation = state.dayPrecipitation,
                lowTemperature = state.lowTemperature,
                highTemperature = state.highTemperature,
                hours = state.hours
            )
        }
    }
}

@Composable
private fun CurrentConditionsCard(
    title: TextResource,
    airTemperature: TextResource,
    feelsLike: TextResource,
    wind: TextResource,
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
                Column {
                    Text(
                        title.asString(),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        airTemperature.asString(),
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        feelsLike.asString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Image(
                    painter = painterResource(id = descriptionIcon),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                ConditionTextCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    wind.asString()
                )
                Spacer(Modifier.width(8.dp))
                ConditionTextCard(
                    modifier = Modifier
                        .fillMaxHeight(),
                    description.asString()
                )
            }
        }
    }
}

@Composable
fun ConditionTextCard(modifier: Modifier = Modifier, content: String) {
    Card(modifier) {
        Text(
            content,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium
        )
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
    dayPrecipitation: TextResource,
    lowTemperature: TextResource,
    highTemperature: TextResource,
    hours: List<TodayHour>
) {
    ElevatedCard(modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.rest_of_the_day),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                LowHighTemperatureCard(
                    modifier = Modifier.fillMaxHeight(),
                    lowTemperature = lowTemperature,
                    highTemperature = highTemperature
                )
                Spacer(modifier = Modifier.width(8.dp))
                ConditionTextCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    content = dayPrecipitation.asString()
                )
            }
        }
    }
}

@Composable
fun LowHighTemperatureCard(
    modifier: Modifier = Modifier,
    lowTemperature: TextResource,
    highTemperature: TextResource
) {
    Card(modifier) {
        val style = MaterialTheme.typography.bodyMedium
        val placeholderSize = style.fontSize
        val inlineContent = mapOf(
            "up" to InlineTextContent(
                placeholder = Placeholder(
                    width = placeholderSize,
                    height = placeholderSize,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_up),
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                    contentDescription = null
                )
            },
            "down" to InlineTextContent(
                placeholder = Placeholder(
                    width = placeholderSize,
                    height = placeholderSize,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                )
            ) {
                Image(
                    modifier = Modifier.rotate(180f),
                    painter = painterResource(id = R.drawable.ic_arrow_up),
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                    contentDescription = null
                )
            }
        )
        Text(
            modifier = Modifier.padding(12.dp),
            text = buildAnnotatedString {
                appendInlineContent("up", "[icon]")
                append(highTemperature.asString())
                appendInlineContent("down", "[icon]")
                append(lowTemperature.asString())
            },
            inlineContent = inlineContent,
            style = style
        )
    }
}



@Preview()
@Composable
private fun TodayContentPreviewDark() {
    PrognozaTheme(useDarkTheme = true) {
        TodayContentPreview()
    }
}

private fun fakeState(): TodayUiState.Success = TodayUiState.Success(
    title = TextResource.fromText("San Francisco, 13:00"),
    temperature = TextResource.fromText("23°"),
    feelsLike = TextResource.fromText("Feels like 28°"),
    wind = TextResource.fromText("Light air\nfrom northeast (2 m/s)"),
    descriptionIcon = R.drawable.clearsky_day,
    description = TextResource.fromText("Clear sky"),
    lowTemperature = TextResource.fromText("18°"),
    highTemperature = TextResource.fromText("30°"),
    dayPrecipitation = TextResource.fromText("Precipitation starts at 18:00"),
    hours = listOf(
        TodayHour(
            time = TextResource.fromText("14:00"),
            icon = R.drawable.clearsky_day,
            temperature = TextResource.fromText("23°"),
            precipitation = null
        ),
        TodayHour(
            time = TextResource.fromText("15:00"),
            icon = R.drawable.clearsky_day,
            temperature = TextResource.fromText("25°"),
            precipitation = null
        ),
        TodayHour(
            time = TextResource.fromText("16:00"),
            icon = R.drawable.partlycloudy_day,
            temperature = TextResource.fromText("26°"),
            precipitation = null
        ),
        TodayHour(
            time = TextResource.fromText("17:00"),
            icon = R.drawable.cloudy,
            temperature = TextResource.fromText("28°"),
            precipitation = null
        ),
        TodayHour(
            time = TextResource.fromText("18:00"),
            icon = R.drawable.heavyrain,
            temperature = TextResource.fromText("30°"),
            precipitation = TextResource.fromText("1.8 mm")
        )
    )
)