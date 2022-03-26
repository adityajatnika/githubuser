package com.example.githubuserapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.data.User
import com.example.githubuserapp.data.remote.response.UserResponse
import com.example.githubuserapp.data.remote.retrofit.ApiConfig
import com.example.githubuserapp.utils.ResponseStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    val users = MutableLiveData<List<User>>()
    val isLoading = MutableLiveData(true)
    val stringError = MutableLiveData<String>()

    private fun setListUser(userResponses: List<UserResponse>) {
        val listUser = ArrayList<User>()
        for (user in userResponses) {
            listUser.add(
                User(user.id.toString(), user.login, user.avatarUrl)
            )
        }
        users.postValue(listUser)
    }

    fun getListUser(query: String) {
        isLoading.postValue(true)
        val client = ApiConfig.getApiService().getFollowers(query)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                isLoading.postValue(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setListUser(responseBody)
                    }
                } else {
                    val errorMessage = when (val statusCode = response.code()) {
                        ResponseStatus.BAD_REQUEST.stat -> "$statusCode : Bad Request"
                        ResponseStatus.FORBIDDEN.stat -> "$statusCode : Forbidden"
                        ResponseStatus.NOT_FOUND.stat -> "$statusCode : Not Found"
                        else -> "$statusCode"
                    }
                    Log.e(MainViewModel.TAG, errorMessage)
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                isLoading.postValue(false)
                stringError.postValue(t.message)
                Log.e(MainViewModel.TAG, t.message.toString())
                t.printStackTrace()
            }
        })
    }
}