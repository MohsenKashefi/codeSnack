package com.example.codesnack

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

object ThemePreferences {
    private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")

    fun getThemeFlow(context: Context): Flow<Boolean?> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_DARK_THEME]
        }
    }

    suspend fun setTheme(context: Context, isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDark
        }
    }

    suspend fun clearTheme(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(IS_DARK_THEME)
        }
    }
}