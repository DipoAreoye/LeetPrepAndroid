package com.leetprep.app.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leetprep.app.data.database.model.Problem
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemsDao {

    @Query("SELECT * FROM problem")
    fun getAll(): Flow<List<Problem>>

    @Query("SELECT * FROM problem WHERE id = :id")
    fun problemWithId(id: String): Flow<Problem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: Collection<Problem>)

    @Delete
    suspend fun delete(problem: Problem)
}