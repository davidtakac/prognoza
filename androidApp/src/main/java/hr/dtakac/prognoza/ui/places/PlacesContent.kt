package hr.dtakac.prognoza.ui.places

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.places.PlaceUi
import hr.dtakac.prognoza.presentation.places.PlacesState
import hr.dtakac.prognoza.ui.common.ContentLoadingIndicatorHost
import hr.dtakac.prognoza.ui.theme.AppTheme

@Composable
fun PlacesContent(
    state: PlacesState,
    searchQuery: String = "",
    onSearchSubmit: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onPlaceClick: (Int) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPlaceLongClick: (Int) -> Unit = {},
) {
    Column {
        Column(
            modifier = Modifier
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 8.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlacesSearchBar(
                    query = searchQuery,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    onSubmitClick = onSearchSubmit,
                    onQueryChange = onSearchQueryChange
                )
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = null
                    )
                }
            }
            ContentLoadingIndicatorHost(state.isLoading) { isVisible ->
                PlacesLoadingUnderline(
                    isVisible,
                    modifier = Modifier.height(1.dp)
                )
            }
        }

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
                onPlaceClick = onPlaceClick,
                onPlaceLongClick = onPlaceLongClick,
                provider = state.provider,
                modifier = Modifier.weight(1f)
            )
        }
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
            isSelected = true,
            isDeletable = false
        ),
        PlaceUi(
            name = TextResource.fromString("Osijek"),
            details = TextResource.fromString("Osijek, Osijek-baranja county, Croatia, 31000"),
            isSelected = false,
            isDeletable = false
        ),
        PlaceUi(
            name = TextResource.fromString("Sombor"),
            details = TextResource.fromString("Sombor, Backa county, Serbia"),
            isSelected = false,
            isDeletable = false
        ),
        PlaceUi(
            name = TextResource.fromString("San Francisco"),
            details = TextResource.fromString("Wherever in the US that San Francisco is, I don't care, it's not like the Americans know where Croatia, let alone Tenja, is."),
            isSelected = false,
            isDeletable = false
        ),
    )
)