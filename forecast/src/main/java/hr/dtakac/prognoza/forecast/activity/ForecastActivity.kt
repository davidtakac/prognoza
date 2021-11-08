package hr.dtakac.prognoza.forecast.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.google.accompanist.pager.ExperimentalPagerApi
import hr.dtakac.prognoza.core.theme.AppTheme
import hr.dtakac.prognoza.forecast.composable.forecastpager.ForecastTabbedPager
import hr.dtakac.prognoza.forecast.viewmodel.ComingForecastViewModel
import hr.dtakac.prognoza.forecast.viewmodel.ForecastPagerViewModel
import hr.dtakac.prognoza.forecast.viewmodel.TodayForecastViewModel
import hr.dtakac.prognoza.forecast.viewmodel.TomorrowForecastViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForecastActivity : ComponentActivity() {
    private val todayViewModel by viewModel<TodayForecastViewModel>()
    private val tomorrowViewModel by viewModel<TomorrowForecastViewModel>()
    private val comingViewModel by viewModel<ComingForecastViewModel>()
    private val forecastPagerViewModel by viewModel<ForecastPagerViewModel>()

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todayViewModel.getForecast()
        setContent {
            AppTheme {
                Screen()
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    fun Screen() {
        Box {
            ForecastTabbedPager(
                todayForecastViewModel = todayViewModel,
                tomorrowForecastViewModel = tomorrowViewModel,
                comingForecastViewModel = comingViewModel,
                forecastPagerViewModel = forecastPagerViewModel
            )
        }
    }
}