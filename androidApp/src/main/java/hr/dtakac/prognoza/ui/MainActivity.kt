package hr.dtakac.prognoza.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import hr.dtakac.prognoza.androidsettings.AndroidSettingsViewModel
import hr.dtakac.prognoza.androidsettings.UiMode
import hr.dtakac.prognoza.shared.domain.GetOverview
import hr.dtakac.prognoza.shared.domain.OverviewResult
import hr.dtakac.prognoza.shared.entity.Overview
import hr.dtakac.prognoza.ui.settings.LicensesAndCreditScreen
import hr.dtakac.prognoza.ui.settings.SettingsScreen
import hr.dtakac.prognoza.ui.theme.AppTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var getOverview: GetOverview

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val androidSettingsViewModel: AndroidSettingsViewModel = hiltViewModel()
            val androidSettingsState by androidSettingsViewModel.state

            // Refresh forecast on every resume, ie every app enter
            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        // TODO: refresh forecast
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
            }

            val useDarkTheme = when (androidSettingsState.uiMode) {
                UiMode.Dark -> true
                UiMode.Light -> false
                UiMode.FollowSystem -> isSystemInDarkTheme()
            }

            AppTheme(useDarkTheme = useDarkTheme) {
                val navController = rememberNavController()
                val systemUiController = rememberSystemUiController()

                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = !useDarkTheme)
                systemUiController.setNavigationBarColor(Color.Transparent, darkIcons = !useDarkTheme)

                NavHost(navController = navController, startDestination = "forecast") {
                    composable("forecast") {
                        val scope = rememberCoroutineScope()
                        var overview by remember { mutableStateOf<Overview?>(null) }
                        LaunchedEffect(Unit) {
                            scope.launch {
                               overview = (getOverview() as? OverviewResult.Success)?.overview
                            }
                        }
                        Text(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(64.dp)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            color = Color.Black,
                            text = if (overview == null) "Loading..." else overview!!.hours.joinToString("\n---\n")
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            onBackClick = navController::navigateUp,
                            onCreditAndLicensesClick = { navController.navigate("credit") },
                            updateTheme = androidSettingsViewModel::getState,
                            updateForecast = { /*todo: update forecast*/ }
                        )
                    }
                    composable("credit") {
                        LicensesAndCreditScreen(
                            onBackClick = navController::navigateUp
                        )
                    }
                }
            }
        }
    }
}