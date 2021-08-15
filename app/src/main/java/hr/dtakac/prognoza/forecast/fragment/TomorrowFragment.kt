package hr.dtakac.prognoza.forecast.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.common.BUNDLE_KEY_PLACE_PICKED
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.common.TOMORROW_REQUEST_KEY
import hr.dtakac.prognoza.common.util.formatEmptyMessage
import hr.dtakac.prognoza.common.util.formatRepresentativeWeatherIconDescription
import hr.dtakac.prognoza.common.util.formatTemperatureValue
import hr.dtakac.prognoza.common.util.formatTotalPrecipitation
import hr.dtakac.prognoza.databinding.FragmentTomorrowBinding
import hr.dtakac.prognoza.forecast.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.DayUiModel
import hr.dtakac.prognoza.forecast.uimodel.EmptyForecast
import hr.dtakac.prognoza.forecast.uimodel.TomorrowForecast
import hr.dtakac.prognoza.forecast.viewmodel.TomorrowFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class TomorrowFragment :
    ViewBindingFragment<FragmentTomorrowBinding>(FragmentTomorrowBinding::inflate) {
    private val adapter = HoursRecyclerViewAdapter()
    private val viewModel by viewModel<TomorrowFragmentViewModel>()
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, d LLLL", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
        initializeTryAgain()
        initializeDataRefreshOnChangedPlace()
    }

    override fun onResume() {
        super.onResume()
        if (binding.emptyScreen.root.visibility != View.VISIBLE) {
            viewModel.getForecast()
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
            binding.emptyScreen.progressBar.apply {
                if (isLoading) show() else hide()
            }
        }
        viewModel.emptyScreen.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.emptyScreen.root.visibility = View.GONE
            } else {
                showEmptyScreen(it)
            }
        }
        viewModel.cachedResultsMessage.observe(viewLifecycleOwner) {
            if (!it.isConsumed) {
                showCachedResultsMessage(it.getValue())
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
        binding.emptyScreen.btnTryAgain.setOnClickListener {
            viewModel.getForecast()
        }
    }

    private fun initializeDataRefreshOnChangedPlace() {
        parentFragmentManager.setFragmentResultListener(
            TOMORROW_REQUEST_KEY,
            this,
            { _, bundle ->
                if (bundle.getBoolean(BUNDLE_KEY_PLACE_PICKED)) {
                    viewModel.getForecast()
                }
            }
        )
    }

    private fun showForecast(uiModel: TomorrowForecast) {
        populateSummaryViews(uiModel.summary)
        adapter.submitList(uiModel.hours)
    }

    private fun showEmptyScreen(uiModel: EmptyForecast) {
        binding.emptyScreen.tvErrorMessage.text = resources.formatEmptyMessage(uiModel.reasonResourceId)
        binding.emptyScreen.root.visibility = View.VISIBLE
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
        binding.tvPrecipitation.text =
            resources.formatTotalPrecipitation(uiModel.totalPrecipitationAmount)
    }

    private fun showCachedResultsMessage(reason: Int?) {
        Snackbar.make(
            binding.root,
            resources.getString(R.string.notify_cached_result),
            Snackbar.LENGTH_SHORT
        )
            .setAction(R.string.action_why) {
                showAlertDialog(reason ?: R.string.error_generic)
            }
            .show()
    }

    private fun showAlertDialog(messageId: Int) {
        AlertDialog.Builder(requireActivity())
            .setMessage(messageId)
            .setPositiveButton(R.string.action_ok, null)
            .show()
    }
}