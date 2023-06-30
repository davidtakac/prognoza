package hr.dtakac.prognoza.ui.overview

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R

@Composable
fun OverviewNow(
    temperature: String,
    maximumTemperature: String,
    minimumTemperature: String,
    @DrawableRes weatherIcon: Int,
    weatherDescription: String,
    feelsLikeTemperature: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.forecast_label_now),
            style = MaterialTheme.typography.bodyLarge
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(horizontalAlignment = Alignment.Start) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = temperature,
                        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.SemiBold),
                    )
                    Image(
                        painter = painterResource(id = weatherIcon),
                        contentDescription = null,
                        // TODO: figure out how to line the bottoms of temp and weather icon precisely.
                        //  The line height of the temperature text is too high, so they're out of alignment.
                        modifier = Modifier.padding(bottom = 8.dp).size(42.dp).align(Alignment.Bottom),
                    )
                }
                Text(
                    text = stringResource(
                        id = R.string.forecast_label_high_low,
                        maximumTemperature,
                        minimumTemperature
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = weatherDescription,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = R.string.forecast_label_feels_like, feelsLikeTemperature),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}