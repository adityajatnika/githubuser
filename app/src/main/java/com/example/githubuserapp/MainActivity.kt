package com.example.githubuserapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.adapter.ListUserAdapter
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
//    private val list = ArrayList<User>()

    private lateinit var binding: ActivityMainBinding
    private val baseUrl = "https://api.github.com/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = getString(R.string.list_user)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.setHasFixedSize(true)

//        list.addAll(listUser)
//        showRecyclerList()

        getListUser("users")
    }

    private fun getListUser(nextUrl : String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = StringBuilder(baseUrl).append(nextUrl).toString()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                showRecyclerList()
                val listUser = ArrayList<User>()
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = if (nextUrl == "users"){
                        JSONArray(result)
                    } else {
                        val jsonObject = JSONObject(result)
                        jsonObject.getJSONArray("items")
                    }
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val login = jsonObject.getString("login")
                        val avatar = jsonObject.getString("avatar_url")
                        val id = jsonObject.getString("id")
                        val user = User(id, login, avatar)
                        listUser.add(user)
                    }
                    val adapter = ListUserAdapter(listUser)
                    binding.rvUser.adapter = adapter

                    adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: User) {
                            showSelectedUser(data)
                        }
                    })
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }
//        val listHeroAdapter = ListUserAdapter(list)
//        binding.rvUser.adapter = listHeroAdapter
//
//        listHeroAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: User) {
//                showSelectedUser(data)
//            }
//        })
    }

//    private val listUser: ArrayList<User>
//        get() {
//            val dataName = resources.getStringArray(R.array.name)
//            val dataUsername = resources.getStringArray(R.array.username)
//            val dataPhoto = resources.obtainTypedArray(R.array.avatar)
//            val dataCompany = resources.getStringArray(R.array.company)
//            val dataLocation = resources.getStringArray(R.array.location)
//            val dataFollowers = resources.getStringArray(R.array.followers)
//            val dataFollowing = resources.getStringArray(R.array.following)
//            val dataRepository = resources.getStringArray(R.array.repository)
//            val listUser = ArrayList<User>()
//            for (i in dataName.indices) {
//                val user = User(dataName[i],dataUsername[i], dataPhoto.getResourceId(i, -1),
//                dataCompany[i],dataLocation[i], dataFollowers[i], dataFollowing[i], dataRepository[i])
//                listUser.add(user)
//            }
//            dataPhoto.recycle()
//            return listUser
//        }

    private fun showSelectedUser(user: User) {
        val intent = Intent(this@MainActivity, ProfileActivity::class.java)
        intent.putExtra(ProfileActivity.EXTRA_USER, user)
        startActivity(intent)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                getListUser("search/users?q=${query}")
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu1 -> {
                Toast.makeText(baseContext, "Fitur Belum Tersedia", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu2 -> {
                Toast.makeText(baseContext, "Fitur Belum Tersedia", Toast.LENGTH_SHORT).show()
                true
            }
            else -> true
        }


    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.setting_menu, menu)
//        return true
//    }

    //    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.menu1 -> {
////-------------------Kode Night Mode
//                return true
//            }
//            else -> {true}
//        }
//    }
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

}