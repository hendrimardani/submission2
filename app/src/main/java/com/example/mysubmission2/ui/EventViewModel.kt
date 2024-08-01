package com.example.mysubmission2.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysubmission2.data.EventRepository
import com.example.mysubmission2.data.Result
import com.example.mysubmission2.data.local.entity.EventEntity
import com.example.mysubmission2.data.remote.response.Detail
import com.example.mysubmission2.data.remote.response.DetailResponse
import com.example.mysubmission2.data.remote.retrofit.ApiConfig
import com.example.mysubmission2.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _detail = MutableLiveData<Detail>()
    val detail: LiveData<Detail> = _detail

    init {
        getDetail("123")
    }

    fun getDetail(id: String) {
        val client = ApiConfig.getApiService().getDetail(id)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    val detailItem = response.body()?.detail
                    _detail.value = response.body()?.detail
                    Log.e(EventRepository.TAG, detailItem.toString())
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e(EventRepository.TAG, "onFailure : ${t.message}")
            }
        })
    }

    fun getUpComing() = eventRepository.getUpComing()

    fun getFinished() = eventRepository.getFinished()

//    fun getDetail(id: String) = eventRepository.detail

    fun getBookmarkedEvent() = eventRepository.getBookmarkedEvent()

    fun saveEvent(event: EventEntity) {
        eventRepository.setBookmarkedEvent(event, true)
    }

    fun deleteEvent(event: EventEntity) {
        eventRepository.setBookmarkedEvent(event, false)
    }
}