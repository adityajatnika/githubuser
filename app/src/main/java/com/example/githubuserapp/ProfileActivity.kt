package com.example.githubuserapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.adapter.SectionPagerAdapter
import com.example.githubuserapp.databinding.ActivityProfileBinding
import com.example.githubuserapp.model.User
import com.example.githubuserapp.viewmodel.ProfileViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.title = getString(R.string.detail_user)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setUpView()
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        viewModel.getDetail(user.username)

        val sectionsPagerAdapter = SectionPagerAdapter(this, user)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

    }



    private fun setUpView() {

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.INVISIBLE
        }

        viewModel.userData.observe(this) { user ->
            if(user != null){
                binding.apply {
                    tvNama.text = user.name
                    tvUsername.text = java.lang.StringBuilder("@").append(user.login)
                    tvCompany.text = StringBuilder(getString(R.string.company)).append(": ").append(user.company)
                    tvLocation.text = StringBuilder(getString(R.string.location)).append(": ").append(user.location)
                    Glide.with(baseContext)
                        .load(user.avatarUrl)
                        .circleCrop()
                        .into(imgUser)
                    tvFollowers.text = java.lang.StringBuilder(user.followers.toString()).append(" ").append(getString(
                        R.string.followers).lowercase(
                        Locale.getDefault()
                    ))
                    tvFollowing.text = java.lang.StringBuilder(user.following.toString()).append(" diikuti")
                    tvRepository.text = java.lang.StringBuilder(user.publicRepos.toString()).append(" repositori")
                }
            }
        }

        viewModel.stringError.observe(this) {
            if (it != null){
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        const val EXTRA_USER = "extra_person"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}