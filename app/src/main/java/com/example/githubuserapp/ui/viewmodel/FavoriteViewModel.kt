package com.example.githubuserapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.data.UserRepository
import com.example.githubuserapp.data.local.entity.UserEntity

class FavoriteViewModel (application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)
    fun getUserFavorite(): LiveData<List<UserEntity>> = mUserRepository.getUserFavorite()
}