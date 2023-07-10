package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OverviewHour(
  time: @Composable () -> Unit,
  icon: @Composable () -> Unit,
  value: @Composable () -> Unit,
  secondaryValue: (@Composable () -> Unit)?,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
        content = time
      )
      Box(
        modifier = Modifier
          .padding(top = 4.dp)
          .size(32.dp)
      ) { icon() }
      secondaryValue?.let {
        CompositionLocalProvider(
          LocalTextStyle provides MaterialTheme.typography.bodySmall,
          LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
          content = it
        )
      }
    }
    CompositionLocalProvider(
      LocalTextStyle provides MaterialTheme.typography.titleMedium,
      content = value
    )
  }
}