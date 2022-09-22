package hr.dtakac.prognoza.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hr.dtakac.prognoza.ui.forecast.ForecastScreen
import hr.dtakac.prognoza.ui.places.PlacesScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "forecast") {
                composable("forecast") {
                    ForecastScreen(
                        onPlacePickerClick = {
                            navController.navigate("places")
                        },
                        onSettingsClick = {
                            navController.navigate("settings")
                        }
                    )
                }
                composable("places") {
                    PlacesScreen()
                }
                composable("settings") {

                }
            }
        }
    }
}