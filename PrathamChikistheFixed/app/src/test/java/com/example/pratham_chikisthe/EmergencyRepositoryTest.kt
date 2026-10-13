package com.example.pratham_chikisthe

import com.example.pratham_chikisthe.data.models.supportedLanguages
import com.example.pratham_chikisthe.data.repository.EmergencyRepository
import com.example.pratham_chikisthe.data.repository.HospitalRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EmergencyRepositoryTest {

    private val emergencyRepository = EmergencyRepository()
    private val hospitalRepository = HospitalRepository()

    @Test
    fun testGetEmergencies_NotEmpty() {
        val list = emergencyRepository.getEmergencies()
        assertTrue("Emergency dataset should not be empty", list.isNotEmpty())
    }

    @Test
    fun testAllEmergenciesHaveComplete10LanguageTranslations() {
        val list = emergencyRepository.getEmergencies()
        val langCodes = supportedLanguages.map { it.code }

        for (emergency in list) {
            for (lang in langCodes) {
                val title = emergency.getTitle(lang)
                assertTrue("Emergency '${emergency.id}' missing title for lang '$lang'", title.isNotBlank())

                val action = emergency.getImmediateAction(lang)
                assertTrue("Emergency '${emergency.id}' missing immediate action for lang '$lang'", action.isNotBlank())

                val doList = emergency.getDoList(lang)
                assertTrue("Emergency '${emergency.id}' missing DO list for lang '$lang'", doList.isNotEmpty())

                val dontList = emergency.getDontList(lang)
                assertTrue("Emergency '${emergency.id}' missing DON'T list for lang '$lang'", dontList.isNotEmpty())

                val escalation = emergency.getEscalation(lang)
                assertTrue("Emergency '${emergency.id}' missing escalation for lang '$lang'", escalation.isNotBlank())

                val keywords = emergency.getKeywords(lang)
                assertTrue("Emergency '${emergency.id}' missing keywords for lang '$lang'", keywords.isNotEmpty())

                for (step in emergency.steps) {
                    val stepInst = step.getInstruction(lang)
                    assertTrue("Emergency '${emergency.id}' step ${step.stepNumber} missing instruction for lang '$lang'", stepInst.isNotBlank())

                    val stepDesc = step.getDescription(lang)
                    assertTrue("Emergency '${emergency.id}' step ${step.stepNumber} missing description for lang '$lang'", stepDesc.isNotBlank())
                }
            }
        }
    }

    @Test
    fun testSearchEmergencies_English() {
        val results = emergencyRepository.searchEmergencies("cpr", "en")
        assertTrue("CPR search should return results", results.any { it.id == "cpr" })
    }

    @Test
    fun testSearchEmergencies_Hindi() {
        val results = emergencyRepository.searchEmergencies("खून", "hi")
        assertTrue("Hindi search for blood/bleeding should return bleeding emergency", results.any { it.id == "bleeding" })
    }

    @Test
    fun testSearchEmergencies_Kannada() {
        val results = emergencyRepository.searchEmergencies("ಉಸಿರಾಟ", "kn")
        assertTrue("Kannada search for breathing should return CPR", results.any { it.id == "cpr" })
    }

    @Test
    fun testHospitalDistanceCalculation() {
        val hospitalsWithDist = hospitalRepository.getHospitals(12.9716, 77.5946)
        assertTrue("Hospitals should have non-null distance when user coordinates provided", hospitalsWithDist.all { it.distanceKm != null })
        val closest = hospitalsWithDist.first()
        assertTrue("Closest distance should be positive", (closest.distanceKm ?: 0.0) > 0.0)
    }
}
