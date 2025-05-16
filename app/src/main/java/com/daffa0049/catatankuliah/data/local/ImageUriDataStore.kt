package com.daffa0049.catatankuliah.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class ImageUriDataStore(private val context: Context) {
    private val IMAGE_URI_KEY = stringPreferencesKey("image_uri")

    suspend fun saveImageUri(uri: String) {
        context.dataStore.edit { preferences ->
            preferences[IMAGE_URI_KEY] = uri
        }
    }

    suspend fun getImageUri(): String? {
        return context.dataStore.data
            .map { preferences -> preferences[IMAGE_URI_KEY] }
            .first()
    }
}
