package hr.dtakac.prognoza.common.util

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.databinding.CellDayBinding
import hr.dtakac.prognoza.forecast.uimodel.DayUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun CellDayBinding.bind(uiModel: DayUiModel) {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("EE, d LLLL", Locale.getDefault())
    val resources = root.context.resources
    tvDateTime.text = uiModel.time
        .withZoneSameInstant(ZoneId.systemDefault())
        .format(dateTimeFormatter)
    tvTemperatureHighLow.text =
        resources.formatTemperatureHighLow(uiModel.highTemperature, uiModel.lowTemperature)
    tvDescription.text =
        resources.formatWeatherIconDescriptionMostly(uiModel.weatherIcon?.descriptionResourceId)
    ivWeatherIcon.setImageResource(
        uiModel.weatherIcon?.iconResourceId ?: R.drawable.ic_cloud
    )
    tvPrecipitation.text = resources.formatTotalPrecipitation(uiModel.totalPrecipitationAmount)
}