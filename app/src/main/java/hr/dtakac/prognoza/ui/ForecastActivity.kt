package hr.dtakac.prognoza.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import dagger.hilt.android.AndroidEntryPoint
import hr.dtakac.prognoza.presentation.today.TodayViewModel
import hr.dtakac.prognoza.ui.today.TodayScreen

@AndroidEntryPoint
class ForecastActivity : ComponentActivity() {
    private val viewModel by viewModels<TodayViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by remember { viewModel.state }
            TodayScreen(state)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getState()
    }
}