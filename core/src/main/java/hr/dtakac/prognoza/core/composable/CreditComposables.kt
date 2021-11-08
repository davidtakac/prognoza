package hr.dtakac.prognoza.core.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import hr.dtakac.prognoza.core.theme.PrognozaTheme

@Composable
fun OrganizationCredit(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        style = PrognozaTheme.typography.caption,
        color = PrognozaTheme.textColors.mediumEmphasis,
        modifier = modifier,
        textAlign = TextAlign.End
    )
}