package com.example.githubuserapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuserapp.FollowersFragment
import com.example.githubuserapp.FollowingFragment
import com.example.githubuserapp.model.User

class SectionPagerAdapter(activity: AppCompatActivity, private val user: User) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment(user)
            1 -> fragment = FollowingFragment(user)
        }
        return fragment as Fragment
    }

}