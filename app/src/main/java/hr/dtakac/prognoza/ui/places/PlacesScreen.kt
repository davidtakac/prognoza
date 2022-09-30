package hr.dtakac.prognoza.ui.places

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.presentation.places.PlacesViewModel
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.R

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

    Column {
        PlacesSearchBar(
            modifier = Modifier.padding(
                top = 24.dp,
                start = 24.dp,
                end = 24.dp
            ),
            isLoading = state.isLoading,
            onQuerySubmit = viewModel::search,
            onQueryChange = { if (it.isBlank()) viewModel.getSaved() }
        )
        val empty = state.empty
        if (empty != null) {
            Text(
                text = empty.asString(),
                style = PrognozaTheme.typography.subtitleMedium,
                color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium),
                modifier = Modifier
                    .padding(24.dp)
                    .weight(1f)
            )
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(state.places) { idx, placeUi ->
                    if (idx == 0) Spacer(modifier = Modifier.height(12.dp))
                    PlaceItem(
                        placeUi = placeUi,
                        modifier = Modifier
                            .clickable(
                                onClick = { viewModel.select(idx) },
                                indication = rememberRipple(bounded = true),
                                interactionSource = remember { MutableInteractionSource() }
                            )
                            .fillMaxWidth()
                            .padding(
                                vertical = 10.dp,
                                horizontal = 24.dp
                            )
                    )
                }
            }
        }

        Text(
            text = stringResource(id = R.string.settings),
            style = PrognozaTheme.typography.subtitleMedium,
            modifier = Modifier
                .clickable(
                    onClick = onSettingsClick,
                    indication = rememberRipple(bounded = true),
                    interactionSource = remember { MutableInteractionSource() }
                )
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        )
    }
}