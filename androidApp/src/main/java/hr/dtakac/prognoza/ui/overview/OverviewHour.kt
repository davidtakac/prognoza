package hr.dtakac.prognoza.ui.overview

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R

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

@Composable
fun OverviewSunsetHour(time: String, modifier: Modifier = Modifier) {
  OverviewHour(
    time = { Text(time) },
    icon = {
      Icon(
        imageVector = Icons.Default.ArrowForward,
        contentDescription = null,
        modifier = Modifier.fillMaxSize().rotate(90f)
      )
    },
    value = { Text(stringResource(id = R.string.forecast_label_sunset)) },
    secondaryValue = null,
    modifier = modifier
  )
}

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

@Composable
fun OverviewHour(
  time: @Composable () -> Unit,
  icon: @Composable () -> Unit,
  value: @Composable () -> Unit,
  secondaryValue: (@Composable () -> Unit)?,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodySmall,
        content = time
      )
      Box(
        modifier = Modifier
          .padding(top = 4.dp)
          .size(32.dp)
      ) { icon() }
      secondaryValue?.let {
        CompositionLocalProvider(
          LocalTextStyle provides MaterialTheme.typography.bodySmall,
          LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
          content = it
        )
      }
    }
    CompositionLocalProvider(
      LocalTextStyle provides MaterialTheme.typography.titleMedium,
      content = value
    )
  }
}