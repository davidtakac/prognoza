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
import hr.dtakac.prognoza.databinding.FragmentTomorrowBinding
import hr.dtakac.prognoza.forecast.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.DayUiModel
import hr.dtakac.prognoza.forecast.uimodel.EmptyForecast
import hr.dtakac.prognoza.forecast.uimodel.TomorrowForecast
import hr.dtakac.prognoza.forecast.viewmodel.TomorrowViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZoneId
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
            viewModel.getTomorrowForecast()
        }
    }

    private fun showForecast(uiModel: TomorrowForecast) {
        populateSummaryViews(uiModel.summary)
        adapter.submitList(uiModel.hours)
    }

    private fun showEmptyScreen(uiModel: EmptyForecast) {
        binding.error.tvErrorMessage.text = resources.formatEmptyMessage(uiModel.reasonResourceId)
        binding.error.root.visibility = View.VISIBLE
    }

    private fun populateSummaryViews(uiModel: DayUiModel) {
        val resources = binding.root.context.resources
        binding.tvDateTime.text = uiModel.time
            .withZoneSameInstant(ZoneId.systemDefault())
            .format(dateTimeFormatter)
        binding.tvTemperatureHigh.text =
            resources.formatTemperatureValue(uiModel.highTemperature)
        binding.tvTemperatureLow.text =
            resources.formatTemperatureValue(uiModel.lowTemperature)
        binding.tvDescription.text =
            resources.formatRepresentativeWeatherIconDescription(uiModel.representativeWeatherIcon)
        binding.ivWeatherIcon.setImageResource(
            uiModel.representativeWeatherIcon?.weatherIcon?.iconResourceId ?: R.drawable.ic_cloud
        )
        binding.tvPrecipitation.text = resources.formatTotalPrecipitation(uiModel.totalPrecipitationAmount)
    }
}