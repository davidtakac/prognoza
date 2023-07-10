package hr.dtakac.prognoza.ui.overview

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

private val temperatureBarHeight = 6.dp

@Composable
fun OverviewDay(
  day: String,
  pop: String?,
  @DrawableRes weatherIcon: Int,
  minimumTemperature: String,
  maximumTemperature: String,
  temperatureBarStartFraction: Float,
  temperatureBarEndFraction: Float,
  currentTemperatureCenterFraction: Float?,
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(4.dp),
) {
  Card(
    shape = shape,
    modifier = modifier,
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = day, Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
          Image(
            painter = painterResource(id = weatherIcon),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
          )
          pop?.let {
            Text(
              text = it,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(text = minimumTemperature, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Box(
          modifier = Modifier
            .weight(1f)
            .height(temperatureBarHeight)
        ) {
          Row(
            modifier = Modifier
              .background(
                color = Color.Black,
                shape = RoundedCornerShape(percent = 100)
              )
              .fillMaxSize()
          ) {
            Spacer(Modifier.fillMaxWidth(fraction = temperatureBarStartFraction))
            Spacer(
              Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                  color = MaterialTheme.colorScheme.onSurface,
                  shape = RoundedCornerShape(percent = 100)
                )
            )
            Spacer(Modifier.fillMaxWidth(fraction = temperatureBarEndFraction))
          }
          currentTemperatureCenterFraction?.let {
            Row(modifier = Modifier.fillMaxSize()) {
              Spacer(Modifier.fillMaxWidth(fraction = it))
              Box(
                modifier = Modifier
                  .size(temperatureBarHeight)
                  // Accounts for the fact that the current temperature fraction refers to
                  // the center of the circle, not the start
                  .offset(x = -(temperatureBarHeight / 2))
                  .border(
                    width = 1.dp,
                    shape = CircleShape,
                    color = Color.Black
                  )
              ) {}
            }
          }
        }
        Text(text = maximumTemperature)
      }
    }
  }
}