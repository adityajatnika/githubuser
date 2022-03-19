package com.example.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.ApiConfig
import com.example.githubuserapp.model.User
import com.example.githubuserapp.responses.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    val users = MutableLiveData<List<User>>()
    val isLoading = MutableLiveData(true)
    val stringError = MutableLiveData<String>()

    fun getListUser(nextUrl: String) {
        isLoading.postValue(true)

        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                isLoading.postValue(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val listUser = ArrayList<User>()
                        for (user in responseBody) {
                            listUser.add(
                                User(user.id.toString(), user.login, user.avatarUrl)
                            )
                        }
                        users.postValue(listUser)
                    }
                } else {
                    val errorMessage = when (val statusCode = response.code()) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode"
                    }
                    Log.e(TAG, errorMessage)
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                isLoading.postValue(false)
                stringError.postValue(t.message)
                Log.e(TAG, t.message.toString())
                t.printStackTrace()
            }
        })
    }

    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
    }
}