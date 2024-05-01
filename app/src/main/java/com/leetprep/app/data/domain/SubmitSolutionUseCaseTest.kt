package com.leetprep.app.data.domain;

import android.util.Log
import com.leetprep.app.data.ProblemStore
import com.leetprep.app.data.database.model.FeedbackItem
import com.leetprep.app.data.database.model.Submission
import com.leetprep.app.data.database.model.SubmissionFeedback
import com.leetprep.app.data.network.APIService
import kotlinx.coroutines.delay
import javax.inject.Inject


class SubmitSolutionUseCaseTest @Inject constructor(
    private val apiService: APIService,
    private val problemStore: ProblemStore
) {
     suspend operator fun invoke(submission: Submission, problemDesc: String): SubmissionFeedback {
         delay(200)
         val feedback = SubmissionFeedback(
            problemId = submission.problemId,
            feedbackItems = listOf(FeedbackItem(title = "Use HashMap", message = "store elements for O(1) retrieval")),
            correct = false
        )

         problemStore.updateSubmissionFeedback(feedback)
         return feedback
    }
}


