package hr.dtakac.prognoza.ui.forecast

import androidx.activity.compose.BackHandler
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import hr.dtakac.prognoza.presentation.forecast.ForecastState
import hr.dtakac.prognoza.ui.places.PlacesScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    state: ForecastState,
    onSettingsClick: () -> Unit = {},
    onPlaceSelected: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) {
            focusManager.clearFocus()
        }
    }
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                PlacesScreen(
                    onPlaceSelected = {
                        scope.launch { drawerState.close() }
                        onPlaceSelected()
                    },
                    onSettingsClick = onSettingsClick
                )
            }
        },
        content = {
            ForecastContent(
                state = state,
                onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    )
}