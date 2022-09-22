package hr.dtakac.prognoza.ui.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ForecastToolbar(
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    onMenuClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit
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
                    modifier = Modifier.size(42.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                content()
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
        }
    }
}