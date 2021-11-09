package hr.dtakac.prognoza.forecast.composable.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.model.OutdatedForecastUiModel

@Composable
fun OutdatedForecastMessage(
    outdatedForecastUiModel: OutdatedForecastUiModel,
    showDialog: Boolean,
    modifier: Modifier,
    onDialogConfirm: () -> Unit,
    onDialogDismiss: () -> Unit
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cloud_off),
                contentDescription = null,
                modifier = Modifier.size(size = 16.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.notify_cached_data),
                style = PrognozaTheme.typography.subtitle2
            )
        }
    }
    OutdatedForecastDialog(
        showDialog = showDialog,
        reasonId = outdatedForecastUiModel.reason ?: R.string.error_generic,
        onConfirmRequest = onDialogConfirm,
        onDismissRequest = onDialogDismiss
    )
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
                        onClick = { onConfirmRequest() }
                    ) {
                        Text(stringResource(id = R.string.action_ok))
                    }
                }
            }
        )
    }
}