package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OverviewDetailsRow(
  first: @Composable () -> Unit,
  second: @Composable () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
    Box(modifier = Modifier.weight(1f).aspectRatio(1f)) { first() }
    Box(modifier = Modifier.weight(1f).aspectRatio(1f)) { second() }
  }
}