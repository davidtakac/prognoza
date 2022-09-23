package hr.dtakac.prognoza.ui.forecast

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastDrawerContent(
    backgroundColor: Color = Color.Unspecified,
    onBackgroundColor: Color = Color.Unspecified,
    onSettingsClick: () -> Unit = {},
) {
    ModalDrawerSheet(
        drawerContentColor = onBackgroundColor,
        drawerShape = RectangleShape,
        drawerContainerColor = backgroundColor
    ) {

    }
}