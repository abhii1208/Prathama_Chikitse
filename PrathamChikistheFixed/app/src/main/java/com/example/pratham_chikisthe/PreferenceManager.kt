package com.example.pratham_chikisthe

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.prathamDataStore by preferencesDataStore(name = "pratham_chikisthe_preferences")

class PreferenceManager(private val context: Context) {
    private val nameKey = stringPreferencesKey("name")
    private val bloodGroupKey = stringPreferencesKey("blood_group")
    private val emergencyContactKey = stringPreferencesKey("emergency_contact")
    private val emergencyContactsKey = stringSetPreferencesKey("emergency_contacts")
    private val checkedItemsKey = stringSetPreferencesKey("checked_items")
    private val selectedLanguageKey = stringPreferencesKey("selected_language")
    private val isLanguageSetKey = booleanPreferencesKey("is_language_set")

    val getSelectedLanguage: Flow<String> = context.prathamDataStore.data.map { preferences ->
        preferences[selectedLanguageKey] ?: "en"
    }

    val isLanguageSet: Flow<Boolean> = context.prathamDataStore.data.map { preferences ->
        preferences[isLanguageSetKey] ?: false
    }

    val getName: Flow<String> = context.prathamDataStore.data.map { preferences ->
        preferences[nameKey].orEmpty()
    }

    val getBloodGroup: Flow<String> = context.prathamDataStore.data.map { preferences ->
        preferences[bloodGroupKey].orEmpty()
    }

    val getEmergencyContact: Flow<String> = context.prathamDataStore.data.map { preferences ->
        preferences[emergencyContactKey].orEmpty()
    }

    val getEmergencyContacts: Flow<Set<String>> = context.prathamDataStore.data.map { preferences ->
        preferences[emergencyContactsKey] ?: emptySet()
    }

    val getCheckedItems: Flow<Set<String>> = context.prathamDataStore.data.map { preferences ->
        preferences[checkedItemsKey] ?: emptySet()
    }

    suspend fun setSelectedLanguage(languageCode: String) {
        context.prathamDataStore.edit { preferences ->
            preferences[selectedLanguageKey] = languageCode
            preferences[isLanguageSetKey] = true
        }
    }

    suspend fun saveData(name: String, bloodGroup: String, emergencyContact: String) {
        context.prathamDataStore.edit { preferences ->
            preferences[nameKey] = name.trim()
            preferences[bloodGroupKey] = bloodGroup.trim().uppercase()
            preferences[emergencyContactKey] = emergencyContact.trim()
        }
    }

    suspend fun updateCheckedItems(items: Set<String>) {
        context.prathamDataStore.edit { preferences ->
            preferences[checkedItemsKey] = items
        }
    }

    suspend fun addEmergencyContact(contactName: String, contactPhone: String) {
        val cleanName = contactName.trim()
        val cleanPhone = contactPhone.trim()
        if (cleanName.isBlank() || cleanPhone.isBlank()) return

        context.prathamDataStore.edit { preferences ->
            val current = preferences[emergencyContactsKey] ?: emptySet()
            preferences[emergencyContactsKey] = current + "$cleanName|$cleanPhone"
        }
    }

    suspend fun removeEmergencyContact(contact: String) {
        context.prathamDataStore.edit { preferences ->
            val current = preferences[emergencyContactsKey] ?: emptySet()
            preferences[emergencyContactsKey] = current - contact
        }
    }
}
