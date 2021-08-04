package hr.dtakac.prognoza.forecast.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.databinding.FragmentDaysBinding
import hr.dtakac.prognoza.forecast.adapter.DaysRecyclerViewAdapter
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.forecast.uimodel.DaysForecastUiModel
import hr.dtakac.prognoza.forecast.viewmodel.DaysViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DaysFragment : ViewBindingFragment<FragmentDaysBinding>(FragmentDaysBinding::inflate) {
    private val adapter = DaysRecyclerViewAdapter()
    private val viewModel by viewModel<DaysViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
        initializeTryAgain()
    }

    override fun onResume() {
        super.onResume()
        if (binding.error.root.visibility != View.VISIBLE) {
            viewModel.getDaysForecast()
        }
    }

    private fun observeViewModel() {
        viewModel.daysForecast.observe(viewLifecycleOwner) {
            when (it) {
                is DaysForecastUiModel.Success -> showForecast(it)
                is DaysForecastUiModel.Error -> showError(it)
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
            viewModel.getDaysForecast()
        }
    }

    private fun showForecast(uiModel: DaysForecastUiModel.Success) {
        adapter.submitList(uiModel.days)
        binding.error.root.visibility = View.GONE
    }

    private fun showError(uiModel: DaysForecastUiModel.Error) {
        binding.error.tvErrorMessage.text = resources.getString(uiModel.errorMessageResourceId)
        binding.error.root.visibility = View.VISIBLE
    }
}