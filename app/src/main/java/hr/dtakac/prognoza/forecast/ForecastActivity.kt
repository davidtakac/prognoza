package hr.dtakac.prognoza.forecast

import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingActivity
import hr.dtakac.prognoza.databinding.ActivityForecastBinding
import hr.dtakac.prognoza.forecast.adapter.ForecastPagerAdapter
import hr.dtakac.prognoza.forecast.viewmodel.ForecastViewModel
import hr.dtakac.prognoza.places.PlacesActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForecastActivity : ViewBindingActivity<ActivityForecastBinding>(ActivityForecastBinding::inflate) {
    private val viewModel by viewModel<ForecastViewModel>()

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
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.forecast_tab_names)[position]
        }.attach()
    }

    private fun initializeToolbar() {
        binding.toolbar.setOnClickListener {
            startActivity(Intent(this, PlacesActivity::class.java))
        }
    }
}