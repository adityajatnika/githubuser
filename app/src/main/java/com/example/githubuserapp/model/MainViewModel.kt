package com.example.githubuserapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel(){
    val users = MutableLiveData<List<User>>()
    val isLoading = MutableLiveData(true)
    val stringError = MutableLiveData<String>()
    private val baseUrl = "https://api.github.com/"

    fun getListUser(nextUrl : String) {
        isLoading.postValue(true)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = StringBuilder(baseUrl).append(nextUrl).toString()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                isLoading.postValue(false)
                val listUser = ArrayList<User>()
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = if (nextUrl == "users"){
                        JSONArray(result)
                    } else {
                        val jsonObject = JSONObject(result)
                        jsonObject.getJSONArray("items")
                    }
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val login = jsonObject.getString("login")
                        val avatar = jsonObject.getString("avatar_url")
                        val id = jsonObject.getString("id")
                        val user = User(id, login, avatar)
                        listUser.add(user)
                    }
                    users.postValue(listUser)
                } catch (e: Exception) {
                    stringError.postValue(e.message)
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                isLoading.postValue(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                stringError.postValue(errorMessage)
            }
        })
    }

    companion object{
        private val TAG = ProfileViewModel::class.java.simpleName
    }
}