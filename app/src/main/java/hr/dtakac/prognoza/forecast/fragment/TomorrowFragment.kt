package hr.dtakac.prognoza.forecast.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dtakac.prognoza.IMAGE_PLACEHOLDER
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.database.entity.isPrecipitationAmountSignificant
import hr.dtakac.prognoza.database.entity.isWindSpeedSignificant
import hr.dtakac.prognoza.databinding.FragmentTomorrowBinding
import hr.dtakac.prognoza.forecast.adapter.ForecastItemDecoration
import hr.dtakac.prognoza.forecast.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.DayUiModel
import hr.dtakac.prognoza.forecast.uimodel.TomorrowUiModel
import hr.dtakac.prognoza.forecast.viewmodel.TomorrowViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.format.DateTimeFormatter
import java.util.*

class TomorrowFragment :
    ViewBindingFragment<FragmentTomorrowBinding>(FragmentTomorrowBinding::inflate) {
    private val adapter = HoursRecyclerViewAdapter()
    private val viewModel by viewModel<TomorrowViewModel>()
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("EE, d LLLL", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
        initializeTryAgain()
    }

    override fun onResume() {
        super.onResume()
        if (binding.error.root.visibility != View.VISIBLE) {
            viewModel.getTomorrowForecast()
        }
    }

    private fun observeViewModel() {
        viewModel.tomorrowForecast.observe(viewLifecycleOwner) {
            when (it) {
                is TomorrowUiModel.Success -> showForecast(it)
                is TomorrowUiModel.Error -> showError(it)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.apply {
                if (isLoading) show() else hide()
            }
            binding.error.progressBar.apply {
                if (isLoading) show() else hide()
            }
        }
    }

    private fun initializeRecyclerView() {
        binding.rvHours.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvHours.adapter = adapter
        binding.rvHours.addItemDecoration(ForecastItemDecoration())
        binding.rvHours.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun initializeTryAgain() {
        binding.error.btnTryAgain.setOnClickListener {
            viewModel.getTomorrowForecast()
        }
    }

    private fun showForecast(uiModel: TomorrowUiModel.Success) {
        populateSummaryViews(uiModel.summary)
        adapter.submitList(uiModel.hours)
        binding.error.root.visibility = View.GONE
    }

    private fun showError(uiModel: TomorrowUiModel.Error) {
        binding.error.tvErrorMessage.text = resources.getString(uiModel.errorMessageResourceId)
        binding.error.root.visibility = View.VISIBLE
    }

    private fun populateSummaryViews(summary: DayUiModel) {
        binding.tvDateTime.text = summary.time.format(dateTimeFormatter)
        binding.tvTemperatureHigh.text = resources.getString(
            R.string.template_temperature,
            summary.highTemperature
        )
        binding.tvTemperatureLow.text = resources.getString(
            R.string.template_temperature,
            summary.lowTemperature
        )
        binding.ivWeatherIcon.setImageResource(
            summary.weatherIcon?.iconResourceId ?: IMAGE_PLACEHOLDER
        )
        binding.tvDescription.text = resources.getString(
            summary.weatherIcon?.descriptionResourceId ?: R.string.placeholder_weather_description
        )
        binding.windAndPrecipitation.tvPrecipitationAmount.text =
            if (summary.precipitationAmount.isPrecipitationAmountSignificant()) {
                resources.getString(R.string.template_precipitation, summary.precipitationAmount)
            } else {
                resources.getString(R.string.placeholder_precipitation)
            }
        binding.windAndPrecipitation.ivWindFromDirection.visibility = View.INVISIBLE
        binding.windAndPrecipitation.tvWindSpeed.text =
            if (summary.maxWindSpeed.isWindSpeedSignificant()) {
                resources.getString(R.string.template_wind_speed, summary.maxWindSpeed)
            } else {
                resources.getString(R.string.placeholder_wind_speed)
            }
    }
}