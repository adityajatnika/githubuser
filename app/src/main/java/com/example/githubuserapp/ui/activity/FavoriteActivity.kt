package com.example.githubuserapp.ui.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.data.User
import com.example.githubuserapp.data.local.entity.UserEntity
import com.example.githubuserapp.databinding.ActivityFavoriteBinding
import com.example.githubuserapp.ui.adapter.ListUserAdapter
import com.example.githubuserapp.ui.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.title = getString(R.string.favorite_user)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.setHasFixedSize(true)

        setUpView()

        viewModel.getUserFavorite()

    }

    private fun setUpView() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }

        viewModel.getUserFavorite().observe(this) {
            if(it!=null){
                val adapter = ListUserAdapter(mappingListToArray(it))
                binding.rvUser.adapter = adapter

                adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: User) {
                        showSelectedUser(data)
                    }
                })
            }
        }
    }

    private fun mappingListToArray(users : List<UserEntity>): ArrayList<User>{
        val temp = ArrayList<User>()
        for(user in users){
            val mapped = User(
                user.id,
                user.username,
                user.urlToImage
            )
            temp.add(mapped)
        }
        return temp
    }

    private fun showSelectedUser(user: User) {
        val intent = Intent(this@FavoriteActivity, ProfileActivity::class.java)
        intent.putExtra(ProfileActivity.EXTRA_USER, user)
        startActivity(intent)
    }

    companion object {
        private val TAG = FavoriteActivity::class.java.simpleName
    }
}