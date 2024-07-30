package com.example.mysubmission2.ui

import androidx.lifecycle.ViewModel
import com.example.mysubmission2.data.EventRepository

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getListEvents() = eventRepository.getListEvents()


}