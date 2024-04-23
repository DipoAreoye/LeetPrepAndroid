package com.leetprep.app.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leetprep.app.data.database.model.Submission
import com.leetprep.app.data.database.model.SubmissionFeedback
import kotlinx.coroutines.flow.Flow
@Dao
interface SubmissionFeedbackDao {
    @Query("SELECT * FROM submission_feedback WHERE submission_id = :submissionId")
    fun feedbackWithSubmissionId(submissionId: Long): Flow<SubmissionFeedback>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: Submission)

    @Delete
    suspend fun delete(submission: Submission)

}