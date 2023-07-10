package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import hr.dtakac.prognoza.R

@Composable
fun OverviewSunriseHour(time: String, modifier: Modifier = Modifier) {
  OverviewHour(
    time = { Text(time) },
    icon = {
      Icon(
        imageVector = Icons.Default.ArrowForward,
        contentDescription = null,
        modifier = Modifier.fillMaxSize().rotate(-90f)
      )
    },
    value = { Text(stringResource(id = R.string.forecast_label_sunrise)) },
    secondaryValue = null,
    modifier = modifier
  )
}