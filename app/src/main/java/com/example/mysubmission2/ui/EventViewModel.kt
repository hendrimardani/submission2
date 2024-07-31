package com.example.mysubmission2.ui

import androidx.lifecycle.ViewModel
import com.example.mysubmission2.data.EventRepository
import com.example.mysubmission2.data.local.entity.EventEntity

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getUpComing() = eventRepository.getUpComing()

    fun getFinished() = eventRepository.getFinished()

    fun getBookmarkedEvent() = eventRepository.getBookmarkedEvent()

    fun saveEvent(event: EventEntity) {
        eventRepository.setBookmarkedEvent(event, true)
    }

    fun deleteEvent(event: EventEntity) {
        eventRepository.setBookmarkedEvent(event, false)
    }

}