package hr.dtakac.prognoza.forecast.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.databinding.FragmentTodayBinding
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.common.bind
import hr.dtakac.prognoza.forecast.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.TodayForecastUiModel
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
        initializeTryAgain()
    }

    override fun onResume() {
        super.onResume()
        if (binding.error.root.visibility != View.VISIBLE) {
            viewModel.getTodayForecast()
        }
    }

    private fun observeViewModel() {
        viewModel.todayForecast.observe(viewLifecycleOwner) {
            when (it) {
                is TodayForecastUiModel.Success -> showForecast(it)
                is TodayForecastUiModel.Error -> showError(it)
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
        binding.rvHours.addItemDecoration(MarginItemDecoration())
        binding.rvHours.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun initializeTryAgain() {
        binding.error.btnTryAgain.setOnClickListener {
            viewModel.getTodayForecast()
        }
    }

    private fun showForecast(uiModel: TodayForecastUiModel.Success) {
        val currentHour = uiModel.currentHour
        binding.tvDateTime.text = currentHour.time.format(dateTimeFormatter)
        binding.tvTemperature.text = resources.getString(
            R.string.template_temperature,
            currentHour.temperature
        )
        binding.ivWeatherIcon.setImageResource(
            currentHour.weatherIcon?.iconResourceId ?: R.drawable.ic_cloud
        )
        binding.tvDescription.text = resources.getString(
            currentHour.weatherIcon?.descriptionResourceId
                ?: R.string.placeholder_description
        )
        binding.windAndPrecipitation.bind(
            uiModel.currentHour.windSpeed,
            uiModel.currentHour.windIconRotation,
            uiModel.currentHour.precipitationAmount
        )
        adapter.submitList(uiModel.otherHours)
        binding.error.root.visibility = View.GONE
    }

    private fun showError(uiModel: TodayForecastUiModel.Error) {
        binding.error.root.visibility = View.VISIBLE
        binding.error.tvErrorMessage.text = resources.getString(uiModel.errorMessageResourceId)
    }
}