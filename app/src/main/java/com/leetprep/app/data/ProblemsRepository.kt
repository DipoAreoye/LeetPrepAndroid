package com.leetprep.app.data

import com.leetprep.app.data.database.model.Submission
import com.leetprep.app.data.domain.SubmitSolutionUseCase
import com.leetprep.app.data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProblemsRepository @Inject constructor(
    private val problemStore: ProblemStore,
    private val submitSolutionUseCase: SubmitSolutionUseCase,
    private val apiService: APIService
) {
    suspend fun updateProblems() {
        val problems = apiService.getProblems()
        withContext(Dispatchers.IO) {
            problemStore.addProblems(problems)
        }
    }
    suspend fun submitSolution(submission: Submission) {
        problemStore.problemWithId(submission.problemId).collect{
            val feedback = submitSolutionUseCase(submission, it.desc)
            problemStore.updateSubmissionFeedback(feedback)
        };
    }
}