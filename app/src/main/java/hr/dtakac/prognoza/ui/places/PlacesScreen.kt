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
    LaunchedEffect(true) { viewModel.getSaved() }
    val state by viewModel.state

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