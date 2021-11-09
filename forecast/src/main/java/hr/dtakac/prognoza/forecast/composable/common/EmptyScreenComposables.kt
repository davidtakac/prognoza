package hr.dtakac.prognoza.forecast.composable.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.forecast.R

@Composable
fun EmptyForecast(
    reason: String,
    onTryAgainClicked: () -> Unit,
    isLoading: Boolean
) {
    Surface(
        shape = PrognozaTheme.shapes.large,
        color = PrognozaTheme.colors.surface,
        contentColor = PrognozaTheme.colors.onSurface,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        start = 48.dp,
                        end = 48.dp
                    )
                    .weight(2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cloud_off),
                    contentDescription = null,
                    modifier = Modifier.size(size = 64.dp),
                    colorFilter = ColorFilter.tint(color = PrognozaTheme.colors.error)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        id = R.string.template_error_forecast_empty_reason,
                        reason
                    ),
                    style = PrognozaTheme.typography.body1,
                    color = PrognozaTheme.textColors.highEmphasis,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                RefreshButton(
                    text = stringResource(id = R.string.button_try_again),
                    isLoading = isLoading,
                    onClick = onTryAgainClicked
                )
            }
        }
    }
}

@Composable
fun RefreshButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(18.dp)
                        .padding(2.dp),
                    color = MaterialTheme.colors.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = null,
                    modifier = Modifier.size(size = 18.dp),
                    colorFilter = ColorFilter.tint(color = PrognozaTheme.colors.onPrimary)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text)
        }
    }
}