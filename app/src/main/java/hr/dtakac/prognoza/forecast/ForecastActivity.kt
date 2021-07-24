package hr.dtakac.prognoza.forecast

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingActivity
import hr.dtakac.prognoza.databinding.ActivityMainBinding
import hr.dtakac.prognoza.forecast.adapter.ForecastPagerAdapter
import hr.dtakac.prognoza.forecast.viewmodel.ForecastViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "MainActivity"
class ForecastActivity : ViewBindingActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel by viewModel<ForecastViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
        initializeViewPager()
        initializeToolbar()
        viewModel.getLocationName()
    }

    private fun observeViewModel() {
        viewModel.locationName.observe(this) {
            binding.toolbar.title = it
        }
    }

    private fun initializeViewPager() {
        binding.viewPager.adapter = ForecastPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.forecast_tab_names)[position]
        }.attach()
    }

    private fun initializeToolbar() {
        binding.toolbar.setOnClickListener {
            Snackbar.make(binding.root, "Pick location", Snackbar.LENGTH_LONG).show()
        }
    }
}