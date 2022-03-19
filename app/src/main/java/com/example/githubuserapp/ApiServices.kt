package com.example.githubuserapp

import com.example.githubuserapp.responses.DetailUserResponse
import com.example.githubuserapp.responses.FindUserResponse
import com.example.githubuserapp.responses.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("users")
    @Headers("Authorization: token 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")
    fun getUsers(): Call<List<UserResponse>>

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun findUsers(@Query("q") query: String): Call<FindUserResponse>

    @GET("users/{login}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(@Path("login") login : String): Call<List<UserResponse>>

    @GET("users/{login}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(@Path("login") login : String): Call<List<UserResponse>>

    @GET("users/{login}")
    @Headers("Authorization: token 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")
    fun getDetailUser(@Path("login") login : String): Call<DetailUserResponse>
}