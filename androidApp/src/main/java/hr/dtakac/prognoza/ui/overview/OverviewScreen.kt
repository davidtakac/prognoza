package hr.dtakac.prognoza.ui.overview

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.presentation.overview.OverviewScreenState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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
        ) {
            if (state.data == null) return@Scaffold
            LazyColumn(
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 24.dp,
                    start = it.calculateStartPadding(LocalLayoutDirection.current) + 24.dp,
                    end = it.calculateEndPadding(LocalLayoutDirection.current) + 24.dp,
                    bottom = it.calculateBottomPadding()
                )
            ) {
                item("now") {
                    OverviewNow(
                        temperature = state.data.temperature.asString(),
                        maximumTemperature = state.data.maximumTemperature.asString(),
                        minimumTemperature = state.data.minimumTemperature.asString(),
                        weatherIcon = state.data.weatherIcon,
                        weatherDescription = state.data.weatherDescription.asString(),
                        feelsLikeTemperature = state.data.feelsLikeTemperature.asString(),
                        backgroundImage = state.data.backgroundImage
                    )
                }
            }
        }
    }
}