package com.example.githubuserapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.data.remote.retrofit.ApiConfig
import com.example.githubuserapp.utils.ResponseStatus
import com.example.githubuserapp.data.remote.response.DetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel(){
    var userData = MutableLiveData<DetailUserResponse>()
    val isLoading = MutableLiveData(true)
    val stringError = MutableLiveData<String>()

    private fun setDetailUser(user: DetailUserResponse) {
        userData.postValue(user)
    }

    fun getDetail(query: String) {
        isLoading.postValue(true)
        val client = ApiConfig.getApiService().getDetailUser(query)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                isLoading.postValue(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setDetailUser(responseBody)
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

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                isLoading.postValue(false)
                stringError.postValue(t.message)
                Log.e(MainViewModel.TAG, t.message.toString())
                t.printStackTrace()
            }
        })
    }
}