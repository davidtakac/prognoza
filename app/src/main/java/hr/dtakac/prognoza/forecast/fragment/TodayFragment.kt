package hr.dtakac.prognoza.forecast.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.common.util.*
import hr.dtakac.prognoza.databinding.FragmentTodayBinding
import hr.dtakac.prognoza.forecast.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.EmptyForecast
import hr.dtakac.prognoza.forecast.uimodel.TodayForecast
import hr.dtakac.prognoza.forecast.viewmodel.TodayViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.format.DateTimeFormatter
import java.util.*

class TodayFragment : ViewBindingFragment<FragmentTodayBinding>(FragmentTodayBinding::inflate) {
    private val adapter = HoursRecyclerViewAdapter()
    private val viewModel by viewModel<TodayViewModel>()
    private val dateTimeFormatter =
        DateTimeFormatter.ofPattern("d LLLL, HH:mm", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
        initializeTryAgain()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTodayForecast()
    }

    private fun observeViewModel() {
        viewModel.todayForecast.observe(viewLifecycleOwner) {
            showForecast(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.apply {
                if (isLoading) show() else hide()
            }
            binding.error.progressBar.apply {
                if (isLoading) show() else hide()
            }
        }
        viewModel.emptyScreen.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.error.root.visibility = View.GONE
            } else {
                showEmptyScreen(it)
            }
        }
        viewModel.message.observe(viewLifecycleOwner) {
            if (!it.isConsumed) {
                Snackbar.make(
                    binding.root,
                    resources.getString(it.getValue()),
                    Snackbar.LENGTH_SHORT
                ).show()
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

    private fun showForecast(uiModel: TodayForecast) {
        val currentHour = uiModel.currentHour
        binding.tvDateTime.text = currentHour.time.format(dateTimeFormatter)
        binding.tvTemperature.text = resources.formatTemperatureValue(currentHour.temperature)
        binding.ivWeatherIcon.setImageResource(
            currentHour.weatherIcon?.iconResourceId ?: R.drawable.ic_cloud
        )
        binding.tvPrecipitationForecast.text =
            resources.formatPrecipitationTwoHours(uiModel.precipitationForecast)
        binding.tvFeelsLike.text = resources.formatFeelsLikeDescription(currentHour.feelsLike)
        adapter.submitListActual(uiModel.otherHours)
    }

    private fun showEmptyScreen(uiModel: EmptyForecast) {
        binding.error.root.visibility = View.VISIBLE
        binding.error.tvErrorMessage.text = resources.formatEmptyMessage(uiModel.reasonResourceId)
    }
}