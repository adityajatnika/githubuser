package com.example.githubuserapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ProfileViewModel : ViewModel(){
    val userData = MutableLiveData<User>()
    val isLoading = MutableLiveData<Boolean>(true)
    val stringError = MutableLiveData<String>()

    fun getDetailUser(user: User) {
        isLoading.postValue(true)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/${user.username}"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                // Jika koneksi berhasil
                isLoading.postValue(false)
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val id = responseObject.getString("id")
                    val name = responseObject.getString("name")
                    val login = responseObject.getString("login")
                    val company = responseObject.getString("company")
                    val location = responseObject.getString("location")
                    val avatar = responseObject.getString("avatar_url")
                    val followers = responseObject.getInt("followers")
                    val repos = responseObject.getInt("public_repos")
                    val following = responseObject.getInt("following")
                    val userModel = User(id, login, avatar, company, location, followers, following, repos, name)
                    userData.postValue(userModel)
                } catch (e: Exception) {
                    stringError.postValue(e.message)
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
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