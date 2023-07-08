package hr.dtakac.prognoza.ui.overview

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun OverviewDay(
    day: String,
    pop: String?,
    @DrawableRes weatherIcon: Int,
    minimumTemperature: String,
    maximumTemperature: String,
    temperatureBarStartFraction: Float,
    temperatureBarEndFraction: Float,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
) {
    Card(
        shape = shape,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = day, Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    pop?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Image(
                        painter = painterResource(id = weatherIcon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = minimumTemperature, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                            shape = RoundedCornerShape(percent = 100)
                        )
                        .height(10.dp),
                ) {
                    Spacer(Modifier.fillMaxWidth(fraction = temperatureBarStartFraction))
                    Spacer(Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(percent = 100)
                        )
                    )
                    Spacer(Modifier.fillMaxWidth(fraction = temperatureBarEndFraction))
                }
                Text(text = maximumTemperature)
            }
        }
    }
}