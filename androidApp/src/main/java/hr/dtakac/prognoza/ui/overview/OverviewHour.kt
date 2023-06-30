package hr.dtakac.prognoza.ui.overview

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun OverviewHour(
    temperature: String,
    pop: String?,
    @DrawableRes weatherIcon: Int,
    time: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = temperature, style = MaterialTheme.typography.titleMedium)
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
        Text(text = time, style = MaterialTheme.typography.bodySmall)
    }
}

