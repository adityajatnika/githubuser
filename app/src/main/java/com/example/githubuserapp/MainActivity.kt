package com.example.githubuserapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.adapter.ListUserAdapter
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.model.User
import com.example.githubuserapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = getString(R.string.list_user)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.setHasFixedSize(true)

        setUpView()

        viewModel.getListUser()
    }

    private fun setUpView() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

        viewModel.users.observe(this) {
            if(it != null){
                val adapter = ListUserAdapter(it)
                binding.rvUser.adapter = adapter

                adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: User) {
                        showSelectedUser(data)
                    }
                })
            }
        }

        viewModel.stringError.observe(this){
            if(it != null){
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

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
                viewModel.getListUser(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if(searchView.query.toString().isEmpty()) {
                    viewModel.getListUser()
                }
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

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

}