package hr.dtakac.prognoza.ui.overview

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun OverviewWeatherHour(
  temperature: String,
  pop: String?,
  @DrawableRes weatherIcon: Int,
  time: String,
  modifier: Modifier = Modifier
) {
  OverviewHour(
    time = { Text(time) },
    icon = {
      Image(
        painter = painterResource(id = weatherIcon),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
      )
    },
    value = { Text(temperature) },
    secondaryValue = pop?.let { @Composable { Text(it) } },
    modifier = modifier
  )
}