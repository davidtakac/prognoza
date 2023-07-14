package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
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
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Compass(
          speed = speed,
          arrowAngle = windDirection,
        )
      }
    },
    description = { Text(maximumGust) },
    modifier = modifier
  )
}

private val notchLength = 8.dp
private val northArrowHeight = notchLength
private val northArrowWidth = notchLength + 2.dp
private val arrowTipHeight = notchLength + 4.dp
private val arrowTipWidth = notchLength + 4.dp
private val arrowShaftWidth = 2.dp
private val arrowRootHeight = notchLength
private val arrowRootStrokeWidth = 2.dp

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
      val northArrowHeightPx = density.run { northArrowHeight.toPx() }
      val northArrowWidthPx = density.run { northArrowWidth.toPx() }
      val northArrowPath = Path().apply {
        moveTo(x = size.width / 2, y = 0f)
        lineTo(x = (size.width / 2) - (northArrowWidthPx / 2), y = northArrowHeightPx)
        lineTo(x = (size.width / 2) + (northArrowWidthPx / 2), y = northArrowHeightPx)
        lineTo(x = size.width / 2, y = 0f)
      }
      drawPath(northArrowPath, color)
    }
    Column(
      modifier = Modifier
        .fillMaxHeight()
        .padding(vertical = notchLength),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(stringResource(id = R.string.north_label_short), style = MaterialTheme.typography.labelSmall)
      Text(stringResource(id = R.string.south_label_short), style = MaterialTheme.typography.labelSmall)
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = notchLength + 2.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(stringResource(id = R.string.west_label_short), style = MaterialTheme.typography.labelSmall)
      Text(stringResource(id = R.string.east_label_short), style = MaterialTheme.typography.labelSmall)
    }
    Canvas(
      modifier = Modifier
        .fillMaxHeight()
        .rotate(arrowAngle)
    ) {
      val arrowTipHeightPx = density.run { arrowTipHeight.toPx() }
      val arrowTipWidthPx = density.run { arrowTipWidth.toPx() }
      val arrowRootHeightPx = density.run { arrowRootHeight.toPx() }
      val arrowShaftWidthPx = density.run { arrowShaftWidth.toPx() }
      val arrowRootStrokeWidthPx = density.run { arrowRootStrokeWidth.toPx() }
      val tipPath = Path().apply {
        moveTo(x = size.width / 2, y = 0f)
        lineTo(x = - (arrowTipWidthPx / 2), y = arrowTipHeightPx)
        lineTo(x = size.width / 2, y = arrowTipHeightPx * 0.7f)
        lineTo(x = arrowTipWidthPx / 2, y = arrowTipHeightPx)
        lineTo(x = size.width / 2, y = 0f)
      }
      drawPath(path = tipPath, color = color)
      drawLine(
        color = color,
        start = Offset(0f, 0f),
        end = Offset(x = size.width / 2, y = size.height - arrowRootHeightPx),
        strokeWidth = arrowShaftWidthPx
      )
      drawCircle(
        color = color,
        radius = arrowRootHeightPx / 2,
        center = Offset(
          x = size.width / 2,
          y = size.height - (arrowRootHeightPx / 2),
        ),
        style = Stroke(width = arrowRootStrokeWidthPx)
      )
    }
    Surface(
      color = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
      shape = CircleShape,
      modifier = Modifier
        .fillMaxWidth(0.4f)
        .aspectRatio(1f)
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        val (value, unit) = speed.split(" ")
        Text(text = value, style = MaterialTheme.typography.titleSmall)
        Text(text = unit, style = MaterialTheme.typography.labelSmall)
      }
    }
  }
}