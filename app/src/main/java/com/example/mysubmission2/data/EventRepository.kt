package com.example.mysubmission2.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.example.mysubmission2.data.local.entity.EventEntity
import com.example.mysubmission2.data.local.room.EventDao
import com.example.mysubmission2.data.remote.response.Detail
import com.example.mysubmission2.data.remote.response.DetailResponse
import com.example.mysubmission2.data.remote.response.FinishedResponse
import com.example.mysubmission2.data.remote.response.SearchResponse
import com.example.mysubmission2.data.remote.response.UpcomingResponse
import com.example.mysubmission2.data.remote.retrofit.ApiService
import com.example.mysubmission2.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<EventEntity>>>()


    fun isEventFavorite(id: String): LiveData<Boolean> {
        return eventDao.isEventFavorite(id)
    }

    fun updateFavoriteEvent(id: String, favoriteState: Boolean) {
        appExecutors.diskIO.execute {
            eventDao.updateFavoriteEvent(id, favoriteState)
        }
    }

    fun getFavorite(): LiveData<List<EventEntity>> {
        return eventDao.getFavorite()
    }

    fun getUpComing(): LiveData<Result<List<EventEntity>>> {
        result.value = Result.Loading
        val client = apiService.getUpcoming()
        client.enqueue(object : Callback<UpcomingResponse> {
            override fun onResponse(call: Call<UpcomingResponse>, response: Response<UpcomingResponse>) {
                if (response.isSuccessful) {
                    val listComingItem = response.body()?.listUpcomingItem
                    val eventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        listComingItem?.forEach { item ->
                            val event = EventEntity(
                                item.id.toString(),
                                item.name,
                                item.summary,
                                item.description,
                                item.mediaCover,
                                item.quota.toString(),
                                item.beginTime,
                                false
                            )
                            eventList.add(event)
                        }
                        eventDao.deleteAll()
                        eventDao.insertUpcoming(eventList)
                    }
                }
            }

            override fun onFailure(call: Call<UpcomingResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = eventDao.getEventUpComing()
        result.addSource(localData) { eventData: List<EventEntity> ->
            result.value = Result.Success(eventData)
        }

        return result
    }

    fun getFinished(): LiveData<Result<List<EventEntity>>> {
        result.value = Result.Loading
        val client = apiService.getFinished()
        client.enqueue(object : Callback<FinishedResponse> {
            override fun onResponse(call: Call<FinishedResponse>, response: Response<FinishedResponse>) {
                if (response.isSuccessful) {
                    val listFinishedItem = response.body()?.listFinishedItem
                    val eventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        listFinishedItem?.forEach { item ->
                            val event = EventEntity(
                                item.id.toString(),
                                item.name,
                                item.summary,
                                item.description,
                                item.mediaCover,
                                item.quota.toString(),
                                item.beginTime,
                                false
                            )
                            eventList.add(event)
                        }
                        eventDao.deleteAll()
                        eventDao.insertFinished(eventList)
                    }
                }
            }

            override fun onFailure(call: Call<FinishedResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = eventDao.getEventFinished()
        result.addSource(localData) { eventData: List<EventEntity> ->
            result.value = Result.Success(eventData)
        }
        return result
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}