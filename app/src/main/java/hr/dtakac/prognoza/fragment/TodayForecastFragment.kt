package hr.dtakac.prognoza.fragment

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.TODAY_REQUEST_KEY
import hr.dtakac.prognoza.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.databinding.FragmentTodayBinding
import hr.dtakac.prognoza.databinding.LayoutOutdatedForecastBinding
import hr.dtakac.prognoza.extensions.formatFeelsLike
import hr.dtakac.prognoza.extensions.formatTemperatureValue
import hr.dtakac.prognoza.extensions.formatWeatherIconDescription
import hr.dtakac.prognoza.uimodel.forecast.TodayForecastUiModel
import hr.dtakac.prognoza.viewmodel.TodayFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TodayForecastFragment :
    ForecastFragment<TodayForecastUiModel, FragmentTodayBinding>(FragmentTodayBinding::inflate) {
    override val emptyForecastBinding get() = binding.emptyScreen
    override val progressBar get() = binding.progressBar
    override val outdatedForecastBinding: LayoutOutdatedForecastBinding get() = binding.cachedDataMessage
    override val requestKey get() = TODAY_REQUEST_KEY
    override val viewModel by viewModel<TodayFragmentViewModel>()

    private val adapter = HoursRecyclerViewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
    }

    override fun showForecast(uiModel: TodayForecastUiModel) {
        val currentConditions = uiModel.currentConditionsUiModel
        val currentHour = currentConditions.currentHour
        val time = DateUtils.formatDateTime(
            requireContext(),
            currentHour.time.toInstant().toEpochMilli(),
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
        )
        with(binding) {
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
            tvDescription.text =
                requireContext().formatWeatherIconDescription(currentHour.weatherDescription?.descriptionResourceId)
            tvFeelsLike.text =
                requireContext().formatFeelsLike(
                    currentHour.feelsLike,
                    currentHour.displayDataInUnit
                )
            cvCurrentHour.visibility = View.VISIBLE
        }
        adapter.submitList(uiModel.otherHours)
    }

    private fun initializeRecyclerView() {
        with(binding.rvHours) {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = this@TodayForecastFragment.adapter
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