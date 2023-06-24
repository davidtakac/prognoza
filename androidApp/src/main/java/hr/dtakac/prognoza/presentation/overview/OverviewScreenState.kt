package hr.dtakac.prognoza.presentation.overview

import hr.dtakac.prognoza.presentation.TextResource

data class OverviewScreenState(
    val loading: Boolean = false,
    val placeName: TextResource? = null,
    val error: TextResource? = null,
    val data: OverviewDataState? = null
)

data class OverviewDataState(
    val temperature: TextResource
)