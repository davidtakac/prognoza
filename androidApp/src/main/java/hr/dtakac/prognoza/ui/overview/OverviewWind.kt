package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

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
      .then(modifier),
    contentAlignment = Alignment.Center
  ) {
    val color = MaterialTheme.colorScheme.onSurface
    val notchLength = 8.dp
    val density = LocalDensity.current
    Canvas(modifier = Modifier.fillMaxSize()) {
      val outerRadius = size.height / 2
      val innerRadius = outerRadius - density.run { notchLength.toPx() }
      for (i in 0..360 step 3) {
        val angle = (i * (PI / 180)).toFloat()
        val innerX = cos(angle) * innerRadius + center.x
        val outerX = cos(angle) * outerRadius + center.x
        val innerY = sin(angle) * innerRadius + center.y
        val outerY = sin(angle) * outerRadius + center.y
        drawLine(
          color = color,
          start = Offset(x = innerX, y = innerY),
          end = Offset(x = outerX, y = outerY),
          strokeWidth = if (i % 45 == 0) density.run { 1.dp.toPx() } else Stroke.HairlineWidth
        )
      }
    }
    Column(
      modifier = Modifier.fillMaxHeight().padding(vertical = notchLength),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(stringResource(id = R.string.north_label_short), style = MaterialTheme.typography.labelSmall)
      Text(stringResource(id = R.string.south_label_short), style = MaterialTheme.typography.labelSmall)
    }
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = notchLength + 2.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(stringResource(id = R.string.west_label_short), style = MaterialTheme.typography.labelSmall)
      Text(stringResource(id = R.string.east_label_short), style = MaterialTheme.typography.labelSmall)
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