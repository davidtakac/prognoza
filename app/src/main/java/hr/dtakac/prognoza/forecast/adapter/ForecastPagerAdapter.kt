package hr.dtakac.prognoza.forecast.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import hr.dtakac.prognoza.forecast.fragment.TodayFragment
import java.lang.IllegalStateException

class ForecastPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFragment()
            else -> throw IllegalStateException("No fragment for position $position.")
        }
    }

    override fun getItemCount() = 1
}