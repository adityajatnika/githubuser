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
import com.example.githubuserapp.ui.adapter.ListUserAdapter
import com.example.githubuserapp.databinding.FragmentFollowingBinding
import com.example.githubuserapp.data.User
import com.example.githubuserapp.ui.activity.ProfileActivity
import com.example.githubuserapp.ui.viewmodel.FollowingViewModel


class FollowingFragment(private val user: User) : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private val viewModel : FollowingViewModel by viewModels()
    private val listUser = ArrayList<User>()
    private val adapter = ListUserAdapter(listUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }
    private fun setUpView(){
        showRecyclerList()
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

        viewModel.users.observe(viewLifecycleOwner) {
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
        setUpView()
        viewModel.getListUser(user.username)
    }

    private fun showRecyclerList() {
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}