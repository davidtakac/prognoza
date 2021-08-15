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
import hr.dtakac.prognoza.common.TODAY_REQUEST_KEY
import hr.dtakac.prognoza.common.util.formatEmptyMessage
import hr.dtakac.prognoza.common.util.formatPrecipitationTwoHours
import hr.dtakac.prognoza.common.util.formatTemperatureValue
import hr.dtakac.prognoza.databinding.FragmentTodayBinding
import hr.dtakac.prognoza.forecast.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.EmptyForecast
import hr.dtakac.prognoza.forecast.uimodel.TodayForecast
import hr.dtakac.prognoza.forecast.viewmodel.TodayFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.format.DateTimeFormatter
import java.util.*

class TodayFragment : ViewBindingFragment<FragmentTodayBinding>(FragmentTodayBinding::inflate) {
    private val adapter = HoursRecyclerViewAdapter()
    private val viewModel by viewModel<TodayFragmentViewModel>()
    private val dateTimeFormatter =
        DateTimeFormatter.ofPattern("d LLLL, HH:mm", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
        initializeTryAgain()
        initializeDataRefreshOnChangedPlace()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getForecast()
    }

    private fun observeViewModel() {
        viewModel.todayForecast.observe(viewLifecycleOwner) {
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
            TODAY_REQUEST_KEY,
            this,
            { _, bundle ->
                if (bundle.getBoolean(BUNDLE_KEY_PLACE_PICKED)) {
                    viewModel.getForecast()
                }
            }
        )
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
        binding.tvFeelsLike.text = resources.getString(
            R.string.template_feels_like,
            if (currentHour.feelsLike == null) {
                resources.getString(R.string.placeholder_temperature)
            } else {
                resources.formatTemperatureValue(currentHour.feelsLike)
            }
        )
        adapter.submitList(uiModel.otherHours)
    }

    private fun showEmptyScreen(uiModel: EmptyForecast) {
        binding.emptyScreen.root.visibility = View.VISIBLE
        binding.emptyScreen.tvErrorMessage.text = resources.formatEmptyMessage(uiModel.reasonResourceId)
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