package hr.dtakac.prognoza.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hr.dtakac.prognoza.ui.forecast.ForecastScreen
import hr.dtakac.prognoza.ui.theme.PrognozaRippleTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalRippleTheme provides PrognozaRippleTheme) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "forecast") {
                    composable("forecast") {
                        ForecastScreen()
                    }
                }
            }
        }
    }
}