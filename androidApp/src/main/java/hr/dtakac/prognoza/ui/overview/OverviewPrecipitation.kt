package hr.dtakac.prognoza.ui.overview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    mainValue = { Text(text = amountInLastPeriod) },
    supportingValue = { Text(text = hoursInLastPeriod) },
    description = { Text(text = nextExpected) },
    modifier = modifier
  )
}