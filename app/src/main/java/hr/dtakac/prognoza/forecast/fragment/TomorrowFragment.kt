package hr.dtakac.prognoza.forecast.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.databinding.FragmentTomorrowBinding
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.common.util.bind
import hr.dtakac.prognoza.forecast.adapter.HoursRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.DayUiModel
import hr.dtakac.prognoza.forecast.uimodel.TomorrowForecastUiModel
import hr.dtakac.prognoza.forecast.viewmodel.TomorrowViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TomorrowFragment :
    ViewBindingFragment<FragmentTomorrowBinding>(FragmentTomorrowBinding::inflate) {
    private val adapter = HoursRecyclerViewAdapter()
    private val viewModel by viewModel<TomorrowViewModel>()

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
            when (it) {
                is TomorrowForecastUiModel.Success -> showForecast(it)
                is TomorrowForecastUiModel.Error -> showError(it)
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
            viewModel.getTomorrowForecast()
        }
    }

    private fun showForecast(uiModel: TomorrowForecastUiModel.Success) {
        populateSummaryViews(uiModel.summary)
        adapter.submitListActual(uiModel.hours)
        binding.error.root.visibility = View.GONE
    }

    private fun showError(uiModel: TomorrowForecastUiModel.Error) {
        binding.error.tvErrorMessage.text = resources.getString(uiModel.errorMessageResourceId)
        binding.error.root.visibility = View.VISIBLE
    }

    private fun populateSummaryViews(uiModel: DayUiModel) {
        binding.summary.bind(uiModel)
    }
}