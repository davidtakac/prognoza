package hr.dtakac.prognoza.ui.forecast

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastDrawerContent(
    place: String,
    backgroundColor: Color = Color.Unspecified,
    onBackgroundColor: Color = Color.Unspecified,
    onTodayClick: () -> Unit = {},
    onComingClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPlacePickerClick: () -> Unit = {}
) {
    ModalDrawerSheet(
        drawerContentColor = onBackgroundColor,
        drawerShape = RectangleShape,
        drawerContainerColor = backgroundColor
    ) {
        LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
            item {
                SectionItem(
                    text = stringResource(id = R.string.forecast),
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                )
            }
            item {
                NavigationItem(
                    text = stringResource(id = R.string.today),
                    icon = R.drawable.ic_calendar_today,
                    onClick = onTodayClick
                )
            }
            item {
                NavigationItem(
                    text = stringResource(id = R.string.coming),
                    icon = R.drawable.ic_calendar_month,
                    onClick = onComingClick
                )
            }
            item {
                Divider(
                    color = LocalContentColor.current,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
                SectionItem(
                    text = stringResource(id = R.string.settings),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            item {
                NavigationItem(
                    text = place,
                    icon = R.drawable.ic_location_pin,
                    onClick = onPlacePickerClick
                )
            }
            item {
                NavigationItem(
                    text = stringResource(id = R.string.settings),
                    icon = R.drawable.ic_outline_settings,
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@Composable
private fun SectionItem(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = PrognozaTheme.typography.label,
        modifier = modifier
    )
}

@Composable
private fun NavigationItem(
    text: String,
    @DrawableRes
    icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = text,
            style = PrognozaTheme.typography.subtitleSmall,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}