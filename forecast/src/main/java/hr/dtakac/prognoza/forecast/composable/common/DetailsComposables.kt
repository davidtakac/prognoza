package hr.dtakac.prognoza.forecast.composable.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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

@Composable
fun DetailsItem(
    @DrawableRes iconId: Int,
    @StringRes labelId: Int,
    text: String
) {
    Surface(
        shape = PrognozaTheme.shapes.small,
        color = PrognozaTheme.colors.surface,
        elevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp,
                start = 12.dp,
                end = 12.dp
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = rememberImagePainter(iconId),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(stringResource(labelId))
                }
            }
            Spacer(Modifier.width(4.dp))
            Text(text)
        }
    }
}