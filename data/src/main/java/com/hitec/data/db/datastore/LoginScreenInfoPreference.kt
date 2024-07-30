package com.hitec.data.db.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hitec.domain.model.LoginScreenInfo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LoginScreenInfoPreference @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveLoginScreenInfo(
        id: String,
        password: String,
        localSite: String,
        androidDeviceId: String,
        isSwitchOn: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[ID] = id
            preferences[PASSWORD] = password
            preferences[LOCAL_SITE] = localSite
            preferences[ANDROID_DEVICE_ID] = androidDeviceId
            preferences[IS_SWITCH_ON] = isSwitchOn
        }
    }

    suspend fun getLoginScreenInfo(): LoginScreenInfo {
        val preferences = dataStore.data.first()
        return LoginScreenInfo(
            id = preferences[ID] ?: "",
            password = preferences[PASSWORD] ?: "",
            localSite = preferences[LOCAL_SITE] ?: "",
            androidDeviceId = preferences[ANDROID_DEVICE_ID] ?: "",
            isSwitchOn = preferences[IS_SWITCH_ON] ?: false
        )
    }

    suspend fun clearLoginScreenInfo() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        val ID = stringPreferencesKey("id")
        val PASSWORD = stringPreferencesKey("password")
        val LOCAL_SITE = stringPreferencesKey("local_site")
        val ANDROID_DEVICE_ID = stringPreferencesKey("android_device_id")
        val IS_SWITCH_ON = booleanPreferencesKey("is_switch_on")
    }
}