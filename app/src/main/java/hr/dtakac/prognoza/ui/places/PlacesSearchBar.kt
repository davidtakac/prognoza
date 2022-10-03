package hr.dtakac.prognoza.ui.places

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun PlacesSearchBar(
    onQuerySubmit: (String) -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    isLoading: Boolean = false,
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
                LoadingSearchBarUnderline(isLoading)
            }
        }
    )
}

@Composable
private fun LoadingSearchBarUnderline(isLoading: Boolean = false) {
    Crossfade(targetState = isLoading) {
        if (it) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().height(1.dp),
                color = PrognozaTheme.colors.onSurface,
                trackColor = Color.Transparent
            )
        } else {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().height(1.dp),
                color = PrognozaTheme.colors.onSurface,
                trackColor = Color.Transparent,
                progress = 1f
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PrognozaTheme {
        PlacesSearchBar(modifier = Modifier.padding(24.dp))
    }
}