package com.leetprep.app.data.domain

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.leetprep.app.data.database.model.FeedbackItem
import com.leetprep.app.data.database.model.Submission
import com.leetprep.app.data.database.model.SubmissionFeedback
import com.leetprep.app.data.network.APIService
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

const val SYSTEM_MESSAGE = "You are an expert software engineer interview assistant. your role is to give feedback on candidates answers to leetcode style programming questions. \n" +
        "\n" +
        "The users will be using a mobile app to submit there solutions, either just with natural language/pseudo code or code. \n" +
        "\n" +
        "your goal is to give concise feedback on how they can give a better solution in regards to time and space complexity. \n" +
        "\n" +
        "your output will be in json\n" +
        "\n" +
        "{ \"correct\": true/false, \"feedback_items\": [{\n" +
        "title: string,\n" +
        "message: string\n" +
        "}] }. \n" +
        "Each item in the improvementNotes array should only include an improvement suggestion and use minimal words,  make sure the improvements are ways for them to reduce space or time complexity, i.e exploring a different data structure/algorithim, if the candidate doesn't provide code just analyse their approach and don't mention code specific  topics such as variable names, try to sumarise the improvement suggestion in  the title and make sure the desc is max 400 characters\n" +
        "\n" +
        "Each message sent to you should included the programming question and the candidates solution"


class SubmitSolutionUseCase @Inject constructor(
    private val apiService: APIService
) {
    suspend operator fun invoke(submission: Submission, problemDesc: String) : SubmissionFeedback {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = SYSTEM_MESSAGE
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = "Problem: {$problemDesc}, Users Solution:{${submission.text}}"
                )
            )
        )


        val completion: ChatCompletion =
            apiService.openai.chatCompletion(chatCompletionRequest)

        val choice = completion.choices.firstOrNull()
        val content = choice?.message
            ?: throw IllegalStateException("Expected content, but found null")
            // Get the content, safely handling nulls

        content.content?.let { Log.d("json", it) }

        val response = content.content?.let { JSONObject(it) }  // Convert to JSONObject
        return SubmissionFeedback(
            problemId = submission.problemId,
            correct = response!!.getBoolean("correct"),
            feedbackItems = feedbackJSONtoList(response.getJSONArray("feedback_items"))
        )
    }
    private fun feedbackJSONtoList(items: JSONArray) : List<FeedbackItem> {
        val output = mutableListOf<FeedbackItem>()

        for (i in 0 until items.length()) {
            val title = items.getJSONObject(i).getString("title")
            val message = items.getJSONObject(i).getString("message")
            output.add(FeedbackItem(title, message))
        }

        return output
    }
}