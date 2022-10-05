package hr.dtakac.prognoza.ui.places

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.places.PlaceUi
import hr.dtakac.prognoza.presentation.places.PlacesState
import hr.dtakac.prognoza.ui.common.rememberPrognozaLoadingIndicatorState
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
        SearchBar(
            modifier = Modifier.padding(
                top = 24.dp,
                start = 24.dp,
                end = 24.dp
            ),
            isLoading = state.isLoading,
            onQuerySubmit = onQuerySubmit,
            onQueryChange = onQueryChange
        )

        if (state.empty != null) {
            Empty(
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

@Composable
private fun Empty(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        style = PrognozaTheme.typography.subtitleMedium,
        color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium),
        modifier = modifier
    )
}

@Composable
private fun PlacesList(
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
                    .clickable(
                        onClick = { onPlaceSelected(idx) },
                        indication = rememberRipple(bounded = true),
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .fillMaxWidth()
                    .padding(
                        vertical = 12.dp,
                        horizontal = 24.dp
                    )
            )
        }
    }
}

@Composable
private fun SettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.settings),
        style = PrognozaTheme.typography.subtitleMedium,
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            )
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    )
}

@Composable
private fun PlaceItem(
    name: String,
    details: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected) {
                Image(
                    painter = painterResource(id = R.drawable.ic_my_location),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(20.dp),
                    colorFilter = ColorFilter.tint(LocalContentColor.current)
                )
            }
            Text(
                text = name,
                style = PrognozaTheme.typography.body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = details,
            style = PrognozaTheme.typography.body,
            color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SearchBar(
    isLoading: Boolean = false,
    onQuerySubmit: (String) -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val style = PrognozaTheme.typography.subtitleMedium.copy(color = LocalContentColor.current)
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    BasicTextField(
        value = query,
        onValueChange = {
            query = it
            onQueryChange(it)
        },
        maxLines = 1,
        textStyle = style,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions {
            onQuerySubmit(query)
            focusManager.clearFocus()
        },
        cursorBrush = SolidColor(LocalContentColor.current),
        decorationBox = { innerTextField ->
            Column {
                Row(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(LocalContentColor.current),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(24.dp)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        if (query.isEmpty()) {
                            Text(
                                stringResource(id = R.string.search_places),
                                style = style,
                                color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
                            )
                        }
                        innerTextField()
                    }
                }

                val loadingState = rememberPrognozaLoadingIndicatorState()
                if (isLoading) loadingState.showLoadingIndicator()
                else loadingState.hideLoadingIndicator()
                LoadingUnderline(isLoading)
            }
        }
    )
}

@Composable
private fun LoadingUnderline(isLoading: Boolean = false) {
    Crossfade(targetState = isLoading) {
        if (it) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = PrognozaTheme.colors.onSurface,
                trackColor = Color.Transparent
            )
        } else {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = PrognozaTheme.colors.onSurface,
                trackColor = Color.Transparent,
                progress = 1f
            )
        }
    }
}

@Preview
@Composable
private fun PlacesSearchBarPreview() {
    PrognozaTheme {
        SearchBar(modifier = Modifier.padding(24.dp))
    }
}

@Preview
@Composable
private fun PlaceItemPreview() {
    PrognozaTheme {
        PlaceItem(
            name = "Tenja",
            details = "Tenja, Osijek-baranja county, Croatia, 31207",
            isSelected = true,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Preview
@Composable
private fun PlacesContentPreview() = PrognozaTheme {
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