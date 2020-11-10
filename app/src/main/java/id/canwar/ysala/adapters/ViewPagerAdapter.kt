package id.canwar.ysala.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.canwar.ysala.fragments.HistoryFragment
import id.canwar.ysala.fragments.HomeFragment
import id.canwar.ysala.fragments.HomestayFragment
import id.canwar.ysala.fragments.ProfileFragment
import id.canwar.ysala.helpers.TABS_COUNT
import java.lang.RuntimeException

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = HashMap<Int, Fragment>()

    override fun createFragment(position: Int): Fragment {
        val fragment = getFragment(position)
        fragments[position] = fragment
        return fragment
    }

    private fun getFragment(position: Int) = when (position) {
        0 -> HomeFragment()
        1 -> HistoryFragment()
        2 -> HomestayFragment()
        3 -> ProfileFragment()

        else -> throw RuntimeException("Trying to fetch unknown fragment id $position")
    }

    override fun getItemCount(): Int = TABS_COUNT
}
