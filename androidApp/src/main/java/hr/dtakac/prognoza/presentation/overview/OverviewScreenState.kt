package hr.dtakac.prognoza.presentation.overview

import androidx.annotation.DrawableRes
import hr.dtakac.prognoza.presentation.TextResource

data class OverviewScreenState(
    val loading: Boolean = false,
    val placeName: TextResource? = null,
    val error: TextResource? = null,
    val data: OverviewDataState? = null
)

data class OverviewDataState(
    val temperature: TextResource,
    val maximumTemperature: TextResource,
    val minimumTemperature: TextResource,
    val feelsLikeTemperature: TextResource,
    @DrawableRes val weatherIcon: Int,
    val weatherDescription: TextResource,
    @DrawableRes val backgroundImage: Int
)