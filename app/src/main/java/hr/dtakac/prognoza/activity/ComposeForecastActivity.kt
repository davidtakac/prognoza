package hr.dtakac.prognoza.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import com.google.accompanist.pager.ExperimentalPagerApi
import hr.dtakac.prognoza.theme.AppTheme
import hr.dtakac.prognoza.composable.forecast.TabbedForecastPager
import hr.dtakac.prognoza.viewmodel.TodayForecastViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeForecastActivity : ComponentActivity() {
    private val todayViewModel by viewModel<TodayForecastViewModel>()

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

    override fun onResume() {
        super.onResume()
        todayViewModel.getForecast()
    }

    @ExperimentalPagerApi
    @Composable
    fun Screen() {
        Box {
            TabbedForecastPager(
                todayForecastViewModel = todayViewModel
            )
        }
    }
}