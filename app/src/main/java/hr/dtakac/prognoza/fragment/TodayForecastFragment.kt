package hr.dtakac.prognoza.fragment

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.common.TODAY_REQUEST_KEY
import hr.dtakac.prognoza.extensions.formatPrecipitationTwoHours
import hr.dtakac.prognoza.extensions.formatTemperatureValue
import hr.dtakac.prognoza.databinding.FragmentTodayBinding
import hr.dtakac.prognoza.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.uimodel.TodayForecastUiModel
import hr.dtakac.prognoza.viewmodel.TodayFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.format.DateTimeFormatter
import java.util.*

class TodayForecastFragment :
    BaseForecastFragment<TodayForecastUiModel, FragmentTodayBinding>(FragmentTodayBinding::inflate) {
    override val emptyForecastBinding get() = binding.emptyScreen
    override val progressBar get() = binding.progressBar
    override val recyclerView get() = binding.rvHours
    override val requestKey get() = TODAY_REQUEST_KEY
    override val viewModel by viewModel<TodayFragmentViewModel>()

    private val adapter = HoursRecyclerViewAdapter()

    override fun showForecast(uiModel: TodayForecastUiModel) {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("d LLLL, HH:mm", Locale.getDefault())
        val currentHour = uiModel.currentHour
        binding.tvDateTime.text = currentHour.time.format(dateTimeFormatter)
        binding.tvTemperature.text = resources.formatTemperatureValue(currentHour.temperature)
        binding.ivWeatherIcon.setImageResource(
            currentHour.weatherIcon?.iconResourceId ?: R.drawable.ic_cloud
        )
        binding.tvPrecipitationForecast.text =
            resources.formatPrecipitationTwoHours(uiModel.precipitationForecast)
        binding.tvFeelsLike.text = resources.getString(
            R.string.template_feels_like,
            if (currentHour.feelsLike == null) {
                resources.getString(R.string.placeholder_temperature)
            } else {
                resources.formatTemperatureValue(currentHour.feelsLike)
            }
        )
        adapter.submitList(uiModel.otherHours)
    }

    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        recyclerView.adapter = adapter
    }
}