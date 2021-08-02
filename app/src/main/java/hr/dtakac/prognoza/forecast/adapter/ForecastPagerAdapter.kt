package hr.dtakac.prognoza.forecast.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import hr.dtakac.prognoza.forecast.fragment.DaysFragment
import hr.dtakac.prognoza.forecast.fragment.TodayFragment
import hr.dtakac.prognoza.forecast.fragment.TomorrowFragment

class ForecastPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFragment()
            1 -> TomorrowFragment()
            2 -> DaysFragment()
            else -> throw IllegalStateException("No fragment for position $position.")
        }
    }

    override fun getItemCount() = 3
}