package hr.dtakac.prognoza.ui.overview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hr.dtakac.prognoza.R

@Composable
fun OverviewHumidity(
  humidity: String,
  dewPoint: String,
  modifier: Modifier = Modifier
) {
  OverviewDetail(
    label = { Text(stringResource(id = R.string.forecast_label_humidity)) },
    mainValue = { Text(humidity) },
    description = { Text(dewPoint) },
    modifier = modifier
  )
}