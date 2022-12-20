package hr.dtakac.prognoza.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun AppDialog(
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmLabel: String = stringResource(id = R.string.confirm),
    dismissLabel: String = stringResource(id = R.string.cancel),
    @DrawableRes iconResId: Int? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        icon = iconResId?.let {
            {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null
                )
            }
        },
        title = {
            Text(
                text = title,
                style = PrognozaTheme.typography.titleMedium
            )
        },
        text = {
            CompositionLocalProvider(
                LocalTextStyle provides PrognozaTheme.typography.body
            ) {
                content()
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmLabel, style = PrognozaTheme.typography.label)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissLabel, style = PrognozaTheme.typography.label)
            }
        }
    )
}