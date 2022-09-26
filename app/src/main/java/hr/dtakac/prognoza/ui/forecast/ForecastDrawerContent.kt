package hr.dtakac.prognoza.ui.forecast

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import hr.dtakac.prognoza.ui.places.PlacesScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastDrawerContent(
    backgroundColor: Color = Color.Unspecified,
    onBackgroundColor: Color = Color.Unspecified,
    onSettingsClick: () -> Unit = {},
    onPlaceSelected: () -> Unit = {}
) {
    ModalDrawerSheet(
        drawerContentColor = onBackgroundColor,
        drawerShape = RectangleShape,
        drawerContainerColor = backgroundColor
    ) {
        PlacesScreen(onPlaceSelected = onPlaceSelected)
    }
}