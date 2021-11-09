package hr.dtakac.prognoza.places.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.places.R
import hr.dtakac.prognoza.places.model.EmptyPlacesUiModel
import hr.dtakac.prognoza.places.model.PlaceUiModel
import hr.dtakac.prognoza.places.viewmodel.PlacesViewModel

@Composable
fun Places(
    placesViewModel: PlacesViewModel,
    onBackClicked: () -> Unit
) {
    val places by placesViewModel.places
    val isLoading by placesViewModel.isLoading
    val message by placesViewModel.message
    val emptyPlaces by placesViewModel.emptyPlaces

    Surface(
        shape = PrognozaTheme.shapes.large,
        color = PrognozaTheme.colors.surface
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                var searchValue by rememberSaveable { mutableStateOf("") }
                PlacesTopAppBar(onBackClicked = onBackClicked)
                PlacesSearchBox(
                    value = searchValue,
                    onValueChange = { newValue: String -> searchValue = newValue },
                    onSearchClicked = {
                        placesViewModel.showPlaces(searchValue)
                    },
                    onClearAll = {
                        searchValue = ""
                        placesViewModel.showPlaces()
                    }
                )
                emptyPlaces?.let {
                    EmptyPlaces(emptyPlaces = it)
                } ?: PlacesList(
                    places = places,
                    onPlacePicked = {
                        placesViewModel.select(placeId = it.id)
                    }
                )
            }
            if (isLoading) {
                CircularProgressIndicator()
            }
            message?.let {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Snackbar(modifier = Modifier.padding(16.dp)) {
                        Text(text = stringResource(id = it.messageId))
                    }
                }
            }
        }
    }
}

@Composable
fun PlacesTopAppBar(
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.pick_a_place)) },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    painter = rememberImagePainter(data = R.drawable.ic_arrow_back),
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp)
                )
            }
        }
    )
}

@Composable
fun PlacesSearchBox(
    value: String,
    onSearchClicked: () -> Unit,
    onValueChange: (String) -> Unit,
    onClearAll: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = R.string.hint_places_search)) },
        singleLine = true,
        textStyle = PrognozaTheme.typography.body1.copy(color = PrognozaTheme.textColors.highEmphasis),
        leadingIcon = {
            Icon(
                painter = rememberImagePainter(data = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(size = 24.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClicked() }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = { onClearAll() }
                ) {
                    Icon(
                        painter = rememberImagePainter(data = R.drawable.ic_clear),
                        contentDescription = null,
                        modifier = Modifier.size(size = 24.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun PlacesList(
    places: List<PlaceUiModel>,
    onPlacePicked: (PlaceUiModel) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(places) { index, place ->
            Place(
                place = place,
                onClicked = { onPlacePicked(place) }
            )
            if (index == places.lastIndex) {
                OsmNominatimOrganizationCredit()
            }
        }
    }
}

@Composable
fun Place(
    place: PlaceUiModel,
    onClicked: () -> Unit
) {
    Surface(
        shape = PrognozaTheme.shapes.medium,
        color = PrognozaTheme.colors.surface,
        elevation = 2.dp,
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 8.dp
        )
    ) {
        val showIcon = place.isSelected || place.isSaved
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClicked() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showIcon) {
                Image(
                    painter = rememberImagePainter(
                        data = if (place.isSelected) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                    ),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = PrognozaTheme.colors.primary),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Column {
                Text(
                    text = place.name,
                    color = PrognozaTheme.textColors.highEmphasis,
                    style = PrognozaTheme.typography.subtitle1
                )
                Text(
                    text = place.fullName,
                    color = PrognozaTheme.textColors.mediumEmphasis,
                    style = PrognozaTheme.typography.subtitle2
                )
            }
        }
    }
}

@Composable
fun EmptyPlaces(
    emptyPlaces: EmptyPlacesUiModel
) {
    Text(
        text = stringResource(id = emptyPlaces.reason),
        color = PrognozaTheme.textColors.mediumEmphasis,
        style = PrognozaTheme.typography.subtitle1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp
            ),
        textAlign = TextAlign.Center
    )
}