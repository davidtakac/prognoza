package hr.dtakac.prognoza.activity

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import hr.dtakac.prognoza.*
import hr.dtakac.prognoza.databinding.ActivityForecastBinding
import hr.dtakac.prognoza.adapter.ForecastPagerAdapter
import hr.dtakac.prognoza.fragment.PlaceSearchDialogFragment
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.viewmodel.ForecastActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val SEARCH_FRAGMENT_TAG = "search"

class ForecastActivity :
    ViewBindingActivity<ActivityForecastBinding>(ActivityForecastBinding::inflate) {
    private val viewModel by viewModel<ForecastActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
        initializeViewPager()
        initializeToolbar()
        viewModel.getPlaceName()
        viewModel.getSelectedUnits()
    }

    override fun onResume() {
        super.onResume()
        viewModel.cleanUpDatabase()
    }

    private fun observeViewModel() {
        viewModel.placeName.observe(this) {
            binding.toolbar.title = it
        }
        viewModel.selectedUnits.observe(this) {
            if (!it.isConsumed) {
                notifyFragmentsUnitsHaveChanged()
                updateChangeUnitsMenuItem(it.getValue())
            }
        }
    }

    private fun initializeViewPager() {
        binding.viewPager.adapter = ForecastPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.forecast_tab_names)[position]
        }.attach()
    }

    private fun initializeToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_forecast)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> {
                    openSearch()
                    true
                }
                R.id.units -> {
                    viewModel.changeUnits()
                    true
                }
                else -> false
            }
        }
        supportFragmentManager.setFragmentResultListener(
            PLACE_SEARCH_REQUEST_KEY,
            this,
            { _, bundle ->
                if (bundle.getBoolean(BUNDLE_KEY_PLACE_PICKED)) {
                    viewModel.getPlaceName()
                    notifyFragmentsOfNewPlace()
                    closeSearch()
                }
            }
        )
    }

    private fun openSearch() {
        PlaceSearchDialogFragment().show(supportFragmentManager, SEARCH_FRAGMENT_TAG)
    }

    private fun closeSearch() {
        (supportFragmentManager.findFragmentByTag(SEARCH_FRAGMENT_TAG) as? DialogFragment)?.dismiss()
    }

    private fun notifyFragmentsOfNewPlace() {
        val result = Bundle().apply { putBoolean(BUNDLE_KEY_PLACE_PICKED, true) }
        supportFragmentManager.apply {
            setFragmentResult(TODAY_REQUEST_KEY, result)
            setFragmentResult(TOMORROW_REQUEST_KEY, result)
            setFragmentResult(DAYS_REQUEST_KEY, result)
        }
    }

    private fun notifyFragmentsUnitsHaveChanged() {
        val result = Bundle().apply { putBoolean(BUNDLE_KEY_UNITS_CHANGED, true) }
        supportFragmentManager.apply {
            setFragmentResult(TODAY_REQUEST_KEY, result)
            setFragmentResult(TOMORROW_REQUEST_KEY, result)
            setFragmentResult(DAYS_REQUEST_KEY, result)
        }
    }

    private fun updateChangeUnitsMenuItem(newUnit: MeasurementUnit) {
        binding.toolbar.menu.findItem(R.id.units).title = resources.getString(when (newUnit) {
            MeasurementUnit.METRIC -> R.string.change_to_imperial
            MeasurementUnit.IMPERIAL -> R.string.change_to_metric
        })
    }
}