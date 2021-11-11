package hr.dtakac.prognoza.places.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.places.composable.Places
import hr.dtakac.prognoza.places.viewmodel.PlacesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlacesActivity : ComponentActivity() {
    private val placesViewModel: PlacesViewModel by viewModel()

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrognozaTheme {
                Screen()
            }
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @Composable
    fun Screen() {
        val closePlaces by placesViewModel.closePlaces
        if (closePlaces) {
            finish()
        }

        val places by placesViewModel.places
        val isLoading by placesViewModel.isLoading
        val message by placesViewModel.message
        val emptyPlaces by placesViewModel.emptyPlaces
        val selectedPlaces = placesViewModel.selectedPlaces
        val haptic = LocalHapticFeedback.current

        Places(
            places = places,
            isLoading = isLoading,
            message = message,
            emptyPlaces = emptyPlaces,
            onBackClicked = { finish() },
            onSearchClicked = { placesViewModel.search(it) },
            onClearSearch = { placesViewModel.showSavedPlaces() },
            onPlaceClicked = { placesViewModel.handlePlaceClicked(it) },
            onPlaceLongClicked = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                placesViewModel.handlePlaceSelected(it)
            },
            onDeletePlacesClicked = { placesViewModel.deletePlaces(selectedPlaces) },
            onPlaceSelectionCancelled = { placesViewModel.clearPlaceSelection() },
            selectedPlaces = selectedPlaces
        )
    }
}