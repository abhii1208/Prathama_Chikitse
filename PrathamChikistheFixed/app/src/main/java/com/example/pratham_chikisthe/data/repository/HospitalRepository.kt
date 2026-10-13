package com.example.pratham_chikisthe.data.repository

import com.example.pratham_chikisthe.data.local.EmergencyDataset
import com.example.pratham_chikisthe.data.models.Hospital
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class HospitalWithDistance(
    val hospital: Hospital,
    val distanceKm: Double?
)

class HospitalRepository {

    fun getHospitals(userLat: Double? = null, userLng: Double? = null): List<HospitalWithDistance> {
        val list = EmergencyDataset.sampleHospitals
        if (userLat == null || userLng == null) {
            return list.map { HospitalWithDistance(it, null) }
        }

        return list.map { hospital ->
            val dist = calculateHaversineDistance(userLat, userLng, hospital.lat, hospital.lng)
            HospitalWithDistance(hospital, dist)
        }.sortedBy { it.distanceKm ?: Double.MAX_VALUE }
    }

    private fun calculateHaversineDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val r = 6371.0 // Earth radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}

