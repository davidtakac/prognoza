package hr.dtakac.prognoza.ui.forecast

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import hr.dtakac.prognoza.presentation.forecast.ForecastViewModel
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.forecast.today.ForecastContent
import hr.dtakac.prognoza.ui.places.PlacesScreen
import hr.dtakac.prognoza.ui.theme.applyOverlay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    viewModel: ForecastViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit = {}
) {
    // Refresh state every time screen is re-entered
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.getState()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val state by remember { viewModel.state }
    val forecast = state.forecast ?: return

    PrognozaTheme(forecast.today.shortDescription) {
        val colorAnimationSpec = remember {
            tween<Color>(durationMillis = 1000)
        }
        val surface by animateColorAsState(
            targetValue = PrognozaTheme.colors.surface.applyOverlay(
                overlayColor = PrognozaTheme.colors.weatherDependentOverlay
            ),
            animationSpec = colorAnimationSpec
        )
        val onSurface by animateColorAsState(
            targetValue = PrognozaTheme.colors.onSurface.copy(alpha = PrognozaTheme.alpha.high),
            animationSpec = colorAnimationSpec
        )
        val barSurface by animateColorAsState(
            targetValue = PrognozaTheme.colors.surface.applyOverlay(
                overlayColor = PrognozaTheme.colors.weatherDependentOverlay,
                overlayAlpha = 0.24f
            ),
            animationSpec = colorAnimationSpec
        )
        val onBarSurface by animateColorAsState(
            targetValue = PrognozaTheme.colors.onSurface.copy(alpha = PrognozaTheme.alpha.high),
            animationSpec = colorAnimationSpec
        )

        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(barSurface)
        systemUiController.setNavigationBarColor(barSurface)

        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContentColor = onBarSurface,
                    drawerShape = RectangleShape,
                    drawerContainerColor = barSurface
                ) {
                    PlacesScreen(
                        onPlaceSelected = {
                            scope.launch { drawerState.close() }
                            viewModel.getState()
                        }
                    )
                }
            },
            content = {
                Column(modifier = Modifier.fillMaxSize()) {
                    ForecastContent(
                        forecast = forecast,
                        surfaceColor = surface,
                        contentColor = onSurface,
                        toolbarSurfaceColor = barSurface,
                        toolbarContentColor = onBarSurface,
                        onMenuClick = { scope.launch { drawerState.open() } }
                    )
                }
            }
        )
    }
}