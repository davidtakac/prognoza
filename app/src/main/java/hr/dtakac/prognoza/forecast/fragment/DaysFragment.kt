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
import hr.dtakac.prognoza.common.DAYS_REQUEST_KEY
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.common.util.formatEmptyMessage
import hr.dtakac.prognoza.databinding.FragmentDaysBinding
import hr.dtakac.prognoza.forecast.adapter.DaysRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.DaysForecastUiModel
import hr.dtakac.prognoza.forecast.uimodel.EmptyForecastUiModel
import hr.dtakac.prognoza.forecast.viewmodel.DaysFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DaysFragment : ViewBindingFragment<FragmentDaysBinding>(FragmentDaysBinding::inflate) {
    private val adapter = DaysRecyclerViewAdapter()
    private val viewModel by viewModel<DaysFragmentViewModel>()

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
        viewModel.forecast.observe(viewLifecycleOwner) {
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
        binding.rvDays.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvDays.adapter = adapter
        binding.rvDays.addItemDecoration(MarginItemDecoration())
        binding.rvDays.addItemDecoration(
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
            DAYS_REQUEST_KEY,
            this,
            { _, bundle ->
                if (bundle.getBoolean(BUNDLE_KEY_PLACE_PICKED)) {
                    viewModel.getForecast()
                }
            }
        )
    }

    private fun showForecast(uiModel: DaysForecastUiModel) {
        adapter.submitList(uiModel.days)
    }

    private fun showEmptyScreen(uiModel: EmptyForecastUiModel) {
        binding.emptyScreen.tvErrorMessage.text = resources.formatEmptyMessage(uiModel.reasonResourceId)
        binding.emptyScreen.root.visibility = View.VISIBLE
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