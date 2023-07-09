package hr.dtakac.prognoza.ui.places

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun PlacesEmpty(
  message: String,
  modifier: Modifier = Modifier
) {
  Text(
    text = message,
    style = PrognozaTheme.typography.subtitleMedium,
    color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium),
    modifier = modifier
  )
}
