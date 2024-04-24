package com.leetprep.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leetprep.app.data.database.dao.ProblemDao
import com.leetprep.app.data.database.dao.SubmissionDao
import com.leetprep.app.data.database.dao.SubmissionFeedbackDao
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.data.database.model.Submission
import com.leetprep.app.data.database.model.SubmissionFeedback

@Database(entities = [Problem::class, Submission::class, SubmissionFeedback::class], version = 4)
abstract class LeetPrepDatabase : RoomDatabase() {
    abstract fun problemsDao() : ProblemDao
    abstract fun submissionDao() : SubmissionDao
    abstract fun submissionFeedbackDao() : SubmissionFeedbackDao


}