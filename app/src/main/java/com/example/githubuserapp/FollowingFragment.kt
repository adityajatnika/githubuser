package com.example.githubuserapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.adapter.ListUserAdapter
import com.example.githubuserapp.databinding.FragmentFollowingBinding
import com.example.githubuserapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray


class FollowingFragment(private val user: User) : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private val baseUrl = "https://api.github.com/"
    private val listUser = ArrayList<User>()
    private val adapter = ListUserAdapter(listUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    private fun getListUser() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = StringBuilder(baseUrl).append("users/${user.username}/following").toString()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                showRecyclerList()
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val login = jsonObject.getString("login")
                        val avatar = jsonObject.getString("avatar_url")
                        val id = jsonObject.getString("id")
                        val user = User(id, login, avatar)
                        listUser.add(user)
                    }
                    binding.rvUser.adapter = adapter

                    adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: User) {
                            showSelectedUser(data)
                        }
                    })
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun showSelectedUser(user: User) {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        intent.putExtra(ProfileActivity.EXTRA_USER, user)
        startActivity(intent)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = this@FollowingFragment.adapter
        binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        getListUser()
    }

    private fun showRecyclerList() {
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        private val TAG = FollowingFragment::class.simpleName
    }
}