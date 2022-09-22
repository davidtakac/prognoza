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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun ForecastToolbar(
    place: String,
    placeVisible: Boolean,
    dateTime: String,
    dateTimeVisible: Boolean,
    temperature: String,
    temperatureVisible: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    onBackgroundColor: Color = Color.Unspecified,
    onMenuClicked: () -> Unit = {}
) {
    CompositionLocalProvider(LocalContentColor provides onBackgroundColor) {
        Column(modifier = modifier.background(backgroundColor)) {
            Row(
                modifier = Modifier
                    .height(90.dp)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HamburgerButton(
                    onClick = onMenuClicked,
                    modifier = Modifier.size(42.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    SlideUpAppearText(
                        text = place,
                        visible = placeVisible,
                        style = PrognozaTheme.typography.titleSmall
                    )
                    SlideUpAppearText(
                        text = dateTime,
                        visible = dateTimeVisible,
                        style = PrognozaTheme.typography.subtitleSmall
                    )
                }
                SlideUpAppearText(
                    text = temperature,
                    visible = temperatureVisible,
                    style = PrognozaTheme.typography.headlineSmall
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
        Column(
            modifier = Modifier.padding(4.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            repeat(3) {
                Divider(
                    color = LocalContentColor.current,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SlideUpAppearText(
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