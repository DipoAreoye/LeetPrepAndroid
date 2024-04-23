package com.leetprep.app.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leetprep.app.data.database.model.Submission
import kotlinx.coroutines.flow.Flow

@Dao
interface SubmissionDao {
    @Query("SELECT * FROM submission WHERE problem_id = :problemId")
    fun submissionWithProblemId(problemId: Long): Flow<Submission>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: Submission)

    @Delete
    suspend fun delete(submission: Submission)
}