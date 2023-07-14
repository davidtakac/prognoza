package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R

@Composable
fun OverviewWind(
  speed: String,
  maximumGust: String,
  windDirection: Float,
  modifier: Modifier = Modifier
) {
  OverviewDetail(
    label = { Text(stringResource(id = R.string.forecast_label_wind)) },
    supportingGraphic = {
      Compass(
        speed = speed,
        arrowAngle = windDirection,
        modifier = Modifier.fillMaxSize()
      )
    },
    description = { Text(maximumGust) },
    modifier = modifier
  )
}

@Composable
private fun Compass(
  speed: String,
  arrowAngle: Float,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = Modifier
      .aspectRatio(1f)
      .border(
        width = 2.dp,
        color = MaterialTheme.colorScheme.onSurface,
        shape = CircleShape
      )
      .then(modifier),
    contentAlignment = Alignment.Center
  ) {
    Column(
      modifier = Modifier.fillMaxHeight(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(stringResource(id = R.string.north_label_short))
      Text(stringResource(id = R.string.south_label_short))
    }
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(stringResource(id = R.string.west_label_short))
      Text(stringResource(id = R.string.east_label_short))
    }
    Box(
      modifier = Modifier
        .fillMaxHeight()
        .rotate(arrowAngle)
    ) {
      Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(12.dp)
            .background(
              color = MaterialTheme.colorScheme.onSurface,
              shape = CircleShape
            )
        )
        Box(
          modifier = Modifier
            .weight(1f)
            .width(2.dp)
            .background(MaterialTheme.colorScheme.onSurface)
        )
      }
    }
    Text(text = speed.replace(" ", "\n"), textAlign = TextAlign.Center)
  }
}