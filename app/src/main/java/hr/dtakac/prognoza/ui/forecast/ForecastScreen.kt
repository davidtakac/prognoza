package hr.dtakac.prognoza.ui.forecast

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import hr.dtakac.prognoza.presentation.today.ForecastViewModel
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.today.TodayScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    viewModel: ForecastViewModel = hiltViewModel()
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
    val today = state.today ?: return // todo: handle other cases

    PrognozaTheme(today.shortDescription) {
        val colorAnimationSpec = remember {
            tween<Color>(durationMillis = 1000)
        }
        val contentColor by animateColorAsState(
            targetValue = PrognozaTheme.colors.onBackground.copy(
                alpha = 0.87f
            ),
            animationSpec = colorAnimationSpec
        )
        val backgroundColor by animateColorAsState(
            targetValue = PrognozaTheme.colors.background,
            animationSpec = colorAnimationSpec
        )

        setStatusAndNavigationBarColors(backgroundColor)

        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContentColor = contentColor,
                    drawerShape = RectangleShape,
                    drawerContainerColor = backgroundColor
                ) {
                    Text("shii")
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                ) {
                    var toolbarPlaceVisible by remember { mutableStateOf(false) }
                    var toolbarDateTimeVisible by remember { mutableStateOf(false) }
                    var toolbarTemperatureVisible by remember { mutableStateOf(false) }

                    CompositionLocalProvider(LocalContentColor provides contentColor) {
                        ForecastToolbar(
                            place = today.place.asString(),
                            placeVisible = toolbarPlaceVisible,
                            dateTime = today.time.asString(),
                            dateTimeVisible = toolbarDateTimeVisible,
                            temperature = today.temperature.asString(),
                            temperatureVisible = toolbarTemperatureVisible,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .background(backgroundColor),
                            onMenuClicked = { scope.launch { drawerState.open() } }
                        )

                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = "today") {
                            composable("today") {
                                TodayScreen(
                                    state = today,
                                    modifier = Modifier
                                        .background(backgroundColor)
                                        .padding(horizontal = 24.dp),
                                    onPlaceVisibilityChange = { visibilityPercent ->
                                        toolbarPlaceVisible = visibilityPercent == 0f
                                    },
                                    onDateTimeVisibilityChange = { visibilityPercent ->
                                        toolbarDateTimeVisible = visibilityPercent == 0f
                                    },
                                    onTemperatureVisibilityChange = { visibilityPercent ->
                                        toolbarTemperatureVisible = visibilityPercent <= 50f
                                    }
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun setStatusAndNavigationBarColors(color: Color) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color)
    systemUiController.setNavigationBarColor(color)
}