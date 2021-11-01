package hr.dtakac.prognoza.fragment

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.TOMORROW_REQUEST_KEY
import hr.dtakac.prognoza.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.databinding.FragmentTomorrowBinding
import hr.dtakac.prognoza.databinding.LayoutForecastOutdatedBinding
import hr.dtakac.prognoza.utils.formatRepresentativeWeatherIconDescription
import hr.dtakac.prognoza.utils.formatTemperatureValue
import hr.dtakac.prognoza.utils.formatTotalPrecipitation
import hr.dtakac.prognoza.uimodel.forecast.TomorrowForecastUiModel
import hr.dtakac.prognoza.viewmodel.TomorrowViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TomorrowForecastFragment :
    ForecastFragment<TomorrowForecastUiModel, FragmentTomorrowBinding>(FragmentTomorrowBinding::inflate) {
    override val emptyForecastBinding get() = binding.emptyScreen
    override val progressBar get() = binding.progressBar
    override val outdatedForecastBinding: LayoutForecastOutdatedBinding get() = binding.cachedDataMessage
    override val requestKey get() = TOMORROW_REQUEST_KEY
    override val viewModel by viewModel<TomorrowViewModel>()

    private val adapter = HoursRecyclerViewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
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
                requireContext().formatTemperatureValue(
                    summary.highTemperature,
                    summary.displayDataInUnit
                )
            tvTemperatureLow.text =
                requireContext().formatTemperatureValue(
                    summary.lowTemperature,
                    summary.displayDataInUnit
                )
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
            cvSummary.visibility = View.VISIBLE
        }
        adapter.submitList(uiModel.hours)
    }

    private fun initializeRecyclerView() {
        with(binding.rvHours) {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = this@TomorrowForecastFragment.adapter
            addItemDecoration(MarginItemDecoration())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }
}