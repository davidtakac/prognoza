package hr.dtakac.prognoza.ui.places

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.presentation.places.PlacesViewModel
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.places.PlaceUi

@Composable
fun PlacesScreen(
    viewModel: PlacesViewModel = hiltViewModel()
) {
    val state by remember { viewModel.state }
    val places = state.places

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        SearchBar(
            modifier = Modifier.padding(top = 24.dp),
            onSubmit = viewModel::search
        )
        LazyColumn {
            itemsIndexed(places) { idx, placeUi ->
                PlaceItem(
                    placeUi = placeUi,
                    onClick = { viewModel.select(idx) },
                    modifier = Modifier.padding(top = if (idx == 0) 16.dp else 0.dp, bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val style = PrognozaTheme.typography.subtitleSmall
    var query by remember { mutableStateOf("") }
    BasicTextField(
        value = query,
        onValueChange = { query = it },
        maxLines = 1,
        textStyle = style,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions { onSubmit(query) },
        cursorBrush = SolidColor(LocalContentColor.current),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(LocalContentColor.current.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp)
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (query.isEmpty()) {
                        Text(
                            stringResource(id = R.string.search_places),
                            style = style,
                            color = LocalContentColor.current.copy(alpha = 0.6f)
                        )
                    }
                    innerTextField()
                }
            }

        }
    )
    Divider(color = LocalContentColor.current)
}

@Composable
fun PlaceItem(
    placeUi: PlaceUi,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (placeUi.isSelected) LocalContentColor.current else Color.Transparent
            )
            .clickable { onClick() }
    ) {
        Text(
            text = placeUi.name.asString(),
            style = PrognozaTheme.typography.body,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = placeUi.details.asString(),
            style = PrognozaTheme.typography.body,
            color = LocalContentColor.current.copy(alpha = 0.6f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}