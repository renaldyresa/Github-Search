package com.example.submission2fundamentalandroid.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.submission2fundamentalandroid.R
import com.example.submission2fundamentalandroid.ui.follower.FollowerFragment
import com.example.submission2fundamentalandroid.ui.following.FollowingFragment

class SectionPagerAdapter(private val mContext: Context,
                          fm: FragmentManager,
                          private val username: String
        ): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object{
        const val EXTRA_USERNAME = "extra_username"
    }

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.txt_follower,
        R.string.txt_following
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = fragmentInstance(FollowerFragment())
            1 -> fragment = fragmentInstance(FollowingFragment())
        }
        return fragment as Fragment
    }

    private fun fragmentInstance(fragment: Fragment): Fragment{
        return fragment.apply {
            arguments = Bundle().apply {
                putString(EXTRA_USERNAME, username)
            }
        }
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }


    override fun getCount(): Int {
        return TAB_TITLES.size
    }

}


