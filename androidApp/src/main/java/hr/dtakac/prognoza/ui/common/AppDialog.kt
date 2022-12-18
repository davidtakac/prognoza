package hr.dtakac.prognoza.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    contentColor: Color = PrognozaTheme.colors.onSurface,
    backgroundColor: Color = PrognozaTheme.colors.surface3,
    content: @Composable () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier,
        containerColor = backgroundColor,
        titleContentColor = contentColor,
        onDismissRequest = onDismiss,
        icon = iconResId?.let {
            {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = contentColor.copy(alpha = PrognozaTheme.alpha.medium)
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
                LocalContentColor provides contentColor,
                LocalTextStyle provides PrognozaTheme.typography.body
            ) {
                content()
            }
        },
        confirmButton = {
            Button(
                label = confirmLabel,
                textColor = contentColor,
                onClick = onConfirm
            )
        },
        dismissButton = {
            Button(
                label = dismissLabel,
                textColor = contentColor,
                onClick = onDismiss
            )
        }
    )
}

@Composable
private fun Button(
    label: String,
    onClick: () -> Unit,
    textColor: Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = label,
            color = textColor,
            style = PrognozaTheme.typography.label
        )
    }
}