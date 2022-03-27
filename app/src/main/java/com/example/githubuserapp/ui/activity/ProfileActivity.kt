package com.example.githubuserapp.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.data.User
import com.example.githubuserapp.data.local.entity.UserEntity
import com.example.githubuserapp.databinding.ActivityProfileBinding
import com.example.githubuserapp.ui.adapter.SectionPagerAdapter
import com.example.githubuserapp.ui.viewmodel.ProfileViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    var isChecked = false
//    private lateinit var userRoom: UserEntity = UserEntity()

//    private var _activityNoteAddUpdateBinding: ActivityNoteAddUpdateBinding? = null
//    private val binding get() = _activityNoteAddUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.title = getString(R.string.detail_user)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        noteAddUpdateViewModel = obtainViewModel(this@NoteAddUpdateActivity)

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

        viewModel.viewModelScope.launch {
            updateFavButton(user.id)
        }

        binding.apply {
            btnFav.setOnClickListener {
//                userRoom.let {
//                    userRoom.id = user.id
//                    userRoom.username = user.username
//                    userRoom.urlToImage = user.avatar
//                }
                if(!isChecked){
                    viewModel.insert(UserEntity(user.id, user.username, user.avatar))
                    showToast("Telah Ditambahkan")
                    binding.btnFav.setImageDrawable(ContextCompat.getDrawable(binding.btnFav.context, R.drawable.ic_baseline_favorite_24))
//                    isChecked=false
                } else {
                    viewModel.delete(UserEntity(user.id, user.username, user.avatar))
                    showToast("Telah Dihapus")
                    binding.btnFav.setImageDrawable(ContextCompat.getDrawable(binding.btnFav.context, R.drawable.ic_baseline_favorite_border_24))
//                    isChecked=true
                }
                isChecked = !isChecked
            }
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private suspend fun updateFavButton(id: String) {
        if(viewModel.isFavorite(id)==true){
            binding.btnFav.setImageDrawable(ContextCompat.getDrawable(binding.btnFav.context, R.drawable.ic_baseline_favorite_24))
            isChecked=true
        } else {
            binding.btnFav.setImageDrawable(ContextCompat.getDrawable(binding.btnFav.context, R.drawable.ic_baseline_favorite_border_24))
            isChecked=false
        }
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
                        R.string.followers
                    ).lowercase(
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
//    override fun onDestroy() {
//        super.onDestroy()
//        _activityNoteAddUpdateBinding = null
//    }
//    private fun obtainViewModel(activity: AppCompatActivity): NoteAddUpdateViewModel {
//        val factory = ViewModelFactory.getInstance(activity.application)
//        return ViewModelProvider(activity, factory).get(NoteAddUpdateViewModel::class.java)
//    }

    companion object{
        const val EXTRA_USER = "extra_person"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}