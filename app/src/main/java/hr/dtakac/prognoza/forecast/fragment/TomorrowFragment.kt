package hr.dtakac.prognoza.forecast.fragment

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.common.TOMORROW_REQUEST_KEY
import hr.dtakac.prognoza.common.util.formatRepresentativeWeatherIconDescription
import hr.dtakac.prognoza.common.util.formatTemperatureValue
import hr.dtakac.prognoza.common.util.formatTotalPrecipitation
import hr.dtakac.prognoza.databinding.FragmentTomorrowBinding
import hr.dtakac.prognoza.forecast.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.TomorrowForecastUiModel
import hr.dtakac.prognoza.forecast.viewmodel.TomorrowFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class TomorrowFragment :
    BaseForecastFragment<TomorrowForecastUiModel, FragmentTomorrowBinding>(FragmentTomorrowBinding::inflate) {
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
        val dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, d LLLL", Locale.getDefault())
        binding.tvDateTime.text = uiModel.summary.time
            .withZoneSameInstant(ZoneId.systemDefault())
            .format(dateTimeFormatter)
        binding.tvTemperatureHigh.text =
            resources.formatTemperatureValue(uiModel.summary.highTemperature)
        binding.tvTemperatureLow.text =
            resources.formatTemperatureValue(uiModel.summary.lowTemperature)
        binding.tvDescription.text =
            resources.formatRepresentativeWeatherIconDescription(uiModel.summary.representativeWeatherIcon)
        binding.ivWeatherIcon.setImageResource(
            uiModel.summary.representativeWeatherIcon?.weatherIcon?.iconResourceId
                ?: R.drawable.ic_cloud
        )
        binding.tvPrecipitation.text =
            resources.formatTotalPrecipitation(uiModel.summary.totalPrecipitationAmount)
        adapter.submitList(uiModel.hours)
    }
}