package com.example.githubuserapp.ui.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.data.User
import com.example.githubuserapp.databinding.FragmentFollowBinding
import com.example.githubuserapp.ui.activity.ProfileActivity
import com.example.githubuserapp.ui.adapter.ListUserAdapter
import com.example.githubuserapp.ui.viewmodel.FollowViewModel

class FollowFragment(private val user: User, private val tab: Int) : Fragment() {

    private lateinit var followBinding: FragmentFollowBinding
    private val viewModel: FollowViewModel by viewModels()

    private val listUser = ArrayList<User>()
    private val adapter = ListUserAdapter(listUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    private fun setUpView() {
        showRecyclerList()
        viewModel.isLoading.observe(viewLifecycleOwner) {
            followBinding.progressBar.visibility = if (it) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

        viewModel.users.observe(viewLifecycleOwner) {
            if(it != null){
                val adapter = ListUserAdapter(it)
                followBinding.rvUser.adapter = adapter

                adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: User) {
                        showSelectedUser(data)
                    }
                })
            }
        }

        viewModel.stringError.observe(viewLifecycleOwner){
            if(it != null){
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followBinding = FragmentFollowBinding.inflate(layoutInflater, container, false)
        return followBinding.root

    }

    private fun showSelectedUser(user: User) {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        intent.putExtra(ProfileActivity.EXTRA_USER, user)
        startActivity(intent)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followBinding.rvUser.setHasFixedSize(true)
        followBinding.rvUser.adapter = this@FollowFragment.adapter
        followBinding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        setUpView()
        viewModel.getListUser(user.username, tab)
    }

    private fun showRecyclerList() {
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            followBinding.rvUser.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            followBinding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}