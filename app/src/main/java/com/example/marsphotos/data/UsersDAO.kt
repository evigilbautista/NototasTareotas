package com.example.marsphotos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.marsphotos.model.Users

@Dao
interface UsersDAO {
   @Insert
    fun insert(users: Users)
 // Checar porque no funciona el users
    // @Query ("Select * From users WHERE id = :idNota")
  //  fun getAll(): List<Users>

}