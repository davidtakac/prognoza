package hr.dtakac.prognoza.composable.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.theme.AppTheme

@Composable
fun OrganizationCredit(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        style = AppTheme.typography.caption,
        color = AppTheme.textColors.mediumEmphasis,
        modifier = modifier,
        textAlign = TextAlign.End
    )
}

@Composable
fun MetNorwayOrganizationCredit() {
    OrganizationCredit(
        text = stringResource(id = R.string.met_norway_credit),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp
            )
    )
}