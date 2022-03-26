package com.example.githubuserapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val username: String,
    val avatar: String,
    val company: String = "",
    val location: String = "",
    val followers: Int = 0,
    val following: Int = 0,
    val repository: Int = 0,
    val name: String = ""
) : Parcelable