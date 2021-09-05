package hr.dtakac.prognoza.activity

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import hr.dtakac.prognoza.*
import hr.dtakac.prognoza.adapter.ForecastPagerAdapter
import hr.dtakac.prognoza.databinding.ActivityForecastBinding
import hr.dtakac.prognoza.fragment.PlaceSearchDialogFragment
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.viewmodel.ForecastActivityViewModel
import hr.dtakac.prognoza.widget.MediumCurrentConditionsAppWidgetProvider
import hr.dtakac.prognoza.widget.SmallCurrentConditionsAppWidgetProvider
import hr.dtakac.prognoza.widget.TinyCurrentConditionsAppWidgetProvider
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
        initializeChangedPlaceFragmentResultListener()
        viewModel.getSelectedPlaceName()
        viewModel.getSelectedUnit()
    }

    override fun onResume() {
        super.onResume()
        viewModel.cleanUpDatabase()
    }

    private fun observeViewModel() {
        viewModel.placeName.observe(this) {
            binding.toolbar.title = it
        }
        viewModel.selectedUnit.observe(this) { unit ->
            binding.toolbar.menu.findItem(R.id.units).title = resources.getString(
                when (unit) {
                    MeasurementUnit.METRIC -> R.string.change_to_imperial
                    MeasurementUnit.IMPERIAL -> R.string.change_to_metric
                }
            )
        }
        viewModel.placeChangedEvent.observe(this) {
            if (!it.isConsumed) {
                notifyFragmentsOfNewPlace()
                updateWidgets()
                it.consume()
            }
        }
        viewModel.unitChangedEvent.observe(this) {
            if (!it.isConsumed) {
                notifyFragmentsOfChangedUnits()
                updateWidgets()
                it.consume()
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
                    viewModel.changeSelectedUnit()
                    true
                }
                else -> false
            }
        }
    }

    private fun initializeChangedPlaceFragmentResultListener() {
        supportFragmentManager.setFragmentResultListener(
            PLACE_SEARCH_REQUEST_KEY,
            this,
            { _, bundle ->
                if (bundle.getBoolean(BUNDLE_KEY_PLACE_PICKED)) {
                    viewModel.changeSelectedPlace()
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

    private fun notifyFragmentsOfChangedUnits() {
        val result = Bundle().apply { putBoolean(BUNDLE_KEY_UNITS_CHANGED, true) }
        supportFragmentManager.apply {
            setFragmentResult(TODAY_REQUEST_KEY, result)
            setFragmentResult(TOMORROW_REQUEST_KEY, result)
            setFragmentResult(DAYS_REQUEST_KEY, result)
        }
    }

    private fun updateWidgets() {
        val widgetProviders = listOf(
            TinyCurrentConditionsAppWidgetProvider::class.java,
            SmallCurrentConditionsAppWidgetProvider::class.java,
            MediumCurrentConditionsAppWidgetProvider::class.java
        )
        widgetProviders.forEach {
            val intent = Intent(this, it)
            intent.action = ACTION_APP_WIDGET_CURRENT_CONDITIONS_UPDATE
            PendingIntent.getBroadcast(
                this,
                REQUEST_CODE_APP_WIDGET_CURRENT_CONDITIONS_UPDATE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            ).send()
        }
    }
}