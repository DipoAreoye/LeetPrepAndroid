package com.leetprep.app.data.di

import android.content.Context
import androidx.room.Room
import com.leetprep.app.data.LocalProblemStore
import com.leetprep.app.data.ProblemStore
import com.leetprep.app.data.database.LeetPrepDatabase
import com.leetprep.app.data.database.dao.ProblemDao
import com.leetprep.app.data.database.dao.SubmissionDao
import com.leetprep.app.data.network.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): LeetPrepDatabase =
        Room.databaseBuilder(context, LeetPrepDatabase::class.java, "data.db")
            // This is not recommended for normal apps, but the goal of this sample isn't to
            // showcase all of Room.
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideProblemsDao(
        database: LeetPrepDatabase
    ): ProblemDao = database.problemsDao()

    @Provides
    @Singleton
    fun provideSubmissionsDao(
        database: LeetPrepDatabase
    ): SubmissionDao = database.submissionDao()

    @Provides
    @Singleton
    fun provideProblemsStore(
        problemDao: ProblemDao,
        submissionDao: SubmissionDao
    ): ProblemStore = LocalProblemStore(problemDao, submissionDao)

    @Provides
    @Singleton
    fun provideApiService() : APIService = APIService()
}