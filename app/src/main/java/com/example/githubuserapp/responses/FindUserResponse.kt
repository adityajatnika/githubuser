package com.example.githubuserapp.responses

import com.google.gson.annotations.SerializedName

data class FindUserResponse(

    @field:SerializedName("items")
    val items: List<UserResponse>

)