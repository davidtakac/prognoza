package hr.dtakac.prognoza.ui.forecast

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.presentation.forecast.ForecastState
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.places.PlacesScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    state: ForecastState,
    backgroundColor: Color,
    elevatedBackgroundColor: Color,
    onBackgroundColor: Color,
    onSettingsClick: () -> Unit = {},
    onPlaceSelected: () -> Unit = {}
) {
    // Hide keyboard when drawer is closed
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
            ModalDrawerSheet(
                drawerShape = RectangleShape,
                drawerContainerColor = elevatedBackgroundColor,
                drawerContentColor = onBackgroundColor
            ) {
                PlacesScreen(
                    onPlaceSelected = {
                        scope.launch {
                            drawerState.close()
                        }
                        onPlaceSelected()
                    },
                    onSettingsClick = onSettingsClick
                )
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                var toolbarPlaceVisible by remember { mutableStateOf(false) }
                var toolbarDateVisible by remember { mutableStateOf(false) }
                var toolbarTemperatureVisible by remember { mutableStateOf(false) }

                Box(contentAlignment = Alignment.BottomCenter) {
                    ForecastToolbar(
                        place = state.forecast?.place?.asString() ?: "",
                        placeVisible = toolbarPlaceVisible,
                        date = state.forecast?.today?.date?.asString() ?: "",
                        dateVisible = toolbarDateVisible,
                        temperature = state.forecast?.today?.temperature?.asString() ?: "",
                        temperatureVisible = toolbarTemperatureVisible,
                        backgroundColor = elevatedBackgroundColor,
                        contentColor = onBackgroundColor,
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
                            color = onBackgroundColor,
                            trackColor = elevatedBackgroundColor
                        )
                    }
                }

                if (state.forecast == null) {
                    if (state.error != null) {
                        ForecastError(
                            text = state.error.asString(),
                            backgroundColor = backgroundColor,
                            contentColor = onBackgroundColor
                        )
                    }
                } else {
                    Box {
                        ForecastContent(
                            forecast = state.forecast,
                            backgroundColor = backgroundColor,
                            contentColor = onBackgroundColor,
                            isPlaceVisible = { toolbarPlaceVisible = !it },
                            isDateVisible = { toolbarDateVisible = !it },
                            isTemperatureVisible = { toolbarTemperatureVisible = !it }
                        )

                        if (state.error != null) {
                            var showSnackBar by remember { mutableStateOf(false) }
                            LaunchedEffect(state.forecast, state.error) {
                                scope.launch {
                                    showSnackBar = true
                                    delay(5000L)
                                    showSnackBar = false
                                }
                            }

                            // SnackBar needs to be prominent and stand out
                            // todo: use something like inverseBackground because this doesnt work with
                            //  theme settings
                            PrognozaTheme(
                                description = state.forecast.today.shortDescription,
                                useDarkTheme = !isSystemInDarkTheme()
                            ) {
                                ForecastSnackBar(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(16.dp),
                                    text = state.error.asString(),
                                    visible = showSnackBar,
                                    backgroundColor = elevatedBackgroundColor,
                                    contentColor = onBackgroundColor
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}