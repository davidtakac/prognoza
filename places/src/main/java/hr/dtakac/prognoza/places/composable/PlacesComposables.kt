package hr.dtakac.prognoza.places.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.places.R
import hr.dtakac.prognoza.places.viewmodel.PlacesViewModel

@Composable
fun Places(
    placesViewModel: PlacesViewModel,
    onBackClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxHeight()) {
            PlacesTopAppBar(onBackClicked = onBackClicked)
            PlacesSearchBox(onSearchClicked = { placesViewModel.showPlaces(query = it) })
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
    onSearchClicked: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
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
            .padding(16.dp),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClicked.invoke(query) }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = "" }) {
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