package hr.dtakac.prognoza.places.di

import hr.dtakac.prognoza.places.viewmodel.PlacesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val placesModule = module {
    viewModel {
        PlacesViewModel(null, get(), get(), get(), get())
    }
}