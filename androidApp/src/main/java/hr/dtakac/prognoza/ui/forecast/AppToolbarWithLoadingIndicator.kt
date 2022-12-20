package hr.dtakac.prognoza.ui.forecast

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.shared.entity.Mood
import hr.dtakac.prognoza.ui.common.AppToolbar
import hr.dtakac.prognoza.ui.common.AppToolbarState
import hr.dtakac.prognoza.ui.common.ContentLoadingIndicatorHost
import hr.dtakac.prognoza.ui.common.rememberAppToolbarState
import hr.dtakac.prognoza.ui.theme.AppTheme
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun AppToolbarWithLoadingIndicator(
    placeName: String,
    date: String,
    temperature: String,
    state: AppToolbarState,
    isLoading: Boolean,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier) {
        AppToolbar(
            state = state,
            title = { Text(placeName) },
            subtitle = { Text(date) },
            end = { Text(temperature) },
            navigation = {
                IconButton(
                    onClick = onNavigationClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = null
                    )
                }
            }
        )
        ContentLoadingIndicatorHost(isLoading = isLoading) { isVisible ->
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    trackColor = Color.Transparent
                )
            }
        }
    }
}

@Preview
@Composable
private fun ToolbarWithLoadingIndicatorPreview() {
    AppTheme(mood = Mood.CLEAR_DAY) {
        AppToolbarWithLoadingIndicator(
            placeName = "Helsinki",
            date = "September 29",
            temperature = "23",
            state = rememberAppToolbarState(
                isTitleVisible = true,
                isSubtitleVisible = true,
                isEndVisible = true
            ),
            isLoading = true,
            onNavigationClick = {}
        )
    }
}