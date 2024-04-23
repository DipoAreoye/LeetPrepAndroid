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
            entity = Submission::class,
            parentColumns = ["id"],
            childColumns = ["submission_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("submission_id", unique = true)
    ]
)
@Serializable
data class SubmissionFeedback(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "submission_id") val submissionId: Long,
    @ColumnInfo(name = "message") val message: String
)