package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R

@Composable
fun OverviewPrecipitation(
  amountInLastPeriod: String,
  hoursInLastPeriod: String,
  nextExpected: String,
  isSnow: Boolean,
  modifier: Modifier = Modifier
) {
  OverviewDetail(
    label = { Text(stringResource(id = if (isSnow) R.string.forecast_label_snow else R.string.forecast_label_rain)) },
    value = {
      Column {
        Text(text = amountInLastPeriod)
        Text(text = hoursInLastPeriod, style = MaterialTheme.typography.bodyLarge)
      }
    },
    description = { Text(text = nextExpected) },
    modifier = modifier
  )
}

@Composable
fun OverviewDetail(
  label: @Composable () -> Unit,
  value: @Composable () -> Unit,
  description: @Composable () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(modifier) {
    Column(
      modifier = Modifier
        .padding(16.dp)
        .fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.titleSmall, content = label)
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.headlineMedium, content = value)
      }
      CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium, content = description)
    }
  }
}