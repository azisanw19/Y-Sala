package id.canwar.ysala.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import id.canwar.ysala.R
import id.canwar.ysala.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.lang.RuntimeException

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initFragment()
    }

    private fun initFragment() {
        view_pager.adapter = ViewPagerAdapter(this)

        /** Binding view_pager and TabLayout **/
        TabLayoutMediator(main_tabs_holder, view_pager) { tab, position -> tab.icon = when (position) {
            0 -> getDrawable(R.drawable.ic_home_white)
            1 -> getDrawable(R.drawable.ic_booking_history)
            2 -> getDrawable(R.drawable.img_icon)
            3 -> getDrawable(R.drawable.ic_profile_white)
            else -> throw RuntimeException("Trying to fetch unknown fragment id $position")
        } }.attach()
    }

}