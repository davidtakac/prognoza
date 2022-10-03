package hr.dtakac.prognoza.ui.places

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.places.PlaceUi
import hr.dtakac.prognoza.presentation.places.PlacesState
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun PlacesContent(
    state: PlacesState,
    onPlaceSelected: (Int) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onQuerySubmit: (String) -> Unit = {},
    onQueryChange: (String) -> Unit = {}
) {
    Column {
        PlacesSearchBar(
            modifier = Modifier.padding(
                top = 24.dp,
                start = 24.dp,
                end = 24.dp
            ),
            isLoading = state.isLoading,
            onQuerySubmit = onQuerySubmit,
            onQueryChange = onQueryChange
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
                                onClick = { onPlaceSelected(idx) },
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

@Preview
@Composable
private fun Preview() = PrognozaTheme {
    PlacesContent(state = fakeState)
}

private val fakeState: PlacesState = PlacesState(
    isLoading = false,
    empty = null,
    places = listOf(
        PlaceUi(
            name = TextResource.fromText("Tenja"),
            details = TextResource.fromText("Osijek, Osijek-baranja county, Croatia, 31207"),
            isSelected = true
        ),
        PlaceUi(
            name = TextResource.fromText("Osijek"),
            details = TextResource.fromText("Osijek, Osijek-baranja county, Croatia, 31000"),
            isSelected = false
        ),
        PlaceUi(
            name = TextResource.fromText("Sombor"),
            details = TextResource.fromText("Sombor, Backa county, Serbia"),
            isSelected = false
        ),
        PlaceUi(
            name = TextResource.fromText("San Francisco"),
            details = TextResource.fromText("Wherever in the US that San Francisco is, I don't care, it's not like the Americans know where Croatia, let alone Tenja, is."),
            isSelected = false
        ),
    )
)