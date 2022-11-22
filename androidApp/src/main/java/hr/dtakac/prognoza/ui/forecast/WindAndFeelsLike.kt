package hr.dtakac.prognoza.ui.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun WindAndFeelsLike(
    feelsLike: String,
    wind: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProvideTextStyle(PrognozaTheme.typography.body) {
            Text(text = wind, modifier = Modifier.weight(2f))
            Text(text = feelsLike, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
        }
    }
}