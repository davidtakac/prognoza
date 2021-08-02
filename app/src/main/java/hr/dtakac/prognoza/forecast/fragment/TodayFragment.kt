package hr.dtakac.prognoza.forecast.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dtakac.prognoza.IMAGE_PLACEHOLDER
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.databinding.FragmentTodayBinding
import hr.dtakac.prognoza.forecast.adapter.ForecastItemDecoration
import hr.dtakac.prognoza.forecast.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.TodayUiModel
import hr.dtakac.prognoza.forecast.viewmodel.TodayViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.format.DateTimeFormatter
import java.util.*

class TodayFragment : ViewBindingFragment<FragmentTodayBinding>(FragmentTodayBinding::inflate) {
    private val adapter = HoursRecyclerViewAdapter()
    private val viewModel by viewModel<TodayViewModel>()
    private val dateTimeFormatter =
        DateTimeFormatter.ofPattern("EE d LLLL, HH:mm", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
        initializeReload()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTodayForecast()
    }

    private fun observeViewModel() {
        viewModel.todayForecast.observe(viewLifecycleOwner) {
            when (it) {
                is TodayUiModel.Success -> showForecast(it)
                is TodayUiModel.Error -> showError(it)
            }
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

    private fun initializeReload() {
        binding.ivReload.setOnClickListener {
            viewModel.getTodayForecast()
        }
    }

    private fun showForecast(uiModel: TodayUiModel.Success) {
        binding.tvDateTime.text = uiModel.dateTime.format(dateTimeFormatter)
        binding.tvTemperature.text = resources.getString(
            R.string.template_temperature,
            uiModel.currentTemperature
        )
        binding.ivWeatherIcon.setImageResource(
            uiModel.weatherIcon?.iconResourceId ?: IMAGE_PLACEHOLDER
        )
        binding.tvDescription.text = resources.getString(
            uiModel.weatherIcon?.descriptionResourceId ?: R.string.placeholder_weather_description
        )
        adapter.submitList(uiModel.nextHours)
        binding.error.root.visibility = View.GONE
    }

    private fun showError(error: TodayUiModel.Error) {
        binding.error.root.visibility = View.VISIBLE
        binding.error.tvErrorMessage.text = resources.getString(error.errorMessageResourceId)
    }
}