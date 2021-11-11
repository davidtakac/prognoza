package hr.dtakac.prognoza.forecast.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.google.accompanist.pager.ExperimentalPagerApi
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.forecast.composable.forecastpager.ForecastTabbedPager
import hr.dtakac.prognoza.forecast.viewmodel.*
import hr.dtakac.prognoza.places.activity.PlacesActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForecastActivity : ComponentActivity() {
    private val todayViewModel by viewModel<TodayForecastViewModel>()
    private val tomorrowViewModel by viewModel<TomorrowForecastViewModel>()
    private val comingViewModel by viewModel<ComingForecastViewModel>()
    private val forecastPagerViewModel by viewModel<ForecastPagerViewModel>()

    private var currentPage: Int = 0

    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrognozaTheme {
                Screen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getCurrentForecastViewModel(currentPage).getForecast()
        forecastPagerViewModel.getData()
    }

    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    @Composable
    fun Screen() {
        Box {
            ForecastTabbedPager(
                todayForecastViewModel = todayViewModel,
                tomorrowForecastViewModel = tomorrowViewModel,
                comingForecastViewModel = comingViewModel,
                forecastPagerViewModel = forecastPagerViewModel,
                onSearchClicked = { openPlaces() },
                onPageChanged = { handleChangedPage(newPage = it) },
                onSettingsClicked = { /*TODO*/ }
            )
        }
    }

    private fun openPlaces() {
        val intent = Intent(this, PlacesActivity::class.java)
        startActivity(intent)
    }

    private fun handleChangedPage(newPage: Int) {
        getCurrentForecastViewModel(newPage).getForecast()
        currentPage = newPage
    }

    private fun getCurrentForecastViewModel(page: Int): ForecastViewModel {
        return when (page) {
            0 -> todayViewModel
            1 -> tomorrowViewModel
            2 -> comingViewModel
            else -> throw IllegalStateException("No ViewModel for page $page.")
        }
    }
}