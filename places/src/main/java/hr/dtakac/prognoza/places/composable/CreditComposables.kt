package hr.dtakac.prognoza.places.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.core.composable.OrganizationCredit
import hr.dtakac.prognoza.places.R

@Composable
fun OsmNominatimOrganizationCredit() {
    OrganizationCredit(
        text = stringResource(R.string.osm_nominatim_credit),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
    )
}