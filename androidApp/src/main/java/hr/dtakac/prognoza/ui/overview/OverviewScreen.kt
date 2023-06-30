package hr.dtakac.prognoza.ui.overview

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.overview.OverviewHourState
import hr.dtakac.prognoza.presentation.overview.OverviewScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
    state: OverviewScreenState
) {
    var searchActive by remember { mutableStateOf(false) }
    MaterialTheme {
        Scaffold(
            topBar = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    SearchBar(
                        query = state.placeName?.asString() ?: "",
                        onQueryChange = {},
                        onSearch = {},
                        active = searchActive,
                        onActiveChange = { searchActive = it },
                        leadingIcon = {
                            Box(contentAlignment = Alignment.Center) {
                                // Crossfade and AnimatedContent glitch out with a weird sliding
                                // animation for some reason. This combination of two visibilities
                                // works fine
                                AnimatedVisibility(
                                    visible = searchActive,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    IconButton(onClick = { searchActive = false }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = null
                                        )
                                    }
                                }
                                AnimatedVisibility(
                                    visible = !searchActive,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.LocationOn,
                                        contentDescription = null
                                    )
                                }
                            }

                        },
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = !searchActive,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(onClick = { /*TODO: open menu*/ }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    ) {
                        Text("My cool search bar!")
                    }
                }
            }
        ) { contentPadding ->
            if (state.data == null) return@Scaffold
            LazyColumn(
                contentPadding = PaddingValues(
                    top = contentPadding.calculateTopPadding() + 24.dp,
                    start = contentPadding.calculateStartPadding(LocalLayoutDirection.current) + 24.dp,
                    end = contentPadding.calculateEndPadding(LocalLayoutDirection.current) + 24.dp,
                    bottom = contentPadding.calculateBottomPadding()
                )
            ) {
                item("now") {
                    OverviewNow(
                        temperature = state.data.now.temperature.asString(),
                        maximumTemperature = state.data.now.maximumTemperature.asString(),
                        minimumTemperature = state.data.now.minimumTemperature.asString(),
                        weatherIcon = state.data.now.weatherIcon,
                        weatherDescription = state.data.now.weatherDescription.asString(),
                        feelsLikeTemperature = state.data.now.feelsLikeTemperature.asString(),
                    )
                }
                item("hours-heading") {
                    Text(
                        text = stringResource(id = R.string.forecast_title_hourly),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 64.dp)
                    )
                }
                item("hours") {
                    Card(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 12.dp,
                                bottom = 16.dp
                            )
                        ) {
                            // TODO: do not filter out sunrises and sunsets.
                            //  You can just display sun for sunrise and moon for sunset for now
                            items(state.data.hours.filterIsInstance<OverviewHourState.Weather>()) {
                                OverviewHour(
                                    temperature = it.temperature.asString(),
                                    pop = it.pop?.asString(),
                                    weatherIcon = it.weatherIcon,
                                    time = it.time.asString()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}