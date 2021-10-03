package hr.dtakac.prognoza.fragment

import android.text.format.DateUtils
import android.view.View
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.TODAY_REQUEST_KEY
import hr.dtakac.prognoza.databinding.FragmentTodayBinding
import hr.dtakac.prognoza.databinding.LayoutForecastOutdatedBinding
import hr.dtakac.prognoza.uimodel.cell.HourUiModel
import hr.dtakac.prognoza.uimodel.forecast.TemperatureUiModel
import hr.dtakac.prognoza.uimodel.forecast.TodayForecastUiModel
import hr.dtakac.prognoza.utils.formatFeelsLike
import hr.dtakac.prognoza.utils.formatPrecipitationValue
import hr.dtakac.prognoza.utils.formatTemperatureValue
import hr.dtakac.prognoza.utils.formatWeatherIconDescription
import hr.dtakac.prognoza.viewmodel.TodayFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZonedDateTime

class TodayForecastFragment :
    ForecastFragment<TodayForecastUiModel, FragmentTodayBinding>(FragmentTodayBinding::inflate) {
    override val emptyForecastBinding get() = binding.emptyScreen
    override val progressBar get() = binding.progressBar
    override val outdatedForecastBinding: LayoutForecastOutdatedBinding get() = binding.cachedDataMessage
    override val requestKey get() = TODAY_REQUEST_KEY
    override val viewModel by viewModel<TodayFragmentViewModel>()

    override fun showForecast(uiModel: TodayForecastUiModel) {
        binding.bindCurrentHour(uiModel.currentHour)
        binding.bindTemperatureChart(uiModel.temperatureData)
    }

    private fun FragmentTodayBinding.bindCurrentHour(currentHour: HourUiModel) {
        val time = DateUtils.formatDateTime(
            requireContext(),
            currentHour.time.toInstant().toEpochMilli(),
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
        )
        tvDateTime.text = time
        tvTemperature.text =
            requireContext().formatTemperatureValue(
                currentHour.temperature,
                currentHour.displayDataInUnit
            )
        Glide.with(this@TodayForecastFragment)
            .load(currentHour.weatherDescription?.iconResourceId)
            .fallback(R.drawable.ic_cloud_off)
            .into(ivWeatherIcon)
        tvFeelsLike.text =
            requireContext().formatFeelsLike(
                currentHour.feelsLike,
                currentHour.displayDataInUnit
            )
        tvDescription.text =
            if (currentHour.precipitationAmount != null && currentHour.precipitationAmount > 0) {
                requireContext().formatPrecipitationValue(
                    currentHour.precipitationAmount,
                    currentHour.displayDataInUnit
                )
            } else {
                requireContext()
                    .formatWeatherIconDescription(
                        currentHour.weatherDescription?.descriptionResourceId
                    )
            }
        cvCurrentHour.visibility = View.VISIBLE
    }

    private fun FragmentTodayBinding.bindTemperatureChart(temperatureData: Map<ZonedDateTime, TemperatureUiModel>) {
        val airTemperatureEntries = mutableListOf<Entry>()
        temperatureData.forEach {
            airTemperatureEntries.add(
                Entry(
                    it.key.toInstant().toEpochMilli().toFloat(),
                    it.value.airTemperature!!.toFloat()
                )
            )
        }
        val airTemperatureLineSetData = LineDataSet(airTemperatureEntries, "Air temperature")
        airTemperatureLineSetData.axisDependency = YAxis.AxisDependency.LEFT
        val dataSets = listOf(airTemperatureLineSetData)
        val lineData = LineData(dataSets)
        lcTemperature.data = lineData
        lcTemperature.invalidate()
        cvChartTemperature.visibility = View.VISIBLE
    }
}