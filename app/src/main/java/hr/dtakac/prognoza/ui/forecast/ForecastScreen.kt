package hr.dtakac.prognoza.ui.forecast

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.presentation.forecast.ForecastState
import hr.dtakac.prognoza.ui.places.PlacesScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    state: ForecastState,
    onSettingsClick: () -> Unit = {},
    onPlaceSelected: () -> Unit = {}
) {
    val forecast = state.forecast
    val error = state.error

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
            var toolbarPlaceVisible by remember { mutableStateOf(false) }
            var toolbarDateVisible by remember { mutableStateOf(false) }
            var toolbarTemperatureVisible by remember { mutableStateOf(false) }

            Scaffold(
                topBar = {
                    SmallTopAppBar(
                        navigationIcon = {
                            HamburgerButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            )
                        },
                        title = {
                            ToolbarContent(
                                place = forecast?.place?.asString() ?: "",
                                placeVisible = toolbarPlaceVisible,
                                date = forecast?.today?.date?.asString() ?: "",
                                dateVisible = toolbarDateVisible,
                                temperature = forecast?.today?.temperature?.asString() ?: "",
                                temperatureVisible = toolbarTemperatureVisible,
                            )
                        }
                    )
                }
            ) { padding ->
                Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = state.isLoading,
                        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp),
                        )
                    }

                    if (forecast == null) {
                        if (error != null) {
                            ForecastError(text = error.asString())
                        }
                    } else {
                        Box {
                            ForecastContent(
                                forecast = forecast,
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

                                ForecastSnackBar(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(16.dp),
                                    text = error.asString(),
                                    visible = showSnackBar
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}