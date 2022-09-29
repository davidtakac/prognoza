package hr.dtakac.prognoza.ui.forecast

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

// todo: make this a proper reusable composable!

@Composable
fun ForecastToolbar(
    place: String,
    placeVisible: Boolean,
    date: String,
    dateVisible: Boolean,
    temperature: String,
    temperatureVisible: Boolean,
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    onMenuClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(modifier = Modifier.background(backgroundColor)) {
            Row(
                modifier = Modifier
                    .height(90.dp)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HamburgerButton(
                    onClick = onMenuClick,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                ToolbarContent(
                    place = place,
                    placeVisible = placeVisible,
                    date = date,
                    dateVisible = dateVisible,
                    temperature = temperature,
                    temperatureVisible = temperatureVisible
                )
            }
        }
    }
}

@Composable
private fun HamburgerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_menu),
            contentDescription = null
        )
        /*Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            repeat(3) {
                Divider(
                    color = LocalContentColor.current,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }*/
    }
}

@Composable
private fun RowScope.ToolbarContent(
    place: String,
    placeVisible: Boolean,
    date: String,
    dateVisible: Boolean,
    temperature: String,
    temperatureVisible: Boolean
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        SlideUpAppearText(
            text = place,
            visible = placeVisible,
            style = PrognozaTheme.typography.titleMedium
        )
        SlideUpAppearText(
            text = date,
            visible = dateVisible,
            style = PrognozaTheme.typography.subtitleMedium
        )
    }
    SlideUpAppearText(
        text = temperature,
        visible = temperatureVisible,
        style = PrognozaTheme.typography.headlineSmall
    )
}

// todo: private this when upper todo is done
@Composable
fun SlideUpAppearText(
    text: String,
    visible: Boolean,
    style: TextStyle = LocalTextStyle.current
) {
    val enter = fadeIn() + expandVertically(expandFrom = Alignment.Top)
    val exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
    AnimatedVisibility(
        visible = visible,
        enter = enter,
        exit = exit
    ) {
        Text(text = text, style = style)
    }
}