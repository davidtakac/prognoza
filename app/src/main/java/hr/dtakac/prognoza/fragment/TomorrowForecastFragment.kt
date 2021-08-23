package hr.dtakac.prognoza.fragment

import android.text.format.DateUtils
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.TOMORROW_REQUEST_KEY
import hr.dtakac.prognoza.extensions.formatRepresentativeWeatherIconDescription
import hr.dtakac.prognoza.extensions.formatTemperatureValue
import hr.dtakac.prognoza.extensions.formatTotalPrecipitation
import hr.dtakac.prognoza.databinding.FragmentTomorrowBinding
import hr.dtakac.prognoza.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.uimodel.forecast.TomorrowForecastUiModel
import hr.dtakac.prognoza.viewmodel.TomorrowFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TomorrowForecastFragment :
    ForecastFragment<TomorrowForecastUiModel, FragmentTomorrowBinding>(FragmentTomorrowBinding::inflate) {
    override val emptyForecastBinding get() = binding.emptyScreen
    override val progressBar get() = binding.progressBar
    override val recyclerView get() = binding.rvHours
    override val requestKey get() = TOMORROW_REQUEST_KEY
    override val viewModel by viewModel<TomorrowFragmentViewModel>()

    private val adapter = HoursRecyclerViewAdapter()

    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        recyclerView.adapter = adapter
    }

    override fun showForecast(uiModel: TomorrowForecastUiModel) {
        val resources = binding.root.context.resources
        val summary = uiModel.summary
        val time = DateUtils.formatDateTime(
            requireContext(),
            summary.time.toInstant().toEpochMilli(),
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY
        )
        binding.tvDateTime.text = time
        binding.tvTemperatureHigh.text =
            resources.formatTemperatureValue(summary.highTemperature, summary.unit)
        binding.tvTemperatureLow.text =
            resources.formatTemperatureValue(summary.lowTemperature, summary.unit)
        binding.tvDescription.text =
            resources.formatRepresentativeWeatherIconDescription(summary.representativeWeatherDescription)
        binding.ivWeatherIcon.setImageResource(
            summary.representativeWeatherDescription?.weatherDescription?.iconResourceId
                ?: R.drawable.ic_cloud
        )
        binding.tvPrecipitation.text =
            resources.formatTotalPrecipitation(summary.totalPrecipitationAmount, summary.unit)
        adapter.submitList(uiModel.hours)
    }
}