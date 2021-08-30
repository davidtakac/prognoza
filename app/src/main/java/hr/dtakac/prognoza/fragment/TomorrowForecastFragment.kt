package hr.dtakac.prognoza.fragment

import android.text.format.DateUtils
import com.bumptech.glide.Glide
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.TOMORROW_REQUEST_KEY
import hr.dtakac.prognoza.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.databinding.FragmentTomorrowBinding
import hr.dtakac.prognoza.databinding.LayoutOutdatedForecastBinding
import hr.dtakac.prognoza.extensions.formatRepresentativeWeatherIconDescription
import hr.dtakac.prognoza.extensions.formatTemperatureValue
import hr.dtakac.prognoza.extensions.formatTotalPrecipitation
import hr.dtakac.prognoza.uimodel.forecast.TomorrowForecastUiModel
import hr.dtakac.prognoza.viewmodel.TomorrowFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TomorrowForecastFragment :
    ForecastFragment<TomorrowForecastUiModel, FragmentTomorrowBinding>(FragmentTomorrowBinding::inflate) {
    override val emptyForecastBinding get() = binding.emptyScreen
    override val progressBar get() = binding.progressBar
    override val outdatedForecastBinding: LayoutOutdatedForecastBinding get() = binding.cachedDataMessage
    override val recyclerView get() = binding.rvHours
    override val requestKey get() = TOMORROW_REQUEST_KEY
    override val viewModel by viewModel<TomorrowFragmentViewModel>()

    private val adapter = HoursRecyclerViewAdapter()

    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        recyclerView.adapter = adapter
    }

    override fun showForecast(uiModel: TomorrowForecastUiModel) {
        val summary = uiModel.summary
        val time = DateUtils.formatDateTime(
            requireContext(),
            summary.time.toInstant().toEpochMilli(),
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY
        )
        with(binding) {
            tvDateTime.text = time
            tvTemperatureHigh.text =
                requireContext().formatTemperatureValue(summary.highTemperature, summary.displayDataInUnit)
            tvTemperatureLow.text =
                requireContext().formatTemperatureValue(summary.lowTemperature, summary.displayDataInUnit)
            tvDescription.text =
                requireContext().formatRepresentativeWeatherIconDescription(summary.representativeWeatherDescription)
            Glide.with(this@TomorrowForecastFragment)
                .load(summary.representativeWeatherDescription?.weatherDescription?.iconResourceId)
                .fallback(R.drawable.ic_cloud_off)
                .into(ivWeatherIcon)
            tvPrecipitation.text =
                binding.root.context.formatTotalPrecipitation(
                    summary.totalPrecipitationAmount,
                    summary.displayDataInUnit
                )
        }
        adapter.submitList(uiModel.hours)
    }
}