package com.leetprep.app.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Problem(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "difficulty") val difficulty: Int

) {
    fun getDifficultyString(): String {
        return when (difficulty) {
            1 -> "Easy"
            2-> "Medium"
            3 -> "Hard"
            else -> ""
        }
    }
}
