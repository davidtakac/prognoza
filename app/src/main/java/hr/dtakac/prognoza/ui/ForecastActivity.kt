package hr.dtakac.prognoza.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import hr.dtakac.prognoza.presentation.TodayForecastViewModel

@AndroidEntryPoint
class ForecastActivity : ComponentActivity() {
    private val viewModel by viewModels<TodayForecastViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getState()
    }
}