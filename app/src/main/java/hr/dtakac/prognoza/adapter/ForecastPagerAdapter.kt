package hr.dtakac.prognoza.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import hr.dtakac.prognoza.fragment.DaysForecastFragment
import hr.dtakac.prognoza.fragment.TodayForecastFragment
import hr.dtakac.prognoza.fragment.TomorrowForecastFragment

class ForecastPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayForecastFragment()
            1 -> TomorrowForecastFragment()
            2 -> DaysForecastFragment()
            else -> throw IllegalStateException("No fragment for position $position.")
        }
    }

    override fun getItemCount() = 3
}