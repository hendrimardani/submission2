package com.example.mysubmission2.di

import android.content.Context
import com.example.mysubmission2.data.EventRepository
import com.example.mysubmission2.data.local.room.EventDatabase
import com.example.mysubmission2.data.remote.retrofit.ApiConfig
import com.example.mysubmission2.utils.AppExecutors

object Injection {

    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }
}