package hr.dtakac.prognoza.fragment

import hr.dtakac.prognoza.DAYS_REQUEST_KEY
import hr.dtakac.prognoza.databinding.FragmentDaysBinding
import hr.dtakac.prognoza.adapter.DaysRecyclerViewAdapter
import hr.dtakac.prognoza.uimodel.forecast.DaysForecastUiModel
import hr.dtakac.prognoza.viewmodel.DaysFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DaysForecastFragment :
    ForecastFragment<DaysForecastUiModel, FragmentDaysBinding>(FragmentDaysBinding::inflate) {
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