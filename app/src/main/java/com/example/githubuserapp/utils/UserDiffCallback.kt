package com.example.githubuserapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.githubuserapp.data.local.entity.UserEntity

class UserDiffCallback(private val mOldUserList: List<UserEntity>, private val mNewUserList: List<UserEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldUserList.size
    }
    override fun getNewListSize(): Int {
        return mNewUserList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserList[oldItemPosition].id == mNewUserList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = mOldUserList[oldItemPosition]
        val newUser = mNewUserList[newItemPosition]
        return oldUser.id == newUser.id && oldUser.username == newUser.username
    }
}