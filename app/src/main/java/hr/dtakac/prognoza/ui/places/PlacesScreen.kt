package hr.dtakac.prognoza.ui.places

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.presentation.places.PlacesViewModel

@Composable
fun PlacesScreen(
    viewModel: PlacesViewModel = hiltViewModel(),
    onPlaceSelected: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    // Get state on first start
    LaunchedEffect(viewModel) {
        viewModel.getSaved()
    }
    // Notify place selected to update forecast state
    val state by remember { viewModel.state }
    LaunchedEffect(state.selectedPlace) {
        if (state.selectedPlace != null) {
            onPlaceSelected()
        }
    }

    PlacesContent(
        state = state,
        onPlaceSelected = { idx ->
            viewModel.select(idx)
            onPlaceSelected()
        },
        onSettingsClick = onSettingsClick,
        onQuerySubmit = viewModel::search,
        onQueryChange = { query -> if (query.isBlank()) viewModel.getSaved() }
    )
}