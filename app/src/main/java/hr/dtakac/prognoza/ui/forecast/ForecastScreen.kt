package hr.dtakac.prognoza.ui.forecast

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.presentation.forecast.ForecastViewModel
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.places.PlacesScreen
import kotlinx.coroutines.delay
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
    val forecast = state.forecast
    val error = state.error

    PrognozaTheme(description = forecast?.today?.shortDescription ?: ForecastDescription.Short.UNKNOWN) {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(PrognozaTheme.elevatedSurface)
        systemUiController.setNavigationBarColor(PrognozaTheme.elevatedSurface)

        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContentColor = PrognozaTheme.onSurface,
                    drawerShape = RectangleShape,
                    drawerContainerColor = PrognozaTheme.elevatedSurface
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PrognozaTheme.surface)
                ) {
                    var toolbarPlaceVisible by remember { mutableStateOf(false) }
                    var toolbarDateVisible by remember { mutableStateOf(false) }
                    var toolbarTemperatureVisible by remember { mutableStateOf(false) }

                    Box(contentAlignment = Alignment.BottomCenter) {
                        ForecastToolbar(
                            place = forecast?.place?.asString() ?: "",
                            placeVisible = toolbarPlaceVisible,
                            date = forecast?.today?.date?.asString() ?: "",
                            dateVisible = toolbarDateVisible,
                            temperature = forecast?.today?.temperature?.asString() ?: "",
                            temperatureVisible = toolbarTemperatureVisible,
                            backgroundColor = PrognozaTheme.elevatedSurface,
                            contentColor = PrognozaTheme.onSurface,
                            onMenuClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )

                        androidx.compose.animation.AnimatedVisibility(
                            visible = state.isLoading,
                            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                        ) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp),
                                color = PrognozaTheme.onSurface,
                                trackColor = PrognozaTheme.elevatedSurface
                            )
                        }
                    }

                    if (forecast == null) {
                        if (error != null) {
                            ForecastError(
                                text = error.asString(),
                                surfaceColor = PrognozaTheme.surface,
                                contentColor = PrognozaTheme.onSurface
                            )
                        }
                    } else {
                        Box {
                            ForecastContent(
                                forecast = forecast,
                                surfaceColor = PrognozaTheme.surface,
                                contentColor = PrognozaTheme.onSurface,
                                isPlaceVisible = { toolbarPlaceVisible = !it },
                                isDateVisible = { toolbarDateVisible = !it },
                                isTemperatureVisible = { toolbarTemperatureVisible = !it }
                            )

                            if (error != null) {
                                var showSnackBar by remember { mutableStateOf(false) }
                                LaunchedEffect(forecast, error) {
                                    scope.launch {
                                        showSnackBar = true
                                        delay(5000L)
                                        showSnackBar = false
                                    }
                                }

                                // SnackBar needs to be prominent and stand out
                                PrognozaTheme(
                                    description = forecast.today.shortDescription,
                                    useDarkTheme = !isSystemInDarkTheme()
                                ) {
                                    ForecastSnackBar(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(16.dp),
                                        text = error.asString(),
                                        visible = showSnackBar,
                                        surfaceColor = PrognozaTheme.elevatedSurface,
                                        onSurfaceColor = PrognozaTheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}