package hr.dtakac.prognoza.forecast.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dtakac.prognoza.IMAGE_PLACEHOLDER
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.databinding.FragmentTomorrowBinding
import hr.dtakac.prognoza.forecast.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.TomorrowUiModel
import hr.dtakac.prognoza.forecast.viewmodel.ForecastViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.format.DateTimeFormatter
import java.util.*

class TomorrowFragment : ViewBindingFragment<FragmentTomorrowBinding>(FragmentTomorrowBinding::inflate) {
    private val adapter = HoursRecyclerViewAdapter()
    private val viewModel by sharedViewModel<ForecastViewModel>()
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd LLLL", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
        viewModel.getTomorrowForecast()
    }

    private fun observeViewModel() {
        viewModel.tomorrowForecast.observe(viewLifecycleOwner) {
            populateForecastViews(it)
        }
        viewModel.isTomorrowForecastLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.apply {
                if (isLoading) show() else hide()
            }
        }
    }

    private fun initializeRecyclerView() {
        binding.rvHours.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvHours.adapter = adapter
    }

    private fun populateForecastViews(uiModel: TomorrowUiModel) {
        binding.tvDateTime.text = uiModel.dateTime.format(dateTimeFormatter)
        binding.tvTemperature.text = resources.getString(
            R.string.template_degrees_high_low,
            uiModel.highTemperature, uiModel.lowTemperature
        )
        binding.ivWeatherIcon.setImageResource(uiModel.weatherIcon?.iconResourceId ?: IMAGE_PLACEHOLDER)
        binding.tvDescription.text = resources.getString(
            uiModel.weatherIcon?.descriptionResourceId ?: R.string.weather_icon_description_placeholder
        )
        adapter.data = uiModel.hours
    }
}