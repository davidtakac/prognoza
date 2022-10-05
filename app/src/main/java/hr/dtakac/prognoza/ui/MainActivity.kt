package hr.dtakac.prognoza.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
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
import hr.dtakac.prognoza.presentation.settings.ThemeSettingViewModel
import hr.dtakac.prognoza.ui.forecast.ForecastScreen
import hr.dtakac.prognoza.ui.settings.SettingsScreen
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val forecastViewModel: ForecastViewModel = hiltViewModel()
            val themeSettingViewModel: ThemeSettingViewModel = hiltViewModel()

            val forecastState by forecastViewModel.state
            val themeSetting by themeSettingViewModel.currentTheme

            LaunchedEffect(true) { themeSettingViewModel.getState() }
            // Refresh forecast on every resume, ie every app enter
            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        forecastViewModel.getState()
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
            }

            val useDarkTheme = when (themeSetting) {
                ThemeSetting.DARK -> true
                ThemeSetting.LIGHT -> false
                ThemeSetting.FOLLOW_SYSTEM -> isSystemInDarkTheme()
            }
            PrognozaTheme(
                description = forecastState.forecast?.today?.shortDescription
                    ?: ForecastDescription.Short.UNKNOWN,
                useDarkTheme = useDarkTheme
            ) {
                val navController = rememberNavController()
                val systemUiController = rememberSystemUiController()

                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = !useDarkTheme)
                systemUiController.setNavigationBarColor(Color.Transparent, darkIcons = !useDarkTheme)

                NavHost(navController = navController, startDestination = "forecast") {
                    composable("forecast") {
                        ForecastScreen(
                            state = forecastState,
                            onSettingsClick = { navController.navigate("settings") },
                            onPlaceSelected = forecastViewModel::getState
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            onBackClick = navController::navigateUp,
                            onThemeChange = themeSettingViewModel::getState,
                            onUnitChange = forecastViewModel::getState
                        )
                    }
                }
            }
        }
    }
}