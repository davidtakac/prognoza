package hr.dtakac.prognoza.forecast.di

import hr.dtakac.prognoza.forecast.viewmodel.ComingForecastViewModel
import hr.dtakac.prognoza.forecast.viewmodel.ForecastPagerViewModel
import hr.dtakac.prognoza.forecast.viewmodel.TodayForecastViewModel
import hr.dtakac.prognoza.forecast.viewmodel.TomorrowForecastViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val forecastModule = module {
    viewModel {
        ComingForecastViewModel(null, get(), get(), get(), get(), get())
    }

    viewModel {
        ForecastPagerViewModel(null, get(), get(), get())
    }

    viewModel {
        TodayForecastViewModel(null, get(), get(), get(), get(), get())
    }

    viewModel {
        TomorrowForecastViewModel(null, get(), get(), get(), get(), get())
    }
}