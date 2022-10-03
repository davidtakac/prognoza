package hr.dtakac.prognoza.ui.places

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.places.PlaceUi
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun PlaceItem(
    placeUi: PlaceUi,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (placeUi.isSelected) {
                Image(
                    painter = painterResource(id = R.drawable.ic_my_location),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(20.dp),
                    colorFilter = ColorFilter.tint(LocalContentColor.current)
                )
            }
            Text(
                text = placeUi.name.asString(),
                style = PrognozaTheme.typography.body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = placeUi.details.asString(),
            style = PrognozaTheme.typography.body,
            color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun PlaceItemPreview() {
    PrognozaTheme {
        PlaceItem(
            placeUi = PlaceUi(
                name = TextResource.fromText("Tenja"),
                details = TextResource.fromText("Tenja, Osijek-baranja county, Croatia, 31207"),
                isSelected = true
            ),
            modifier = Modifier.padding(24.dp)
        )
    }
}