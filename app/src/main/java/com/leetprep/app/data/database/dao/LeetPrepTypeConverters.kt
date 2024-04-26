package com.leetprep.app.data.database.dao

import androidx.room.TypeConverter
import com.leetprep.app.data.database.model.FeedbackItem
import org.json.JSONArray
import org.json.JSONObject

class LeetPrepTypeConverters {
    @TypeConverter
    fun stringToFeedbackItems(value: String): List<FeedbackItem> {
        val jsonArray = JSONArray(value)
        val feedbackItems = mutableListOf<FeedbackItem>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val title = jsonObject.getString("title")
            val description = jsonObject.getString("message")

            feedbackItems.add(FeedbackItem(title, description))
        }

        return feedbackItems
    }

    @TypeConverter
    fun feedbackItemsToString(items: List<FeedbackItem>): String {
        val jsonArray = JSONArray()
        items.forEach { item ->
            val jsonObject = JSONObject()
            jsonObject.put("title", item.title)
            jsonObject.put("message", item.message)
            jsonArray.put(jsonObject)
        }

        return jsonArray.toString()
    }
}
