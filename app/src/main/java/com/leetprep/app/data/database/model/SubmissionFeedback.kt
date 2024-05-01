package com.leetprep.app.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity(
    tableName = "submission_feedback",
    foreignKeys = [
        ForeignKey(
            entity = Problem::class,
            parentColumns = ["id"],
            childColumns = ["problem_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("problem_id", unique = true)
    ]
)
@Serializable
data class SubmissionFeedback(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "problem_id") val problemId: Long,
    @ColumnInfo(name = "feedback_items") val feedbackItems: List<FeedbackItem>,
    @ColumnInfo(name = "correct") val correct: Boolean
) {
    fun getSolutionRating() : SolutionRating {
        if (correct) {
            return if (feedbackItems.isEmpty()) {
                SolutionRating.CORRECT
            } else {
                SolutionRating.NEEDS_IMPROVEMENT
            }
        }
        return SolutionRating.INCORRECT
    }
}


@Serializable
data class FeedbackItem(
    val title : String,
    val message: String
)

enum class SolutionRating{
    CORRECT,NEEDS_IMPROVEMENT,INCORRECT
}
