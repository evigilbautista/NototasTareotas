package com.example.marsphotos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.marsphotos.model.Recordatorios
import com.example.marsphotos.model.Users

@Dao
interface UsersDAO {

@Insert
fun insert(users: Users)

    @Query("SELECT * FROM users WHERE idUser = :idUser")
    fun getRecordatorios(idUser: Int): List<Users>

}