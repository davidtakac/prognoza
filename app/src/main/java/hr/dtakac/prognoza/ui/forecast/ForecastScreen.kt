package hr.dtakac.prognoza.ui.forecast

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.forecast.ForecastState
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.places.PlacesScreen
import hr.dtakac.prognoza.ui.common.PrognozaToolbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    state: ForecastState,
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

    CompositionLocalProvider(LocalContentColor provides PrognozaTheme.colors.onSurface) {
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerShape = RectangleShape,
                    drawerContainerColor = PrognozaTheme.colors.surface2
                ) {
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PrognozaTheme.colors.surface1)
                        .padding(WindowInsets.navigationBars.asPaddingValues())
                ) {
                    var toolbarPlaceVisible by remember { mutableStateOf(false) }
                    var toolbarDateVisible by remember { mutableStateOf(false) }
                    var toolbarTemperatureVisible by remember { mutableStateOf(false) }

                    Box(contentAlignment = Alignment.BottomCenter) {
                        PrognozaToolbar(
                            title = { Text(state.forecast?.place?.asString() ?: "") },
                            subtitle = { Text(state.forecast?.today?.date?.asString() ?: "") },
                            end = { Text(state.forecast?.today?.temperature?.asString() ?: "") },
                            navigationIcon = {
                                IconButton(
                                    onClick = { scope.launch { drawerState.open() } },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_menu),
                                        contentDescription = null
                                    )
                                }
                            },
                            titleVisible = toolbarPlaceVisible,
                            subtitleVisible = toolbarDateVisible,
                            endVisible = toolbarTemperatureVisible
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
                                color = PrognozaTheme.colors.onSurface,
                                trackColor = Color.Transparent
                            )
                        }
                    }

                    if (state.forecast == null) {
                        if (state.error != null) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.error.asString(),
                                    style = PrognozaTheme.typography.subtitleMedium,
                                    color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Box {
                            ForecastContent(
                                forecast = state.forecast,
                                modifier = Modifier.padding(horizontal = 24.dp),
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
                                PrognozaSnackBar(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(16.dp),
                                    text = state.error.asString(),
                                    visible = showSnackBar,
                                    backgroundColor = PrognozaTheme.colors.inverseSurface1,
                                    contentColor = PrognozaTheme.colors.onInverseSurface
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}