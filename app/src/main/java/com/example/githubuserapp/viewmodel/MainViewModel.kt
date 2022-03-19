package com.example.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.ApiConfig
import com.example.githubuserapp.model.User
import com.example.githubuserapp.responses.FindUserResponse
import com.example.githubuserapp.responses.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
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

    fun getListUser(query: String = "") {
        isLoading.postValue(true)

        if(query.isEmpty()) {
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
                            setListUser(responseBody)
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
        } else {
            val client = ApiConfig.getApiService().findUsers(query)
            client.enqueue(object : Callback<FindUserResponse> {
                override fun onResponse(
                    call: Call<FindUserResponse>,
                    response: Response<FindUserResponse>
                ) {
                    isLoading.postValue(false)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            setListUser(responseBody.items)
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

                override fun onFailure(call: Call<FindUserResponse>, t: Throwable) {
                    isLoading.postValue(false)
                    stringError.postValue(t.message)
                    Log.e(TAG, t.message.toString())
                    t.printStackTrace()
                }
            })
        }
    }

    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
    }
}