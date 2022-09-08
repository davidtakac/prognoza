package hr.dtakac.prognoza.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import dagger.hilt.android.AndroidEntryPoint
import hr.dtakac.prognoza.presentation.today.TodayUiState
import hr.dtakac.prognoza.presentation.today.TodayViewModel
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@AndroidEntryPoint
class ForecastActivity : ComponentActivity() {
    private val viewModel by viewModels<TodayViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getState()
        setContent {
            PrognozaTheme {
                Scaffold {
                    val state by viewModel.state
                    when (val s = state) {
                        is TodayUiState.Empty -> Empty(s)
                        TodayUiState.Loading -> Loading()
                        is TodayUiState.Success -> Content(s)
                    }
                }
            }
        }
    }

    @Composable
    fun Empty(state: TodayUiState.Empty) {
        Text(state.text.asString(LocalContext.current))
    }

    @Composable
    fun Loading() {
        CircularProgressIndicator()
    }

    @Composable
    fun Content(state: TodayUiState.Success) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(state.placeName.asString())
            Text(state.time.asString())
            Text(state.temperature.asString())
            Text(state.feelsLike.asString())
            Text(state.wind.asString())
            Image(painter = painterResource(id = state.descriptionIcon), contentDescription = null)
            Text(state.description.asString())
            Text(state.highTemperature.asString())
            Text(state.lowTemperature.asString())
            Text(state.precipitation.asString())
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(state.hours) { hour ->
                    Column(modifier = Modifier.fillMaxHeight()) {
                        Text(hour.time.asString())
                        Image(painter = painterResource(id = hour.icon), contentDescription = null)
                        Text(hour.temperature.asString())
                        hour.precipitation?.let { Text(it.asString()) }
                    }
                }
            }
        }
    }
}