package hr.dtakac.prognoza.forecast.composable.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.model.EmptyForecastBecauseNoSelectedPlace
import hr.dtakac.prognoza.forecast.model.EmptyForecastBecauseReason

@Composable
fun EmptyForecast(
    text: String,
    content: @Composable () -> Unit
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
                    modifier = Modifier.size(size = 52.dp),
                    colorFilter = ColorFilter.tint(color = PrognozaTheme.textColors.mediumEmphasis)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = text,
                    style = PrognozaTheme.typography.body2,
                    color = PrognozaTheme.textColors.highEmphasis,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                content()
            }
        }
    }
}

@Composable
fun LoaderButton(
    text: String,
    @DrawableRes iconResourceId: Int,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val buttonColors = ButtonDefaults.buttonColors()
    val contentColor by buttonColors.contentColor(enabled = !isLoading)
    Button(
        onClick = onClick,
        enabled = !isLoading,
        colors = buttonColors
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(18.dp)
                    .padding(2.dp),
                strokeWidth = 2.dp,
                color = contentColor
            )
        } else {
            Icon(
                painter = rememberImagePainter(data = iconResourceId),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = contentColor
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Composable
fun EmptyForecastBecauseReason(
    emptyForecast: EmptyForecastBecauseReason,
    isLoading: Boolean,
    onTryAgainClicked: () -> Unit
) {
    val errorText = stringResource(
        id = R.string.template_error_forecast_empty_reason,
        stringResource(id = emptyForecast.reason ?: R.string.error_generic)
    )
    EmptyForecast(text = errorText) {
        LoaderButton(
            text = stringResource(id = R.string.button_try_again),
            isLoading = isLoading,
            onClick = onTryAgainClicked,
            iconResourceId = R.drawable.ic_refresh
        )
    }
}

@Composable
fun EmptyForecastBecauseNoSelectedPlace(
    emptyForecast: EmptyForecastBecauseNoSelectedPlace,
    isLoading: Boolean,
    onPickAPlaceClicked: () -> Unit
) {
    val errorText = stringResource(id = R.string.error_forecast_empty_no_selected_place)
    EmptyForecast(text = errorText) {
        LoaderButton(
            text = stringResource(id = R.string.pick_a_place),
            isLoading = isLoading,
            onClick = onPickAPlaceClicked,
            iconResourceId = R.drawable.ic_favorite_border
        )
    }
}