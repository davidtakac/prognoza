package hr.dtakac.prognoza.ui.forecast

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import hr.dtakac.prognoza.presentation.forecast.ForecastViewModel
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.places.PlacesScreen
import hr.dtakac.prognoza.ui.theme.applyOverlay
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
    val forecast = state.forecast ?: return

    PrognozaTheme(forecast.today.shortDescription) {
        val colorAnimationSpec = remember {
            tween<Color>(durationMillis = 1000)
        }
        val surface by animateColorAsState(
            targetValue = PrognozaTheme.colors.surface.applyOverlay(
                overlayColor = PrognozaTheme.colors.moodOverlay
            ),
            animationSpec = colorAnimationSpec
        )
        val onSurface by animateColorAsState(
            targetValue = PrognozaTheme.colors.onSurface.copy(alpha = PrognozaTheme.alpha.high),
            animationSpec = colorAnimationSpec
        )
        val barSurface by animateColorAsState(
            targetValue = PrognozaTheme.colors.surface.applyOverlay(
                overlayColor = PrognozaTheme.colors.moodOverlay,
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
                    var toolbarPlaceVisible by remember { mutableStateOf(false) }
                    var toolbarDateVisible by remember { mutableStateOf(false) }
                    var toolbarTemperatureVisible by remember { mutableStateOf(false) }

                    ForecastToolbar(
                        place = forecast.place.asString(),
                        placeVisible = toolbarPlaceVisible,
                        date = forecast.today.date.asString(),
                        dateVisible = toolbarDateVisible,
                        temperature = forecast.today.temperature.asString(),
                        temperatureVisible = toolbarTemperatureVisible,
                        backgroundColor = barSurface,
                        contentColor = onBarSurface,
                        onMenuClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )

                    ForecastContent(
                        forecast = forecast,
                        surfaceColor = surface,
                        contentColor = onSurface,
                        isPlaceVisible = { toolbarPlaceVisible = !it },
                        isDateVisible = { toolbarDateVisible = !it },
                        isTemperatureVisible = { toolbarTemperatureVisible = !it }
                    )
                }
            }
        )
    }
}

@Composable
private fun ForecastToolbar(
    place: String,
    placeVisible: Boolean,
    date: String,
    dateVisible: Boolean,
    temperature: String,
    temperatureVisible: Boolean,
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    onMenuClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(modifier = Modifier.background(backgroundColor)) {
            Row(
                modifier = Modifier
                    .height(90.dp)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HamburgerButton(
                    onClick = onMenuClick,
                    modifier = Modifier.size(42.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                ToolbarContent(
                    place = place,
                    placeVisible = placeVisible,
                    date = date,
                    dateVisible = dateVisible,
                    temperature = temperature,
                    temperatureVisible = temperatureVisible
                )
            }
        }
    }
}

@Composable
private fun HamburgerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            repeat(3) {
                Divider(
                    color = LocalContentColor.current,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun RowScope.ToolbarContent(
    place: String,
    placeVisible: Boolean,
    date: String,
    dateVisible: Boolean,
    temperature: String,
    temperatureVisible: Boolean
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        SlideUpAppearText(
            text = place,
            visible = placeVisible,
            style = PrognozaTheme.typography.titleMedium
        )
        SlideUpAppearText(
            text = date,
            visible = dateVisible,
            style = PrognozaTheme.typography.subtitleMedium
        )
    }
    SlideUpAppearText(
        text = temperature,
        visible = temperatureVisible,
        style = PrognozaTheme.typography.headlineSmall
    )
}

@Composable
private fun SlideUpAppearText(
    text: String,
    visible: Boolean,
    style: TextStyle = LocalTextStyle.current
) {
    val enter = fadeIn() + expandVertically(expandFrom = Alignment.Top)
    val exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
    AnimatedVisibility(
        visible = visible,
        enter = enter,
        exit = exit
    ) {
        Text(text = text, style = style)
    }
}