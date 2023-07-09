package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.ui.theme.AppTheme
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun SettingItem(
  name: String,
  value: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .clickable(onClick = onClick)
      .padding(horizontal = 24.dp, vertical = 16.dp)
      .fillMaxWidth()
  ) {
    Text(
      text = name,
      style = PrognozaTheme.typography.subtitleMedium
    )
    Text(
      text = value,
      style = PrognozaTheme.typography.body,
      color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
    )
  }
}

@Preview
@Composable
private fun SettingItemPreview() = AppTheme {
  SettingItem(
    name = "Temperature unit",
    value = "Degree Celsius",
    onClick = {}
  )
}