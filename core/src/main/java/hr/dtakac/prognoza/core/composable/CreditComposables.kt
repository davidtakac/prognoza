package hr.dtakac.prognoza.core.composable

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import hr.dtakac.prognoza.core.theme.PrognozaTheme

@Composable
fun OrganizationCredit(
    text: String,
    modifier: Modifier
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = text,
            modifier = modifier,
            textAlign = TextAlign.End,
            style = PrognozaTheme.typography.caption
        )
    }
}