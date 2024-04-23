package com.leetprep.app.data

import com.leetprep.app.data.database.dao.ProblemDao
import com.leetprep.app.data.database.dao.SubmissionDao
import com.leetprep.app.data.database.dao.SubmissionDao_Impl
import com.leetprep.app.data.database.dao.SubmissionFeedbackDao
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.data.database.model.Submission
import kotlinx.coroutines.flow.Flow

interface ProblemStore {
    fun problemWithId(id: Long): Flow<Problem>
    fun submissionWithProblemId(id: Long): Flow<Submission>
    fun problemsSortedByDifficulty(): Flow<List<Problem>>
    suspend fun addProblems(problems: List<Problem>)
}

class LocalProblemStore (
    private val problemDao: ProblemDao,
    private val submissionDao: SubmissionDao,
): ProblemStore {
    override fun problemWithId(id: Long): Flow<Problem> {
        return problemDao.problemWithId(id)
    }
    override fun submissionWithProblemId(id: Long): Flow<Submission> {
        return submissionDao.submissionWithProblemId(id)
    }
    override fun problemsSortedByDifficulty(): Flow<List<Problem>> {
        return problemDao.getAll()
    }
    override suspend fun addProblems(problems: List<Problem>) {
        problemDao.insertAll(problems);
    }
}