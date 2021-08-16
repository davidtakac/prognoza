package hr.dtakac.prognoza.forecast.fragment

import hr.dtakac.prognoza.common.DAYS_REQUEST_KEY
import hr.dtakac.prognoza.databinding.FragmentDaysBinding
import hr.dtakac.prognoza.forecast.adapter.DaysRecyclerViewAdapter
import hr.dtakac.prognoza.forecast.uimodel.DaysForecastUiModel
import hr.dtakac.prognoza.forecast.viewmodel.DaysFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DaysFragment :
    BaseForecastFragment<DaysForecastUiModel, FragmentDaysBinding>(FragmentDaysBinding::inflate) {
    override val emptyForecastBinding get() = binding.emptyScreen
    override val progressBar get() = binding.progressBar
    override val recyclerView get() = binding.rvDays
    override val requestKey get() = DAYS_REQUEST_KEY
    override val viewModel by viewModel<DaysFragmentViewModel>()

    private val adapter = DaysRecyclerViewAdapter()

    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        recyclerView.adapter = adapter
    }

    override fun showForecast(uiModel: DaysForecastUiModel) {
        adapter.submitList(uiModel.days)
    }
}