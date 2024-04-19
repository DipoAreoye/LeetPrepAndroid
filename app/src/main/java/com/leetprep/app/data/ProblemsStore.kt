package com.leetprep.app.data

import com.leetprep.app.data.database.dao.ProblemsDao
import com.leetprep.app.data.database.model.Problem
import kotlinx.coroutines.flow.Flow

interface ProblemStore {
    fun problemWithId(id: String): Flow<Problem>
    fun problemsSortedByDifficulty(): Flow<List<Problem>>
    suspend fun addProblems(problems: List<Problem>)
}

class LocalProblemStore (
    private val problemsDao: ProblemsDao
): ProblemStore {

    override fun problemWithId(id: String): Flow<Problem> {
        return problemsDao.problemWithId(id)
    }

    override fun problemsSortedByDifficulty(): Flow<List<Problem>> {
        return problemsDao.getAll()
    }

    override suspend fun addProblems(problems: List<Problem>) {
        problemsDao.insertAll(problems);
    }
}