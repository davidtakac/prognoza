package hr.dtakac.prognoza.ui.places

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.places.PlaceUi
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlacesList(
    places: List<PlaceUi>,
    provider: TextResource?,
    onPlaceClick: (Int) -> Unit,
    onPlaceLongClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        itemsIndexed(places) { idx, placeUi ->
            PlaceItem(
                name = placeUi.name.asString(),
                details = placeUi.details.asString(),
                isSelected = placeUi.isSelected,
                modifier = Modifier
                    .combinedClickable(
                        onClick = { onPlaceClick(idx) },
                        onLongClick = if (placeUi.isDeletable) {
                            {
                                onPlaceLongClick(idx)
                                hapticFeedback.performHapticFeedback(
                                    hapticFeedbackType = HapticFeedbackType.LongPress
                                )
                            }
                        } else null
                    )
                    .fillMaxWidth()
                    .padding(
                        vertical = 12.dp,
                        horizontal = 24.dp
                    )
            )
        }
        provider?.let {
            item(key = "provider") {
                Text(
                    text = it.asString(),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp),
                    style = PrognozaTheme.typography.body,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}