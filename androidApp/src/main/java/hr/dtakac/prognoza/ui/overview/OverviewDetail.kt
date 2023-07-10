package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OverviewDetail(
  label: @Composable () -> Unit,
  mainValue: @Composable () -> Unit,
  description: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  supportingValue: (@Composable () -> Unit)? = null,
  supportingGraphic: (@Composable () -> Unit)? = null
) {
  Card(modifier) {
    Column(
      modifier = Modifier
        .padding(16.dp)
        .fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        CompositionLocalProvider(
          LocalTextStyle provides MaterialTheme.typography.titleSmall,
          content = label
        )
        CompositionLocalProvider(
          LocalTextStyle provides MaterialTheme.typography.headlineMedium,
          content = mainValue
        )
        supportingValue?.let {
          CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyMedium,
            content = it
          )
        }
        supportingGraphic?.let {
          Spacer(Modifier.height(8.dp))
          it()
        }
      }
      CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodyMedium,
        content = description
      )
    }
  }
}