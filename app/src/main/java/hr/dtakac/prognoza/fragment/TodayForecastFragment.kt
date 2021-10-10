package hr.dtakac.prognoza.fragment

import android.text.format.DateUtils
import android.view.View
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.color.MaterialColors
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.TODAY_REQUEST_KEY
import hr.dtakac.prognoza.chart.FloatingDrawableEntry
import hr.dtakac.prognoza.chart.FloatingDrawableLineChartRenderer
import hr.dtakac.prognoza.databinding.FragmentTodayBinding
import hr.dtakac.prognoza.databinding.LayoutForecastOutdatedBinding
import hr.dtakac.prognoza.uimodel.cell.HourUiModel
import hr.dtakac.prognoza.uimodel.forecast.TemperatureUiModel
import hr.dtakac.prognoza.uimodel.forecast.TodayForecastUiModel
import hr.dtakac.prognoza.utils.*
import hr.dtakac.prognoza.viewmodel.TodayFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.IllegalStateException
import kotlin.math.ceil
import kotlin.math.floor

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

    private fun FragmentTodayBinding.bindTemperatureChart(temperatureData: List<TemperatureUiModel>) {
        val airTemperatureEntries = getAirTemperatureEntries(temperatureData)
        val airTemperatureLineDataSet = getAirTemperatureLineDataSet(airTemperatureEntries)
        val dataSets = listOf(airTemperatureLineDataSet)
        val lineData = LineData(dataSets)
        lcTemperature.renderer = FloatingDrawableLineChartRenderer(requireContext(), lcTemperature)
        lcTemperature.data = lineData
        lcTemperature.setVisibleXRangeMaximum(5f)
        lcTemperature.description.text = ""
        lcTemperature.setScaleEnabled(false)
        lcTemperature.legend.isEnabled = false
        lcTemperature.xAxis.apply {
            valueFormatter = getXAxisTimeFormatter(temperatureData)
            setDrawGridLines(false)
            labelCount = 5
            axisMinimum = -0.35f
            axisMaximum = temperatureData.lastIndex + 0.35f
            position = XAxis.XAxisPosition.BOTTOM
            axisLineWidth = 0.5.toPx
        }
        lcTemperature.axisLeft.apply {
            axisLineWidth = 0.5.toPx
            granularity = 0.5f
        }
        lcTemperature.axisRight.isEnabled = false
        lcTemperature.setExtraOffsets(0f, 8.toPx, 0f, 0f)
        lcTemperature.invalidate()
        cvChartTemperature.visibility = View.VISIBLE
    }

    private fun getAirTemperatureEntries(temperatureData: List<TemperatureUiModel>): List<FloatingDrawableEntry> {
        return temperatureData
            .filter {
                it.instantTemperature != null && it.weatherDescription != null
            }
            .mapIndexed { idx, uiModel ->
                FloatingDrawableEntry(
                    uiModel.weatherDescription!!.iconResourceId,
                    idx.toFloat(),
                    uiModel.instantTemperature!!.toFloat()
                )
            }
    }

    private fun getAirTemperatureLineDataSet(entries: List<FloatingDrawableEntry>): LineDataSet {
        return LineDataSet(entries, resources.getString(R.string.air_temperature)).apply {
            // prevents drawing point values
            setDrawValues(false)
            // thickens up the line
            lineWidth = 1.toPx
            // sets color of line and circles
            val lineColor =  MaterialColors.getColor(requireContext(), R.attr.colorAirTemperatureLine, null)
            color = lineColor
            setCircleColor(lineColor)
            // makes the line smooth
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
    }

    private fun getXAxisTimeFormatter(temperatureData: List<TemperatureUiModel>): ValueFormatter {
        return object : ValueFormatter() {
            val rangeOfValuesToDrawLabel = 0f..temperatureData.lastIndex.toFloat()

            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return if (value in rangeOfValuesToDrawLabel) DateUtils.formatDateTime(
                    requireContext(),
                    temperatureData[value.toInt()].startTime.toInstant().toEpochMilli(),
                    DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
                ) else ""
            }
        }
    }

    private fun getLeftAxisMinimum(temperatureData: List<TemperatureUiModel>): Float {
        val min = temperatureData.mapNotNull { it.instantTemperature?.toFloat() }.minOrNull()
            ?: throw IllegalStateException("No minimum value.")
        return floor(min) - 1f
    }

    private fun getLeftAxisMaximum(temperatureData: List<TemperatureUiModel>): Float {
        val max = temperatureData.mapNotNull { it.instantTemperature?.toFloat() }.maxOrNull()
            ?: throw IllegalStateException("No minimum value.")
        return ceil(max) + 1f
    }
}