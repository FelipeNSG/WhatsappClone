package com.example.whatsappclone.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreSingleton {
    private var dataStore: DataStoreManager? = null

    fun getInstance(context: Context): DataStoreManager {
        if (dataStore == null) {
            dataStore = DataStoreManager(context)
        }
        return dataStore!!
    }

}

class DataStoreManager(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeBoarding")
    private val dataStore = context.dataStore

    companion object {
        val PASS_TO_HOME_SCREEN = booleanPreferencesKey("store_permission")
        val USER_ACCOUNT = stringPreferencesKey("store_loginUser")
    }

    suspend fun setIsEnableToPassToHomeScreen(isEnable: Boolean) {
        dataStore.edit { pref ->
            pref[PASS_TO_HOME_SCREEN] = isEnable
        }

    }

    fun getIsEnableToPassToHomeScreen(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PASS_TO_HOME_SCREEN] ?: true
    }


    suspend fun setUser(value: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ACCOUNT] = value
        }
    }

    fun getUser(): Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_ACCOUNT] ?: ""
    }


}