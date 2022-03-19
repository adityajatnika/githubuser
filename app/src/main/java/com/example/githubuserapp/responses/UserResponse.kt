package com.example.githubuserapp.responses

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("login")
	val login: String
)
