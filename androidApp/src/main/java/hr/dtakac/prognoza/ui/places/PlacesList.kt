package hr.dtakac.prognoza.ui.places

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.places.PlaceUi

@Composable
fun PlacesList(
    places: List<PlaceUi>,
    onPlaceSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 12.dp)
    ) {
        itemsIndexed(places) { idx, placeUi ->
            PlaceItem(
                name = placeUi.name.asString(),
                details = placeUi.details.asString(),
                isSelected = placeUi.isSelected,
                modifier = Modifier
                    .clickable { onPlaceSelected(idx) }
                    .fillMaxWidth()
                    .padding(
                        vertical = 12.dp,
                        horizontal = 24.dp
                    )
            )
        }
    }
}