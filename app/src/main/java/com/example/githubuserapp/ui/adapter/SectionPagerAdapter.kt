package com.example.githubuserapp.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuserapp.data.User
import com.example.githubuserapp.ui.fragment.FollowFragment

class SectionPagerAdapter(activity: AppCompatActivity, private val user: User) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowFragment(user, 0)
            1 -> fragment = FollowFragment(user, 1)
        }
        return fragment as Fragment
    }

}