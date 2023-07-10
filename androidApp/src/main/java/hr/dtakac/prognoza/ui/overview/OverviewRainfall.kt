package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OverviewRainfall(
  lastAmount: String,
  lastAmountTimeframe: String,
  nextExpected: String,
  modifier: Modifier = Modifier
) {
  Card(modifier) {
    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
      Column {
        Text(text = lastAmount, style = MaterialTheme.typography.displaySmall)
        Text(text = lastAmountTimeframe, style = MaterialTheme.typography.bodyLarge)
      }
      Text(text = nextExpected, style = MaterialTheme.typography.bodyMedium)
    }
  }
}