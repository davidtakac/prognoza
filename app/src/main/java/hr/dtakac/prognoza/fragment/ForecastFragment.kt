package hr.dtakac.prognoza.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.snackbar.Snackbar
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.common.BUNDLE_KEY_PLACE_PICKED
import hr.dtakac.prognoza.common.BUNDLE_KEY_UNITS_CHANGED
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.extensions.formatEmptyMessage
import hr.dtakac.prognoza.databinding.LayoutEmptyForecastBinding
import hr.dtakac.prognoza.uimodel.forecast.EmptyForecastUiModel
import hr.dtakac.prognoza.uimodel.forecast.ForecastUiModel
import hr.dtakac.prognoza.viewmodel.ForecastFragmentViewModel

abstract class ForecastFragment<UI_MODEL : ForecastUiModel, VB : ViewBinding>(
    bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : ViewBindingFragment<VB>(bindingInflater) {
    abstract val recyclerView: RecyclerView
    abstract val viewModel: ForecastFragmentViewModel<UI_MODEL>
    abstract val requestKey: String
    abstract val emptyForecastBinding: LayoutEmptyForecastBinding
    abstract val progressBar: BaseProgressIndicator<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
        initializeTryAgain()
        initializeFragmentResultListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getForecast()
    }

    abstract fun showForecast(uiModel: UI_MODEL)

    private fun observeViewModel() {
        viewModel.forecast.observe(viewLifecycleOwner) {
            showForecast(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.apply {
                if (isLoading) show() else hide()
            }
            emptyForecastBinding.progressBar.apply {
                if (isLoading) show() else hide()
            }
        }
        viewModel.emptyScreen.observe(viewLifecycleOwner) {
            if (it == null) {
                emptyForecastBinding.root.visibility = View.GONE
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

    protected open fun initializeRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.addItemDecoration(MarginItemDecoration())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun initializeTryAgain() {
        emptyForecastBinding.btnTryAgain.setOnClickListener {
            viewModel.getForecast()
        }
    }

    private fun initializeFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            requestKey,
            this,
            { _, bundle ->
                when {
                    bundle.getBoolean(BUNDLE_KEY_PLACE_PICKED) -> {
                        viewModel.getForecast()
                    }
                    bundle.getBoolean(BUNDLE_KEY_UNITS_CHANGED) -> {
                        viewModel.getForecast()
                    }
                }
            }
        )
    }

    private fun showEmptyScreen(uiModel: EmptyForecastUiModel) {
        emptyForecastBinding.root.visibility = View.VISIBLE
        emptyForecastBinding.tvErrorMessage.text =
            resources.formatEmptyMessage(uiModel.reasonResourceId)
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