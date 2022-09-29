package hr.dtakac.prognoza.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.presentation.forecast.ForecastViewModel
import hr.dtakac.prognoza.ui.forecast.ForecastScreen
import hr.dtakac.prognoza.ui.settings.SettingsScreen
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val forecastViewModel: ForecastViewModel = hiltViewModel()
            val forecastState = remember { forecastViewModel.state }.value

            PrognozaTheme(description = forecastState.forecast?.today?.shortDescription ?: ForecastDescription.Short.UNKNOWN) {
                val backgroundColor = PrognozaTheme.backgroundColor
                val elevatedBackgroundColor = PrognozaTheme.elevatedBackgroundColor
                val onBackgroundColor = PrognozaTheme.onBackgroundColor

                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(elevatedBackgroundColor)
                systemUiController.setNavigationBarColor(elevatedBackgroundColor)

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "forecast") {
                    composable("forecast") {
                        val lifecycleOwner = LocalLifecycleOwner.current
                        DisposableEffect(lifecycleOwner) {
                            val observer = LifecycleEventObserver { _, event ->
                                if (event == Lifecycle.Event.ON_RESUME) forecastViewModel.getState()
                            }
                            lifecycleOwner.lifecycle.addObserver(observer)
                            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                        }

                        ForecastScreen(
                            state = forecastState,
                            backgroundColor = backgroundColor,
                            elevatedBackgroundColor = elevatedBackgroundColor,
                            onBackgroundColor = onBackgroundColor,
                            onSettingsClick = {
                                navController.navigate("settings")
                            },
                            onPlaceSelected = {
                                forecastViewModel.getState()
                            }
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            backgroundColor = backgroundColor,
                            elevatedBackgroundColor = elevatedBackgroundColor,
                            onBackgroundColor = onBackgroundColor
                        )
                    }
                }
            }
        }
    }
}