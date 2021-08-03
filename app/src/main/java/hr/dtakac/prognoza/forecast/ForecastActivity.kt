package hr.dtakac.prognoza.forecast

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.ViewBindingActivity
import hr.dtakac.prognoza.databinding.ActivityForecastBinding
import hr.dtakac.prognoza.forecast.adapter.ForecastPagerAdapter
import hr.dtakac.prognoza.forecast.viewmodel.ForecastViewModel
import hr.dtakac.prognoza.places.PlacesActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForecastActivity :
    ViewBindingActivity<ActivityForecastBinding>(ActivityForecastBinding::inflate) {
    private val viewModel by viewModel<ForecastViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
        initializeViewPager()
        initializeToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_forecast, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                startActivity(Intent(this, PlacesActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        setSupportActionBar(binding.toolbar)
    }
}