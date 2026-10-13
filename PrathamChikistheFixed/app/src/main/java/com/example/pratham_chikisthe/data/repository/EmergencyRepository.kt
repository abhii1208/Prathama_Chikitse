package com.example.pratham_chikisthe.data.repository

import com.example.pratham_chikisthe.data.local.EmergencyDataset
import com.example.pratham_chikisthe.data.models.Emergency
import com.example.pratham_chikisthe.data.models.EmergencyCategory

class EmergencyRepository {

    fun getEmergencies(): List<Emergency> = EmergencyDataset.emergencies

    fun getEmergencyById(id: String): Emergency? {
        return EmergencyDataset.emergencies.firstOrNull { it.id.equals(id, ignoreCase = true) }
    }

    fun getEmergenciesByCategory(category: EmergencyCategory): List<Emergency> {
        return EmergencyDataset.emergencies.filter { it.category == category }
    }

    fun searchEmergencies(query: String, lang: String): List<Emergency> {
        if (query.isBlank()) return getEmergencies()
        return EmergencyDataset.emergencies.filter { it.matchesQuery(query, lang) }
    }
}

