package hr.dtakac.prognoza.forecast.composable.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
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
                painter = rememberImagePainter(R.drawable.ic_cloud_off),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.notify_cached_data),
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
            title = { Text(stringResource(R.string.title_outdated_forecast)) },
            text = {
                Text(
                    stringResource(
                        id = R.string.template_content_outdated_forecast,
                        stringResource(reasonId)
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
                        Text(stringResource(R.string.action_ok))
                    }
                }
            }
        )
    }
}