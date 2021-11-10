package hr.dtakac.prognoza.forecast.di

import hr.dtakac.prognoza.core.timeprovider.ComingForecastTimeProvider
import hr.dtakac.prognoza.core.timeprovider.TodayForecastTimeProvider
import hr.dtakac.prognoza.core.timeprovider.TomorrowForecastTimeProvider
import hr.dtakac.prognoza.forecast.viewmodel.ComingForecastViewModel
import hr.dtakac.prognoza.forecast.viewmodel.ForecastPagerViewModel
import hr.dtakac.prognoza.forecast.viewmodel.TodayForecastViewModel
import hr.dtakac.prognoza.forecast.viewmodel.TomorrowForecastViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val forecastModule = module {
    viewModel {
        ComingForecastViewModel(null, get(), get<ComingForecastTimeProvider>(), get(), get())
    }

    viewModel {
        ForecastPagerViewModel(null, get(), get())
    }

    viewModel {
        TodayForecastViewModel(null, get(), get<TodayForecastTimeProvider>(), get())
    }

    viewModel {
        TomorrowForecastViewModel(null, get(), get<TomorrowForecastTimeProvider>(), get())
    }
}