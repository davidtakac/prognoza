package hr.dtakac.prognoza.forecast.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.databinding.FragmentTodayBinding
import hr.dtakac.prognoza.forecast.adapter.ForecastHoursRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.TodayUiModel
import hr.dtakac.prognoza.forecast.viewmodel.ForecastViewModel
import org.koin.android.ext.android.inject
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class TodayFragment : ViewBindingFragment<FragmentTodayBinding>(FragmentTodayBinding::inflate) {
    private val adapter = ForecastHoursRecyclerViewAdapter()
    private val viewModel by inject<ForecastViewModel>()
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd LLLL, HH:mm", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
        viewModel.getTodayForecast()
    }

    private fun observeViewModel() {
        viewModel.forecastData.observe(viewLifecycleOwner) {
            populateForecastViews(it)
        }
        viewModel.isForecastLoading.observe(viewLifecycleOwner) { isLoading ->
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

    private fun populateForecastViews(uiModel: TodayUiModel) {
        binding.tvDateTime.text = uiModel.dateTime.atZone(ZoneId.systemDefault()).format(dateTimeFormatter)
        binding.tvTemperature.text = resources.getString(
            R.string.template_degrees,
            uiModel.currentTemperature
        )
        binding.ivWeatherIcon.setImageResource(uiModel.weatherIcon.iconResourceId)
        binding.tvDescription.text = resources.getString(uiModel.weatherIcon.descriptionResourceId)
        adapter.data = uiModel.nextHours
    }
}