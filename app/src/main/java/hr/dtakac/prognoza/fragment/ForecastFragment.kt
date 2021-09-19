package hr.dtakac.prognoza.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.BaseProgressIndicator
import hr.dtakac.prognoza.BUNDLE_KEY_PLACE_PICKED
import hr.dtakac.prognoza.BUNDLE_KEY_UNITS_CHANGED
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.databinding.LayoutForecastEmptyBinding
import hr.dtakac.prognoza.databinding.LayoutForecastOutdatedBinding
import hr.dtakac.prognoza.utils.formatEmptyMessage
import hr.dtakac.prognoza.uimodel.forecast.EmptyForecastUiModel
import hr.dtakac.prognoza.uimodel.forecast.ForecastUiModel
import hr.dtakac.prognoza.viewmodel.ForecastFragmentViewModel

abstract class ForecastFragment<UI_MODEL : ForecastUiModel, VB : ViewBinding>(
    bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : ViewBindingFragment<VB>(bindingInflater) {
    abstract val viewModel: ForecastFragmentViewModel<UI_MODEL>
    abstract val requestKey: String
    abstract val emptyForecastBinding: LayoutForecastEmptyBinding
    abstract val progressBar: BaseProgressIndicator<*>
    abstract val outdatedForecastBinding: LayoutForecastOutdatedBinding?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
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
        viewModel.outdatedForecastMessage.observe(viewLifecycleOwner) {
            if (it == null) {
                outdatedForecastBinding?.root?.visibility = View.GONE
            } else {
                showCachedResultsMessage(it.reason)
            }
        }
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
            requireContext().formatEmptyMessage(uiModel.reasonResourceId)
    }

    private fun showCachedResultsMessage(reason: Int?) {
        outdatedForecastBinding?.root?.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                showOutdatedForecastDialog(reason ?: R.string.error_generic)
            }
        }
    }

    private fun showOutdatedForecastDialog(messageId: Int) {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.title_outdated_forecast)
            .setMessage(
                getString(
                    R.string.template_content_outdated_forecast,
                    getString(messageId)
                )
            )
            .setPositiveButton(R.string.action_ok, null)
            .show()
    }
}