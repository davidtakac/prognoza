package hr.dtakac.prognoza.forecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.databinding.ActivityMainBinding
import hr.dtakac.prognoza.forecast.adapter.ForecastPagerAdapter

private const val TAG = "MainActivity"
class ForecastActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViewPager()
    }

    private fun initializeViewPager() {
        binding.viewPager.adapter = ForecastPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.forecast_tab_names)[position]
        }.attach()
    }
}