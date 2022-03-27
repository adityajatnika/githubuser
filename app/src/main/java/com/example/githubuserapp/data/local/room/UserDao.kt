package com.example.githubuserapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuserapp.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getUserFavorite(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: UserEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Delete
    fun delete(user : UserEntity)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id)")
    suspend fun isUserFavorite(id: String): Boolean

}