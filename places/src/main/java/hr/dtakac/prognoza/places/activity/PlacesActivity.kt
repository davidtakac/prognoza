package hr.dtakac.prognoza.places.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.places.composable.Places
import hr.dtakac.prognoza.places.viewmodel.PlacesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlacesActivity : ComponentActivity() {
    private val placesViewModel: PlacesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrognozaTheme {
                Screen()
            }
        }
    }

    @Composable
    fun Screen() {
        val closePlaces by placesViewModel.closePlaces
        if (closePlaces) {
            finish()
        }
        Places(
            placesViewModel = placesViewModel,
            onBackClicked = { finish() }
        )
    }
}