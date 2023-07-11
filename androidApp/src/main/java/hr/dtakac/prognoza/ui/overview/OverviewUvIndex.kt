package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R

private val colorStops = arrayOf(
  0.18f to Color(0xFF299501),
  0.45f to Color(0xFFF7E401),
  0.64f to Color(0xFFF95901),
  0.91f to Color(0xFFD90011),
  1f to Color(0xFF6C49C9)
)

private val barHeight = 6.dp

@Composable
fun OverviewUvIndex(
  value: String,
  level: String,
  valueCenterFraction: Float,
  recommendations: String,
  modifier: Modifier = Modifier
) {
  OverviewDetail(
    label = { Text(text = stringResource(id = R.string.forecast_label_uv_index)) },
    mainValue = { Text(text = value) },
    supportingValue = { Text(text = level) },
    supportingGraphic = {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(barHeight)
          .background(
            brush = Brush.horizontalGradient(*colorStops),
            shape = RoundedCornerShape(100f)
          )
      ) {
        Spacer(modifier = Modifier.fillMaxWidth(valueCenterFraction))
        Box(
          modifier = Modifier
            .size(6.dp)
            .offset(x = -(barHeight / 2))
            .border(width = 1.dp, color = Color.Black, shape = CircleShape)
        )
      }
    },
    description = { Text(recommendations) },
    modifier = modifier
  )
}