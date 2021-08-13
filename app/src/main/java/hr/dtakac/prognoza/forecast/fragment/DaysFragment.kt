package hr.dtakac.prognoza.forecast.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.common.BUNDLE_KEY_PLACE_PICKED
import hr.dtakac.prognoza.common.DAYS_REQUEST_KEY
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.common.util.formatEmptyMessage
import hr.dtakac.prognoza.databinding.FragmentDaysBinding
import hr.dtakac.prognoza.forecast.adapter.DaysRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.DaysForecast
import hr.dtakac.prognoza.forecast.uimodel.EmptyForecast
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
        if (binding.error.root.visibility != View.VISIBLE) {
            viewModel.getForecast()
        }
    }

    private fun observeViewModel() {
        viewModel.daysForecast.observe(viewLifecycleOwner) {
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
        binding.error.btnTryAgain.setOnClickListener {
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

    private fun showForecast(uiModel: DaysForecast) {
        adapter.submitList(uiModel.days)
    }

    private fun showEmptyScreen(uiModel: EmptyForecast) {
        binding.error.tvErrorMessage.text = resources.formatEmptyMessage(uiModel.reasonResourceId)
        binding.error.root.visibility = View.VISIBLE
    }
}