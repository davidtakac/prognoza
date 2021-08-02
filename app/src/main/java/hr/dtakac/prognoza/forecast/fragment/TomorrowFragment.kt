package hr.dtakac.prognoza.forecast.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dtakac.prognoza.IMAGE_PLACEHOLDER
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.databinding.FragmentTomorrowBinding
import hr.dtakac.prognoza.forecast.adapter.ForecastItemDecoration
import hr.dtakac.prognoza.forecast.adapter.HoursRecyclerViewAdapter
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
        viewModel.getTomorrowForecast()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTomorrowForecast()
    }

    private fun observeViewModel() {
        viewModel.tomorrowForecast.observe(viewLifecycleOwner) {
            populateForecastViews(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.apply {
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

    private fun populateForecastViews(uiModel: TomorrowUiModel) {
        binding.tvDateTime.text = uiModel.dateTime.format(dateTimeFormatter)
        binding.tvTemperatureHigh.text = resources.getString(
            R.string.template_temperature,
            uiModel.highTemperature
        )
        binding.tvTemperatureLow.text = resources.getString(
            R.string.template_temperature,
            uiModel.lowTemperature
        )
        binding.ivWeatherIcon.setImageResource(
            uiModel.weatherIcon?.iconResourceId ?: IMAGE_PLACEHOLDER
        )
        binding.tvDescription.text = resources.getString(
            uiModel.weatherIcon?.descriptionResourceId ?: R.string.placeholder_weather_description
        )
        adapter.submitList(uiModel.hours)
    }
}