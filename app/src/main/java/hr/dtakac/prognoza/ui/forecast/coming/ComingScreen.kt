package hr.dtakac.prognoza.ui.forecast.coming

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hr.dtakac.prognoza.presentation.DayUi

@Composable
fun ComingScreen(
    state: List<DayUi>,
    modifier: Modifier = Modifier
) {
    Text(text = "It's ya boi, the coming forecast")
}