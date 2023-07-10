package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R

@Composable
fun OverviewSunset(time: String, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Text(text = time, style = MaterialTheme.typography.bodySmall)
    Icon(
      imageVector = Icons.Default.ArrowForward,
      contentDescription = null,
      modifier = Modifier
        .size(32.dp)
        .rotate(90f)
    )
    Text(
      text = stringResource(id = R.string.forecast_label_sunset),
      style = MaterialTheme.typography.titleMedium
    )
  }
}