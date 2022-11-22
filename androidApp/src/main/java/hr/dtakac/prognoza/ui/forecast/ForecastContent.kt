package hr.dtakac.prognoza.ui.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.forecast.*
import hr.dtakac.prognoza.shared.entity.Description
import hr.dtakac.prognoza.shared.entity.Mood
import hr.dtakac.prognoza.ui.common.*
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.theme.AppTheme

@Composable
fun ForecastContent(
    state: ForecastState,
    onMenuClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrognozaTheme.colors.surface1)
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        val toolbarState = rememberAppToolbarState()
        AppToolbarWithLoadingIndicator(
            state = toolbarState,
            placeName = state.forecast?.current?.place?.asString() ?: "",
            date = state.forecast?.current?.date?.asString() ?: "",
            temperature = state.forecast?.current?.temperature?.asString() ?: "",
            isLoading = state.isLoading,
            onNavigationClick = onMenuClick
        )
        if (state.forecast == null) {
            if (state.error != null) {
                ForecastFullScreenError(
                    error = state.error.asString(),
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize()
                )
            }
        } else {
            Box {
                ForecastDataList(
                    data = state.forecast,
                    isPlaceVisible = { toolbarState.setTitleVisible(!it) },
                    isDateVisible = { toolbarState.setSubtitleVisible(!it) },
                    isTemperatureVisible = { toolbarState.setEndVisible(!it) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (state.error != null) {
                    ForecastSnackBarError(
                        error = state.error.asString(),
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayScreenPreview(
    current: CurrentUi = fakeCurrentUi(),
    today: TodayUi? = fakeTodayUi(),
    coming: List<ComingDayUi> = fakeComingUi()
) = AppTheme(mood = Mood.CLEAR) {
    Box(modifier = Modifier.background(PrognozaTheme.colors.surface1)) {
        ForecastDataList(
            data = ForecastUi(
                current = current,
                today = today,
                coming = coming
            )
        )
    }
}

@Preview
@Composable
private fun AllVisiblePreview() = TodayScreenPreview()

@Preview
@Composable
private fun TodayScreenJustCurrentPreview() = TodayScreenPreview(
    today = null,
    coming = listOf()
)

@Preview
@Composable
private fun TodayScreenCurrentPlusTodayPreview() = TodayScreenPreview(
    coming = listOf()
)

@Preview
@Composable
private fun TodayScreenCurrentPlusComingPreview() = TodayScreenPreview(
    today = null
)

private fun fakeCurrentUi(): CurrentUi = CurrentUi(
    place = TextResource.fromString("Tenja"),
    date = TextResource.fromString("September 12"),
    temperature = TextResource.fromString("1°"),
    description = TextResource.fromString("Clear sky, sleet soon"),
    weatherIconDescription = Description.CLEAR_SKY_DAY,
    wind = TextResource.fromString("Wind: 15 km/h"),
    feelsLike = TextResource.fromString("Feels like: -21°"),
    mood = Mood.CLEAR,
    precipitation = TextResource.fromString("12 mm")
)

private fun fakeTodayUi(): TodayUi = TodayUi(
    lowHighTemperature = TextResource.fromString("135°—197°"),
    hourly = mutableListOf<DayHourUi>().apply {
        for (i in 1..4) {
            add(
                DayHourUi(
                    time = TextResource.fromString("14:00"),
                    temperature = TextResource.fromString("23°"),
                    precipitation = TextResource.fromString("1.99 mm"),
                    description = TextResource.fromString("Clear and some more text"),
                    weatherIconDescription = if (i % 2 == 0) Description.CLEAR_SKY_DAY else Description.UNKNOWN
                )
            )
        }
    }
)

private fun fakeComingUi(): List<ComingDayUi> = listOf(
    ComingDayUi(
        date = TextResource.fromString("Thu, Sep 13"),
        lowHighTemperature = TextResource.fromString("16—8"),
        precipitation = TextResource.empty(),
        hours = mutableListOf<ComingDayHourUi>().apply {
            for (i in 1..12) {
                add(
                    ComingDayHourUi(
                        time = TextResource.fromString("$i:00"),
                        temperature = TextResource.fromString("20"),
                        weatherIconDescription = Description.CLEAR_SKY_DAY
                    )
                )
            }
        }
    ),
    ComingDayUi(
        date = TextResource.fromString("Fri, Sep 14"),
        lowHighTemperature = TextResource.fromString("18—8"),
        precipitation = TextResource.fromString("0.7 mm"),
        hours = listOf()
    ),
    ComingDayUi(
        date = TextResource.fromString("Sat, Sep 15"),
        lowHighTemperature = TextResource.fromString("21—5"),
        precipitation = TextResource.empty(),
        hours = listOf()
    )
)