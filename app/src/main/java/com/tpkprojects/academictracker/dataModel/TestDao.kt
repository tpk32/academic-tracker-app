package com.tpkprojects.academictracker.dataModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTest(test: Test): Long

    @Transaction
    @Query("""SELECT * FROM Test t
        INNER JOIN Subject s ON t.uniqueSubjectId = s.uniqueSubjectId
        INNER JOIN User u ON s.userId = u.userId
        WHERE u.userId = :userId
        ORDER BY testDate DESC
    """)
    fun getAllTests(userId: String): Flow<List<Test>>

    @Query("SELECT * FROM Test WHERE uniqueSubjectId = :uniqueSubjectId ORDER BY testDate DESC")
    fun getTestsBySubjectId(uniqueSubjectId: String): Flow<List<Test>>

    @Query("DELETE FROM Test WHERE uniqueSubjectId = :uniqueSubjectId AND testId = :testId")
    suspend fun deleteTestById(uniqueSubjectId: String, testId: Long)
}