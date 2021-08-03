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
                is TodayUiModel.Success -> showForecast(it)
                is TodayUiModel.Error -> showError(it)
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
            viewModel.getTodayForecast()
        }
    }

    private fun showForecast(uiModel: TodayUiModel.Success) {
        val currentHour = uiModel.currentHour
        binding.tvDateTime.text = currentHour.time.format(dateTimeFormatter)
        binding.tvTemperature.text = resources.getString(
            R.string.template_temperature,
            currentHour.temperature
        )
        binding.ivWeatherIcon.setImageResource(
            currentHour.weatherIcon?.iconResourceId ?: IMAGE_PLACEHOLDER
        )
        binding.tvDescription.text = resources.getString(
            currentHour.weatherIcon?.descriptionResourceId
                ?: R.string.placeholder_weather_description
        )
        binding.windAndPrecipitation.tvPrecipitationAmount.text =
            if (currentHour.precipitationAmount.isPrecipitationAmountSignificant()) {
                resources.getString(R.string.template_precipitation, currentHour.precipitationAmount)
            } else {
                resources.getString(R.string.placeholder_precipitation)
            }
        if (currentHour.windSpeed.isWindSpeedSignificant()) {
            binding.windAndPrecipitation.tvWindSpeed.text =
                resources.getString(R.string.template_wind_speed, currentHour.windSpeed)
            binding.windAndPrecipitation.ivWindFromDirection.visibility = View.VISIBLE
            binding.windAndPrecipitation.ivWindFromDirection.rotation = currentHour.windFromDirection ?: 0f
        } else {
            binding.windAndPrecipitation.tvWindSpeed.text = resources.getString(R.string.placeholder_wind_speed)
            binding.windAndPrecipitation.ivWindFromDirection.visibility = View.GONE
        }
        adapter.submitList(uiModel.otherHours)
        binding.error.root.visibility = View.GONE
    }

    private fun showError(uiModel: TodayUiModel.Error) {
        binding.error.root.visibility = View.VISIBLE
        binding.error.tvErrorMessage.text = resources.getString(uiModel.errorMessageResourceId)
    }
}