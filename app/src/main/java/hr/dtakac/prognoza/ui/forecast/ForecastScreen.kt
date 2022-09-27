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
import androidx.compose.ui.platform.LocalFocusManager
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
        systemUiController.setSystemBarsColor(PrognozaTheme.elevatedBackgroundColor)
        systemUiController.setNavigationBarColor(PrognozaTheme.elevatedBackgroundColor)

        val focusManager = LocalFocusManager.current
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        LaunchedEffect(drawerState.isClosed) {
            if (drawerState.isClosed) {
                focusManager.clearFocus()
            }
        }
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(drawerShape = RectangleShape) {
                    PlacesScreen(
                        onPlaceSelected = {
                            scope.launch { drawerState.close() }
                            viewModel.getState()
                        },
                        backgroundColor = PrognozaTheme.elevatedBackgroundColor,
                        contentColor = PrognozaTheme.onBackgroundColor
                    )
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PrognozaTheme.backgroundColor)
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
                            backgroundColor = PrognozaTheme.elevatedBackgroundColor,
                            contentColor = PrognozaTheme.onBackgroundColor,
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
                                color = PrognozaTheme.onBackgroundColor,
                                trackColor = PrognozaTheme.elevatedBackgroundColor
                            )
                        }
                    }

                    if (forecast == null) {
                        if (error != null) {
                            ForecastError(
                                text = error.asString(),
                                backgroundColor = PrognozaTheme.backgroundColor,
                                contentColor = PrognozaTheme.onBackgroundColor
                            )
                        }
                    } else {
                        Box {
                            ForecastContent(
                                forecast = forecast,
                                backgroundColor = PrognozaTheme.backgroundColor,
                                contentColor = PrognozaTheme.onBackgroundColor,
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
                                        backgroundColor = PrognozaTheme.elevatedBackgroundColor,
                                        contentColor = PrognozaTheme.onBackgroundColor
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