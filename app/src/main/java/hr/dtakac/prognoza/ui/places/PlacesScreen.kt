package hr.dtakac.prognoza.ui.places

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.presentation.places.PlacesViewModel

@Composable
fun PlacesScreen(
    viewModel: PlacesViewModel = hiltViewModel()
) {
    Text("Places screen boi")
}