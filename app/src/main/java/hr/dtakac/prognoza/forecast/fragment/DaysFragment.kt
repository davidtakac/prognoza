package hr.dtakac.prognoza.forecast.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.databinding.FragmentDaysBinding
import hr.dtakac.prognoza.forecast.adapter.DaysRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.adapter.ForecastItemDecoration
import hr.dtakac.prognoza.forecast.viewmodel.DaysViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DaysFragment : ViewBindingFragment<FragmentDaysBinding>(FragmentDaysBinding::inflate) {
    private val adapter = DaysRecyclerViewAdapter()
    private val viewModel by viewModel<DaysViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDaysForecast()
    }

    private fun observeViewModel() {
        viewModel.daysForecast.observe(viewLifecycleOwner) {
            adapter.submitList(it.days)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.apply {
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
        binding.rvDays.addItemDecoration(ForecastItemDecoration())
        binding.rvDays.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }
}