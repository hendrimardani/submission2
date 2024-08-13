package com.example.mysubmission2.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val themekey = booleanPreferencesKey("theme_setting")
    private val noitifcationkey = booleanPreferencesKey("notification_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map {  preferences ->
            preferences[themekey] ?: false
        }
    }

    suspend fun saveThemeSetting(isNotificationActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themekey] = isNotificationActive
        }
    }
    fun getNotificationSetting(): Flow<Boolean> {
        return dataStore.data.map {  preferences ->
            preferences[noitifcationkey] ?: false
        }
    }

    suspend fun saveNotificationSetting(isNotificationActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[noitifcationkey] = isNotificationActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}