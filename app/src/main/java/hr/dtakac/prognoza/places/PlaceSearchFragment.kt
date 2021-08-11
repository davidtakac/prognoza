package hr.dtakac.prognoza.places

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hr.dtakac.prognoza.base.ViewBindingFragment
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.databinding.FragmentPlacesBinding
import hr.dtakac.prognoza.forecast.ForecastActivity
import hr.dtakac.prognoza.forecast.fragment.DaysFragment
import hr.dtakac.prognoza.forecast.fragment.TodayFragment
import hr.dtakac.prognoza.forecast.fragment.TomorrowFragment
import hr.dtakac.prognoza.forecast.uimodel.TodayForecast
import org.koin.androidx.viewmodel.ext.android.viewModel

// todo: ideja, wrappaj fragmentcontainerview u constraintlayout pa probaj tako elevaciju postic?
class PlaceSearchFragment :
    ViewBindingFragment<FragmentPlacesBinding>(FragmentPlacesBinding::inflate) {
    private val adapter = PlacesRecyclerViewAdapter { viewModel.select(it) }
    private val viewModel by viewModel<PlacesViewModel>()

    companion object {
        const val RESULT_PLACE_PICKED = "search_place_picked"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
        initializeSearchField()
        viewModel.showSavedPlaces()
    }

    private fun observeViewModel() {
        viewModel.places.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.apply {
                if (isLoading) show() else hide()
            }
        }
        viewModel.placeSelectedEvent.observe(viewLifecycleOwner) {
            if (!it.isConsumed) {
                val result = Bundle().apply { putBoolean(RESULT_PLACE_PICKED, true) }
                parentFragmentManager.apply {
                    setFragmentResult(ForecastActivity.REQUEST_KEY, result)
                    setFragmentResult(TodayFragment.REQUEST_KEY, result)
                    setFragmentResult(TomorrowFragment.REQUEST_KEY, result)
                    setFragmentResult(DaysFragment.REQUEST_KEY, result)
                }
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
        binding.rvResults.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvResults.adapter = adapter
        binding.rvResults.addItemDecoration(MarginItemDecoration())
        binding.rvResults.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun initializeSearchField() {
        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(textView.text.toString())
                true
            } else {
                false
            }
        }
        binding.etSearch.addTextChangedListener {
            if (it.isNullOrBlank()) {
                viewModel.showSavedPlaces()
            }
        }
    }
}