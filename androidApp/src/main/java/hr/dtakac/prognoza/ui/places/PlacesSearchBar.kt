package hr.dtakac.prognoza.ui.places

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
import hr.dtakac.prognoza.ui.common.ContentLoadingIndicatorHost
import hr.dtakac.prognoza.ui.theme.AppTheme
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun PlacesSearchBar(
    query: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onQueryChange: (String) -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    ProvideTextStyle(PrognozaTheme.typography.subtitleMedium.copy(color = LocalContentColor.current)) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            maxLines = 1,
            textStyle = LocalTextStyle.current,
            modifier = modifier,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions {
                onSubmitClick()
                focusManager.clearFocus()
            },
            cursorBrush = SolidColor(LocalContentColor.current),
            decorationBox = { innerTextField ->
                SearchFieldDecorationBox(
                    isLoading = isLoading,
                    onClearClick = { onQueryChange("") },
                    isQueryEmpty = query.isEmpty(),
                    innerTextField = innerTextField
                )
            }
        )
    }
}

@Composable
private fun SearchFieldDecorationBox(
    isLoading: Boolean,
    onClearClick: () -> Unit,
    isQueryEmpty: Boolean,
    innerTextField: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchIcon(modifier = Modifier
                .padding(end = 12.dp)
                .size(24.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (isQueryEmpty) {
                    Text(
                        text = stringResource(id = R.string.search_places),
                        color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
                    )
                }
                innerTextField()
            }

            if (!isQueryEmpty) {
                ClearTextButton(
                    onClick = onClearClick,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(24.dp)
                )
            }
        }

        ContentLoadingIndicatorHost(isLoading) { isVisible ->
            LoadingUnderline(
                isVisible,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
    }
}

@Composable
private fun SearchIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_search),
        contentDescription = null,
        colorFilter = ColorFilter.tint(LocalContentColor.current),
        modifier = modifier
    )
}

@Composable
private fun ClearTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.ic_cancel),
        contentDescription = null,
        colorFilter = ColorFilter.tint(LocalContentColor.current),
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun LoadingUnderline(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Crossfade(targetState = isLoading, modifier = modifier) {
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
private fun SearchBarPreview() {
    AppTheme {
        PlacesSearchBar(
            query = "",
            modifier = Modifier.padding(24.dp)
        )
    }
}