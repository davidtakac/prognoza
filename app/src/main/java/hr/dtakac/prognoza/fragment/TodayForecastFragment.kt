package hr.dtakac.prognoza.fragment

import android.text.format.DateUtils
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.TODAY_REQUEST_KEY
import hr.dtakac.prognoza.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.databinding.FragmentTodayBinding
import hr.dtakac.prognoza.extensions.formatFeelsLike
import hr.dtakac.prognoza.extensions.formatPrecipitationTwoHours
import hr.dtakac.prognoza.extensions.formatTemperatureValue
import hr.dtakac.prognoza.uimodel.forecast.TodayForecastUiModel
import hr.dtakac.prognoza.viewmodel.TodayFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TodayForecastFragment :
    ForecastFragment<TodayForecastUiModel, FragmentTodayBinding>(FragmentTodayBinding::inflate) {
    override val emptyForecastBinding get() = binding.emptyScreen
    override val progressBar get() = binding.progressBar
    override val recyclerView get() = binding.rvHours
    override val requestKey get() = TODAY_REQUEST_KEY
    override val viewModel by viewModel<TodayFragmentViewModel>()

    private val adapter = HoursRecyclerViewAdapter()

    override fun showForecast(uiModel: TodayForecastUiModel) {
        val currentConditions = uiModel.currentConditionsModel
        val currentHour = currentConditions.currentHour
        val time = DateUtils.formatDateTime(
            requireContext(),
            currentHour.time.toInstant().toEpochMilli(),
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
        )
        binding.tvDateTime.text = time
        binding.tvTemperature.text =
            resources.formatTemperatureValue(currentHour.temperature, currentHour.displayDataInUnit)
        binding.ivWeatherIcon.setImageResource(
            currentHour.weatherDescription?.iconResourceId ?: R.drawable.ic_cloud
        )
        binding.tvPrecipitationForecast.text =
            resources.formatPrecipitationTwoHours(
                currentConditions.precipitationForecast,
                currentConditions.displayDataInUnit
            )
        binding.tvFeelsLike.text =
            resources.formatFeelsLike(currentHour.feelsLike, currentHour.displayDataInUnit)
        adapter.submitList(uiModel.otherHours)
    }

    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        recyclerView.adapter = adapter
    }
}