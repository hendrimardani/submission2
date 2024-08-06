package com.example.mysubmission2.ui.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ButtonViewModel(private val pref: ButtonDataStoreStateViewModel) : ViewModel() {

    fun getButtonState(): LiveData<Boolean> {
        return pref.getButtonState().asLiveData()
    }

    fun setButtonState(isButtonFilled: Boolean) {
        viewModelScope.launch {
            pref.setButtonState(isButtonFilled)
        }
    }
}