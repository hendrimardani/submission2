package com.example.mysubmission2.ui.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ButtonDataStoreStateViewModel(private val dataStore: DataStore<Preferences>) {
    private val BUTTON_STATE_KEY = booleanPreferencesKey("button_state")

    fun getButtonState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
        preferences[BUTTON_STATE_KEY] ?: false
        }
    }

    suspend fun setButtonState(isFilledImage: Boolean) {
        dataStore.edit { preferences ->
            preferences[BUTTON_STATE_KEY] = isFilledImage
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ButtonDataStoreStateViewModel? = null

        fun getInstance(dataStore: DataStore<Preferences>): ButtonDataStoreStateViewModel {
            return INSTANCE ?: synchronized(this) {
                val instance = ButtonDataStoreStateViewModel(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
