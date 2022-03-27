package com.example.githubuserapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuserapp.data.UserRepository
import com.example.githubuserapp.data.local.entity.UserEntity

class FavoriteViewModel (application: Application) : AndroidViewModel(application) {
    private val mUserRepository: UserRepository = UserRepository(application)
    fun getUserFavorite(): LiveData<List<UserEntity>> = mUserRepository.getUserFavorite()
}