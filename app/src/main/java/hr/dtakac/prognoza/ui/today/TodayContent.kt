package hr.dtakac.prognoza.ui.today

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
                precipitation = state.precipitation,
                descriptionIcon = state.descriptionIcon,
                description = state.description
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
    precipitation: TextResource,
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
                        .weight(1f)
                        .fillMaxHeight(),
                    description.asString()
                )
            }
        }
    }
}

@Composable
fun ConditionTextCard(modifier: Modifier, content: String) {
    Card(modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(content, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview
@Composable
private fun TodayContentPreview() {
    TodayContent(fakeState())
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
    precipitation = TextResource.fromText("Heavy rain at 18:00 (1.8 mm)"),
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