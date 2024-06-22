package com.tpkprojects.academictracker.dataModel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User")
    suspend fun getUser(): User?

    @Delete
    suspend fun deleteUser(user: User)

}