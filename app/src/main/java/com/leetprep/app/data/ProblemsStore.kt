package com.leetprep.app.data

import com.leetprep.app.data.database.dao.ProblemDao
import com.leetprep.app.data.database.dao.SubmissionDao
import com.leetprep.app.data.database.dao.SubmissionFeedbackDao
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.data.database.model.Submission
import com.leetprep.app.data.database.model.SubmissionFeedback
import kotlinx.coroutines.flow.Flow

interface ProblemStore {
    fun problemWithId(id: Long): Flow<Problem>
    fun submissionWithProblemId(id: Long): Flow<Submission>
    fun submissionFeedbackWithProblemId(id: Long): Flow<SubmissionFeedback>
    fun problemsSortedByDifficulty(): Flow<List<Problem>>
    suspend fun addProblems(problems: List<Problem>)
    suspend fun updateSubmission(submission: Submission)
    suspend fun updateSubmissionFeedback(feedback: SubmissionFeedback)
}

class LocalProblemStore (
    private val problemDao: ProblemDao,
    private val submissionDao: SubmissionDao,
    private val submissionFeedbackDao: SubmissionFeedbackDao
): ProblemStore {
    override fun problemWithId(id: Long): Flow<Problem> {
        return problemDao.problemWithId(id)
    }
    override fun submissionWithProblemId(id: Long): Flow<Submission> {
        return submissionDao.submissionWithProblemId(id)
    }

    override fun submissionFeedbackWithProblemId(id: Long): Flow<SubmissionFeedback> {
        return submissionFeedbackDao.feedbackWithProblemId(id)
    }

    override fun problemsSortedByDifficulty(): Flow<List<Problem>> {
        return problemDao.getAll()
    }
    override suspend fun addProblems(problems: List<Problem>) {
        problemDao.insertAll(problems);
    }

    override suspend fun updateSubmission(submission: Submission) {
        submissionDao.insert(submission)
    }

    override suspend fun updateSubmissionFeedback(feedback: SubmissionFeedback) {
        submissionFeedbackDao.insert(feedback)
    }
}