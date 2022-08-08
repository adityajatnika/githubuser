package com.example.githubuserapp.data.remote.retrofit

import com.example.githubuserapp.BuildConfig
import com.example.githubuserapp.data.remote.response.DetailUserResponse
import com.example.githubuserapp.data.remote.response.FindUserResponse
import com.example.githubuserapp.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("users")
    @Headers("Authorization: token "+BuildConfig.API_TOKEN)
    fun getUsers(): Call<List<UserResponse>>

    @GET("search/users")
    @Headers("Authorization: token "+BuildConfig.API_TOKEN)
    fun findUsers(@Query("q") query: String): Call<FindUserResponse>

    @GET("users/{login}/followers")
    @Headers("Authorization: token "+BuildConfig.API_TOKEN)
    fun getFollowers(@Path("login") login : String): Call<List<UserResponse>>

    @GET("users/{login}/following")
    @Headers("Authorization: token "+BuildConfig.API_TOKEN)
    fun getFollowing(@Path("login") login : String): Call<List<UserResponse>>

    @GET("users/{login}")
    @Headers("Authorization: token "+BuildConfig.API_TOKEN)
    fun getDetailUser(@Path("login") login : String): Call<DetailUserResponse>
}