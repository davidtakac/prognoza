package hr.dtakac.prognoza.ui.overview

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import hr.dtakac.prognoza.ui.theme.colors.DeepPurpleColorSchemeDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
  state: OverviewScreenState
) {
  var searchActive by remember { mutableStateOf(false) }
  val roundedCornerRadius = remember { 12.dp }
  MaterialTheme(colorScheme = DeepPurpleColorSchemeDark) {
    Scaffold(
      topBar = {
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Alignment.Center
        ) {
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
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
          top = contentPadding.calculateTopPadding() + 24.dp,
          start = contentPadding.calculateStartPadding(LocalLayoutDirection.current) + 24.dp,
          end = contentPadding.calculateEndPadding(LocalLayoutDirection.current) + 24.dp,
          bottom = contentPadding.calculateBottomPadding()
        )
      ) {
        item(key = "now", span = { GridItemSpan(2) }) {
          OverviewNow(
            temperature = state.data.now.temperature.asString(),
            maximumTemperature = state.data.now.maximumTemperature.asString(),
            minimumTemperature = state.data.now.minimumTemperature.asString(),
            weatherIcon = state.data.now.weatherIcon,
            weatherDescription = state.data.now.weatherDescription.asString(),
            feelsLikeTemperature = state.data.now.feelsLikeTemperature.asString(),
          )
        }
        item(key = "hours-heading", span = { GridItemSpan(2) }) {
          Text(
            text = stringResource(id = R.string.forecast_title_hourly),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 64.dp, bottom = 12.dp)
          )
        }
        item(key = "hours", span = { GridItemSpan(2) }) {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .height(128.dp),
            shape = RoundedCornerShape(roundedCornerRadius)
          ) {
            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(32.dp),
              contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 16.dp
              ),
            ) {
              items(state.data.hours) {
                when (it) {
                  is OverviewHourState.Sunrise -> OverviewSunriseHour(
                    time = it.time.asString(),
                    modifier = Modifier.fillMaxHeight()
                  )
                  is OverviewHourState.Sunset -> OverviewSunsetHour(
                    time = it.time.asString(),
                    modifier = Modifier.fillMaxHeight()
                  )
                  is OverviewHourState.Weather -> OverviewWeatherHour(
                    temperature = it.temperature.asString(),
                    pop = it.pop?.asString(),
                    weatherIcon = it.weatherIcon,
                    time = it.time.asString(),
                    modifier = Modifier.fillMaxHeight()
                  )
                }
              }
            }
          }
        }
        item(key = "coming-heading", span = { GridItemSpan(2) }) {
          Text(
            text = stringResource(
              id = R.string.forecast_title_coming,
              state.data.days.size
            ),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
          )
        }
        itemsIndexed(
          items = state.data.days,
          span = { _, _ -> GridItemSpan(2) }
        ) { idx, day ->
          OverviewDay(
            day = day.dayOfWeek.asString(),
            pop = day.pop?.asString(),
            weatherIcon = day.weatherIcon,
            minimumTemperature = day.minimumTemperature.asString(),
            maximumTemperature = day.maximumTemperature.asString(),
            temperatureBarStartFraction = day.temperatureBarStartFraction,
            temperatureBarEndFraction = day.temperatureBarEndFraction,
            currentTemperatureCenterFraction = day.currentTemperatureCenterFraction,
            shape = RoundedCornerShape(
              topStart = if (idx == 0) roundedCornerRadius else 4.dp,
              topEnd = if (idx == 0) roundedCornerRadius else 4.dp,
              bottomStart = if (idx == state.data.days.lastIndex) roundedCornerRadius else 4.dp,
              bottomEnd = if (idx == state.data.days.lastIndex) roundedCornerRadius else 4.dp
            ),
            modifier = Modifier
              .padding(top = 4.dp)
              .fillMaxWidth()
              .height(64.dp)
          )
        }
        item(
          key = "details-heading",
          span = { GridItemSpan(2) }
        ) {
          Text(
            text = stringResource(id = R.string.forecast_title_details),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 24.dp)
          )
        }
        state.data.details.forEach {
          item {
            val modifier = remember {
              Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
            }
            when (it) {
              is OverviewDetailState.Precipitation -> OverviewPrecipitation(
                amountInLastPeriod = it.amountInLastPeriod.asString(),
                hoursInLastPeriod = it.hoursInLastPeriod.asString(),
                nextExpected = it.nextExpected.asString(),
                isSnow = it.isSnow,
                modifier = modifier
              )
              is OverviewDetailState.UvIndex -> OverviewUvIndex(
                value = it.uvIndex.asString(),
                level = it.level.asString(),
                valueCenterFraction = it.valueCenterFraction,
                recommendations = it.recommendations.asString(),
                modifier = modifier
              )
              is OverviewDetailState.FeelsLike -> OverviewFeelsLike(
                value = it.feelsLike.asString(),
                description = it.description.asString(),
                modifier = modifier
              )
              is OverviewDetailState.Wind -> OverviewWind(
                speed = it.speed.asString(),
                maximumGust = it.maximumGust.asString(),
                windDirection = it.angle,
                modifier = modifier
              )
              is OverviewDetailState.Humidity -> OverviewHumidity(
                humidity = it.humidity.asString(),
                dewPoint = it.dewPoint.asString(),
                modifier = modifier
              )
            }
          }
        }
      }
    }
  }
}