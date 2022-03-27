package com.example.githubuserapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.data.UserRepository
import com.example.githubuserapp.data.local.entity.UserEntity

class UserAddUpdateViewModel (application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)
    fun insert(user: UserEntity) {
        mUserRepository.insert(user)
    }
    fun update(user: UserEntity) {
        mUserRepository.update(user)
    }
    fun delete(user: UserEntity) {
        mUserRepository.delete(user)
    }
}