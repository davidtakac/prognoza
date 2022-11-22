package hr.dtakac.prognoza.ui.places

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.places.PlaceUi
import hr.dtakac.prognoza.presentation.places.PlacesState
import hr.dtakac.prognoza.ui.theme.AppTheme

@Composable
fun PlacesContent(
    state: PlacesState,
    query: String = "",
    onQuerySubmit: () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onPlaceSelected: (Int) -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    Column {
        PlacesSearchBar(
            query = query,
            modifier = Modifier.padding(
                top = 24.dp,
                start = 24.dp,
                end = 24.dp
            ),
            isLoading = state.isLoading,
            onSubmitClick = onQuerySubmit,
            onQueryChange = onQueryChange
        )

        if (state.empty != null) {
            PlacesEmpty(
                message = state.empty.asString(),
                modifier = Modifier
                    .padding(24.dp)
                    .weight(1f)
            )
        } else {
            PlacesList(
                places = state.places,
                onPlaceSelected = onPlaceSelected,
                modifier = Modifier.weight(1f)
            )
        }

        SettingsButton(onClick = onSettingsClick)
    }
}

@Preview
@Composable
private fun PlacesContentPreview() = AppTheme {
    PlacesContent(state = fakeState)
}

private val fakeState: PlacesState = PlacesState(
    isLoading = false,
    empty = null,
    places = listOf(
        PlaceUi(
            name = TextResource.fromString("Tenja"),
            details = TextResource.fromString("Osijek, Osijek-baranja county, Croatia, 31207"),
            isSelected = true
        ),
        PlaceUi(
            name = TextResource.fromString("Osijek"),
            details = TextResource.fromString("Osijek, Osijek-baranja county, Croatia, 31000"),
            isSelected = false
        ),
        PlaceUi(
            name = TextResource.fromString("Sombor"),
            details = TextResource.fromString("Sombor, Backa county, Serbia"),
            isSelected = false
        ),
        PlaceUi(
            name = TextResource.fromString("San Francisco"),
            details = TextResource.fromString("Wherever in the US that San Francisco is, I don't care, it's not like the Americans know where Croatia, let alone Tenja, is."),
            isSelected = false
        ),
    )
)