package uk.ac.kent.pceh3.gulbstudent

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by pceh3 on 25/05/2019.
 */

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val COUNT = 3

    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = WhatsOnFragment()
            1 -> fragment = DealsFragment()
            2 -> fragment = BlogFragment()
        }

        return fragment
    }

    override fun getCount(): Int {
        return COUNT
    }


    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "What's On"
        } else if (position == 1) {
            title = "Deals"
        } else if (position == 2) {
            title = "Blog"
        }
        return title
    }
}