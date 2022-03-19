package com.example.githubuserapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String,
    var username: String,
    var avatar: String,
    var company: String = "",
    var location: String = "",
    var followers: Int = 0,
    var following: Int = 0,
    var repository: Int = 0,
    var name: String = ""
) : Parcelable