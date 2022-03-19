package com.example.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.adapter.ListUserAdapter
import com.example.githubuserapp.adapter.SectionPagerAdapter
import com.example.githubuserapp.databinding.ActivityProfileBinding
import com.example.githubuserapp.model.ProfileViewModel
import com.example.githubuserapp.model.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.StringBuilder
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

//        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        getDetailUser()

//        binding.apply {
//            tvNama.text = user.username
//            tvCompany.text = StringBuilder(getString(R.string.company)).append(": ").append(user.company)
//            tvLocation.text = StringBuilder("Lokasi: ").append(user.location)
//            tvFollowers.text = StringBuilder(user.followers).append(" ").append(getString(R.string.followers).lowercase(
//                Locale.getDefault()
//            ))
//            tvFollowing.text = StringBuilder(user.following).append(" diikuti")
//            tvRepository.text = StringBuilder(user.repository).append(" repositori")
//            tvUsername.text = StringBuilder("ID: ").append(user.username)
//            Glide.with(baseContext)
//                .load(user.avatar)
//                .circleCrop()
//                .into(imgUser)
//        }
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val sectionsPagerAdapter = SectionPagerAdapter(this, user)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

    }

    private fun getDetailUser() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val url = "https://api.github.com/users/${user.username}"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val name = responseObject.getString("name")
                    val login = responseObject.getString("login")
                    val company = responseObject.getString("company")
                    val location = responseObject.getString("location")
                    val avatar = responseObject.getString("avatar_url")
                    val followers = responseObject.getInt("followers")
                    val repos = responseObject.getInt("public_repos")
                    val following = responseObject.getInt("following")

                    binding.apply {
                        tvNama.text = name
                        tvUsername.text = StringBuilder("@").append(login)
                        tvCompany.text = StringBuilder(getString(R.string.company)).append(": ").append(company)
                        tvLocation.text = StringBuilder(getString(R.string.location)).append(": ").append(location)
                        Glide.with(baseContext)
                            .load(avatar)
                            .circleCrop()
                            .into(imgUser)
                        tvFollowers.text = StringBuilder(followers.toString()).append(" ").append(getString(R.string.followers).lowercase(
                            Locale.getDefault()
                        ))
                        tvFollowing.text = StringBuilder(following.toString()).append(" diikuti")
                        tvRepository.text = StringBuilder(repos.toString()).append(" repositori")
                    }

//                    binding.tvQuote.text = quote
//                    binding.tvAuthor.text = author
                } catch (e: Exception) {
                    Toast.makeText(this@ProfileActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@ProfileActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object{
        const val EXTRA_USER = "extra_person"
        private val TAG = MainActivity::class.java.simpleName
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

}