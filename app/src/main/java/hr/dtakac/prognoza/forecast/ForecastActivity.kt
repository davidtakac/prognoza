package hr.dtakac.prognoza.forecast

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingActivity
import hr.dtakac.prognoza.databinding.ActivityForecastBinding
import hr.dtakac.prognoza.forecast.adapter.ForecastPagerAdapter
import hr.dtakac.prognoza.forecast.viewmodel.ForecastViewModel
import hr.dtakac.prognoza.places.PlaceSearchDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val SEARCH_FRAGMENT_TAG = "search"
class ForecastActivity :
    ViewBindingActivity<ActivityForecastBinding>(ActivityForecastBinding::inflate) {
    private val viewModel by viewModel<ForecastViewModel>()

    companion object {
        const val REQUEST_KEY = "forecast_request_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
        initializeViewPager()
        initializeToolbar()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaceName()
        viewModel.cleanUpDatabase()
    }

    private fun observeViewModel() {
        viewModel.placeName.observe(this) {
            binding.toolbar.title = it
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
                else -> false
            }
        }
        supportFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            this,
            { _, bundle ->
                if (bundle.getBoolean(PlaceSearchDialogFragment.RESULT_PLACE_PICKED)) {
                    viewModel.getPlaceName()
                    supportFragmentManager.popBackStack()
                }
            }
        )
    }

    private fun openSearch() {
        /*supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .add(R.id.container_search, PlaceSearchDialogFragment::class.java, null, SEARCH_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
        binding.layoutSearch.visibility = View.VISIBLE*/
        PlaceSearchDialogFragment().show(supportFragmentManager, SEARCH_FRAGMENT_TAG)
    }
}