package com.example.githubuserapp

import com.example.githubuserapp.responses.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiServices {
    @GET("users")
    @Headers("Authorization: token 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")
    fun getUsers(): Call<List<UserResponse>>
}