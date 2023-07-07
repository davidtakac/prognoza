package hr.dtakac.prognoza.ui.overview

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
    minimumTemperatureStartFraction: Float,
    maximumTemperatureEndFraction: Float,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
) {
    Card(
        shape = shape,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Text(text = day)
                Column {
                    Text(
                        // Empty pop acts as a placeholder
                        text = pop ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Image(
                        painter = painterResource(id = weatherIcon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Row(modifier = Modifier.weight(2f)) {
                Text(text = minimumTemperature)
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                            shape = RoundedCornerShape(2.dp)
                        ),
                ) {
                    Spacer(Modifier.fillMaxWidth(fraction = minimumTemperatureStartFraction))
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(2.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    ) {}
                    Spacer(Modifier.fillMaxWidth(fraction = maximumTemperatureEndFraction))
                }
                Text(text = maximumTemperature)
            }
        }
    }
}