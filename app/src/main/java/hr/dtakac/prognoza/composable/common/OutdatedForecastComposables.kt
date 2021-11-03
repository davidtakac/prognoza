package hr.dtakac.prognoza.composable.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.model.ui.forecast.OutdatedForecastUiModel
import hr.dtakac.prognoza.theme.AppTheme

@Composable
fun OutdatedForecastMessage(outdatedForecastUiModel: OutdatedForecastUiModel?) {
    var showDialog by remember { mutableStateOf(false) }

    outdatedForecastUiModel?.let {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { showDialog = true }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_cloud_off),
                contentDescription = null,
                modifier = Modifier.size(size = 16.dp),
                colorFilter = ColorFilter.tint(color = AppTheme.textColors.mediumEmphasis)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.notify_cached_data),
                style = AppTheme.typography.subtitle2,
                color = AppTheme.textColors.mediumEmphasis
            )
        }
        OutdatedForecastDialog(
            showDialog = showDialog,
            reasonId = it.reason ?: R.string.error_generic,
            onConfirmRequest = { showDialog = false },
            onDismissRequest = { showDialog = false }
        )
    }
}

@Composable
fun OutdatedForecastDialog(
    showDialog: Boolean,
    reasonId: Int,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = stringResource(id = R.string.title_outdated_forecast)
                )
            },
            text = {
                Text(
                    text = stringResource(
                        id = R.string.template_content_outdated_forecast,
                        stringResource(id = reasonId)
                    )
                )
            },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        modifier = Modifier.wrapContentSize(),
                        onClick = { onConfirmRequest.invoke() }
                    ) {
                        Text(stringResource(id = R.string.action_ok))
                    }
                }
            }
        )
    }
}