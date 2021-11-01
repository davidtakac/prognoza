package hr.dtakac.prognoza.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dtakac.prognoza.DAYS_REQUEST_KEY
import hr.dtakac.prognoza.adapter.DaysRecyclerViewAdapter
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.databinding.FragmentDaysBinding
import hr.dtakac.prognoza.databinding.LayoutForecastOutdatedBinding
import hr.dtakac.prognoza.uimodel.forecast.DaysForecastUiModel
import hr.dtakac.prognoza.viewmodel.DaysViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DaysForecastFragment :
    ForecastFragment<DaysForecastUiModel, FragmentDaysBinding>(FragmentDaysBinding::inflate) {
    override val emptyForecastBinding get() = binding.emptyScreen
    override val progressBar get() = binding.progressBar
    override val outdatedForecastBinding: LayoutForecastOutdatedBinding get() = binding.cachedDataMessage
    override val requestKey get() = DAYS_REQUEST_KEY
    override val viewModel by viewModel<DaysViewModel>()

    private val adapter = DaysRecyclerViewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
    }

    override fun showForecast(uiModel: DaysForecastUiModel) {
        adapter.submitList(uiModel.days)
    }

    private fun initializeRecyclerView() {
        with(binding.rvDays) {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = this@DaysForecastFragment.adapter
            addItemDecoration(MarginItemDecoration())
        }
    }
}