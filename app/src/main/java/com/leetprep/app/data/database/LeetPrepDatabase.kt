package com.leetprep.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leetprep.app.data.database.dao.ProblemsDao
import com.leetprep.app.data.database.model.Problem

@Database(entities = [Problem::class], version = 2)
abstract class LeetPrepDatabase : RoomDatabase() {
    abstract fun problemsDao() : ProblemsDao
}