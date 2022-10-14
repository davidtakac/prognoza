package hr.dtakac.prognoza.ui.places

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.presentation.OnEvent
import hr.dtakac.prognoza.presentation.places.PlacesViewModel

@Composable
fun PlacesScreen(
    viewModel: PlacesViewModel = hiltViewModel(),
    onPlaceSelected: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val state by viewModel.state
    LaunchedEffect(true) {
        viewModel.getSaved()
    }
    OnEvent(state.placeSelected) {
        onPlaceSelected()
    }

    PlacesContent(
        state = state,
        onPlaceSelected = viewModel::select,
        onSettingsClick = onSettingsClick,
        onQuerySubmit = viewModel::search,
        onQueryChange = { query ->
            if (query.isBlank()) {
                viewModel.getSaved()
            }
        }
    )
}