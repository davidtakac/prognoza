package hr.dtakac.prognoza.ui.overview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hr.dtakac.prognoza.R

@Composable
fun OverviewFeelsLike(
  value: String,
  description: String,
  modifier: Modifier = Modifier
) {
  OverviewDetail(
    label = { Text(text = stringResource(id = R.string.forecast_label_feels_like)) },
    mainValue = { Text(value) },
    description = { Text(description) },
    modifier = modifier
  )
}