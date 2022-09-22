package hr.dtakac.prognoza.ui.forecast

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import hr.dtakac.prognoza.presentation.forecast.ForecastViewModel
import hr.dtakac.prognoza.ui.forecast.coming.ComingScreen
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.forecast.today.TodayScreen
import hr.dtakac.prognoza.ui.theme.applyOverlay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    viewModel: ForecastViewModel = hiltViewModel(),
    onPlacePickerClick: () -> Unit = {},
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
            targetValue = PrognozaTheme.colors.onSurface,
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
            targetValue = PrognozaTheme.colors.onSurface,
            animationSpec = colorAnimationSpec
        )

        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(barSurface)
        systemUiController.setNavigationBarColor(barSurface)

        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val navController = rememberNavController()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ForecastDrawerContent(
                    place = forecast.place.asString(),
                    backgroundColor = barSurface,
                    onBackgroundColor = onBarSurface,
                    onTodayClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("today") {
                                popUpTo("today") { inclusive = true }
                            }
                        }
                    },
                    onComingClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("coming") {
                                popUpTo("coming") { inclusive = true }
                            }
                        }
                    },
                    onPlacePickerClick = onPlacePickerClick,
                    onSettingsClick = onSettingsClick
                )
            },
            content = {
                Column(modifier = Modifier.fillMaxSize()) {
                    fun openDrawer() = scope.launch { drawerState.open() }
                    NavHost(
                        navController = navController,
                        startDestination = "today"
                    ) {
                        composable("today") {
                            TodayScreen(
                                state = forecast.today,
                                place = forecast.place,
                                surfaceColor = surface,
                                contentColor = onSurface,
                                toolbarSurfaceColor = barSurface,
                                toolbarContentColor = onBarSurface,
                                onMenuClick = ::openDrawer
                            )
                        }
                        composable("coming") {
                            ComingScreen(
                                place = forecast.place,
                                state = forecast.coming,
                                surfaceColor = surface,
                                contentColor = onSurface,
                                toolbarSurfaceColor = barSurface,
                                toolbarContentColor = onBarSurface,
                                onMenuClick = ::openDrawer
                            )
                        }
                    }
                }
            }
        )
    }
}