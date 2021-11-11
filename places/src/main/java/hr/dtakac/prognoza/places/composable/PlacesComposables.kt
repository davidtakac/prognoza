package hr.dtakac.prognoza.places.composable

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.dtakac.prognoza.core.composable.ContentLoader
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.places.R
import hr.dtakac.prognoza.places.model.EmptyPlacesUiModel
import hr.dtakac.prognoza.places.model.PlaceUiModel
import hr.dtakac.prognoza.places.model.PlacesMessageUiModel

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Places(
    places: List<PlaceUiModel>,
    isLoading: Boolean,
    message: PlacesMessageUiModel?,
    emptyPlaces: EmptyPlacesUiModel?,
    selectedPlaces: List<PlaceUiModel>,
    onBackClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onClearSearch: () -> Unit,
    onPlaceClicked: (PlaceUiModel) -> Unit,
    onPlaceLongClicked: (PlaceUiModel) -> Unit,
    onPlaceSelectionCancelled: () -> Unit,
    onDeletePlacesClicked: () -> Unit
) {
    val backgroundColor = PrognozaTheme.colors.background
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColorFor(backgroundColor)) {
            Column(modifier = Modifier.fillMaxHeight()) {
                var searchValue by rememberSaveable { mutableStateOf("") }
                if (selectedPlaces.isEmpty()) {
                    PickAPlaceTopAppBar(onBackClicked = onBackClicked)
                } else {
                    SelectPlacesTopAppBar(
                        numberOfPlacesSelected = selectedPlaces.size,
                        onCloseClicked = { onPlaceSelectionCancelled() },
                        onDeleteClicked = { onDeletePlacesClicked() }
                    )
                }
                PlacesSearchBox(
                    value = searchValue,
                    onValueChange = {
                        searchValue = it
                        if (it.isEmpty()) {
                            onClearSearch()
                        }
                    },
                    onSearchClicked = { onSearchClicked(searchValue) },
                    onClearAll = {
                        searchValue = ""
                        onClearSearch()
                    }
                )
                emptyPlaces?.let {
                    EmptyPlaces(emptyPlaces = it)
                } ?: PlacesList(
                    places = places,
                    onPlaceClicked = { onPlaceClicked(it) },
                    onPlaceLongClicked = { onPlaceLongClicked(it) },
                    selectedPlaces = selectedPlaces
                )
            }
            ContentLoader(isLoading = isLoading)
            message?.let {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Snackbar(modifier = Modifier.padding(16.dp)) {
                        Text(text = stringResource(it.messageId))
                    }
                }
            }
        }
    }
}

@Composable
fun PickAPlaceTopAppBar(
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.pick_a_place)) },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    painter = rememberImagePainter(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@Composable
fun SelectPlacesTopAppBar(
    numberOfPlacesSelected: Int,
    onCloseClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                stringResource(
                    id = R.string.template_selected,
                    numberOfPlacesSelected
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClicked) {
                Icon(
                    painter = rememberImagePainter(R.drawable.ic_clear),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        actions = {
            var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
            IconButton(onClick = { showDeleteConfirmationDialog = true }) {
                Icon(
                    painter = rememberImagePainter(R.drawable.ic_delete),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            if (showDeleteConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmationDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteConfirmationDialog = false
                                onDeleteClicked()
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.delete),
                                style = PrognozaTheme.typography.button
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirmationDialog = false }) {
                            Text(
                                text = stringResource(R.string.cancel),
                                style = PrognozaTheme.typography.button
                            )
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.delete_places),
                            style = PrognozaTheme.typography.body1
                        )
                    }
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
        label = {
            Text(text = stringResource(R.string.hint_places_search))
        },
        singleLine = true,
        textStyle = PrognozaTheme.typography.body1,
        leadingIcon = {
            Icon(
                painter = rememberImagePainter(R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
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
                        painter = rememberImagePainter(R.drawable.ic_clear),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    )
}

@ExperimentalFoundationApi
@Composable
fun PlacesList(
    places: List<PlaceUiModel>,
    selectedPlaces: List<PlaceUiModel>,
    onPlaceClicked: (PlaceUiModel) -> Unit,
    onPlaceLongClicked: (PlaceUiModel) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(places) { index, place ->
            Place(
                place = place,
                onClicked = { onPlaceClicked(place) },
                onLongClicked = { onPlaceLongClicked(place) },
                isSelected = place in selectedPlaces
            )
            if (index == places.lastIndex) {
                OsmNominatimOrganizationCredit()
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun Place(
    place: PlaceUiModel,
    isSelected: Boolean,
    onClicked: () -> Unit,
    onLongClicked: () -> Unit
) {
    Surface(
        shape = PrognozaTheme.shapes.medium,
        color = PrognozaTheme.colors.surface,
        elevation = 2.dp,
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .border(
                width = if (isSelected) 2.dp else Dp.Unspecified,
                color = PrognozaTheme.colors.primary,
                shape = PrognozaTheme.shapes.medium
            )
    ) {
        val showIcon = place.isPicked || place.isSaved
        Row(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = { onClicked() },
                    onLongClick = { onLongClicked() }
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showIcon) {
                Icon(
                    painter = rememberImagePainter(
                        data = if (place.isPicked) {
                            R.drawable.ic_favorite
                        } else {
                            R.drawable.ic_favorite_border
                        }
                    ),
                    contentDescription = null,
                    tint = PrognozaTheme.colors.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(16.dp))
            }
            Column {
                Text(
                    text = place.name,
                    style = PrognozaTheme.typography.subtitle1
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = place.fullName,
                        style = PrognozaTheme.typography.subtitle2
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyPlaces(
    emptyPlaces: EmptyPlacesUiModel
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = stringResource(emptyPlaces.reason),
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
}