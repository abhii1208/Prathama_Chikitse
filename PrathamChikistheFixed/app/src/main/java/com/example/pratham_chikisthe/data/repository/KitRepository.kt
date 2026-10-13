package com.example.pratham_chikisthe.data.repository

import com.example.pratham_chikisthe.data.local.EmergencyDataset
import com.example.pratham_chikisthe.data.models.KitItem

class KitRepository {

    fun getKitItems(): List<KitItem> = EmergencyDataset.kitItems

    fun calculateProgress(checkedItemIds: Set<String>): Float {
        val total = EmergencyDataset.kitItems.size
        if (total == 0) return 0f
        return (checkedItemIds.size.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    }
}

