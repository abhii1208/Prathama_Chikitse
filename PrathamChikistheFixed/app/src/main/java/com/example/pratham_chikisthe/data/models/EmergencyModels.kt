package com.example.pratham_chikisthe.data.models

enum class EmergencySeverity {
    CRITICAL,     // 🔴 Call 112 Now
    URGENT,       // 🟠 Seek Prompt Medical Attention
    NON_CRITICAL  // 🟢 Basic First Aid & Monitoring
}

enum class EmergencyCategory {
    LIFE_THREATENING,   // ❤️ Life Threatening
    BLEEDING_WOUNDS,    // 🩸 Bleeding & Wounds
    BURNS,              // 🔥 Burns & Scalds
    FRACTURES,          // 🦴 Fractures & Joints
    HEAD_FACE,          // 👁️ Head, Face & Eye
    BREATHING,          // 🫁 Breathing & Airway
    HEART,              // ❤️ Heart & Stroke
    NEUROLOGICAL,       // 🧠 Brain & Seizures
    ALLERGIES,          // 🌾 Allergies & Anaphylaxis
    POISONING,          // ☠️ Poisoning & Fumes
    HEAT_COLD,          // 🌡️ Heat & Hypothermia
    WATER,              // 🌊 Water Emergencies
    ELECTRICAL,         // ⚡ Electrical & Lightning
    FIRE_SMOKE,         // 🔥 Fire & Smoke
    BITES_STINGS,       // 🐍 Bites & Stings
    DIGESTIVE,          // 🤢 Food & Stomach
    DIABETIC,           // 🩸 Diabetic Emergencies
    BEHAVIORAL,         // 🧠 Mental & Behavioral
    PREGNANCY,          // 🤰 Pregnancy & Birth
    PEDIATRIC,          // 👶 Infant & Child
    ELDERLY,            // 🧓 Elderly & Falls
    ROAD_ACCIDENTS,     // 🚗 Road Accidents & Mass Casualty
    WORKPLACE_OUTDOOR   // 🏭 Workplace & Outdoor
}

data class EmergencyStep(
    val stepNumber: Int,
    val titleMap: Map<String, String> = emptyMap(),
    val instructionMap: Map<String, String>,
    val descriptionMap: Map<String, String>,
    val illustrationType: String? = null,
    val isDo: Boolean? = null
    val isDo: Boolean? = null,
    val isCritical: Boolean = false,
    val optionalTimerSeconds: Int? = null
) {
    fun getTitle(lang: String): String = titleMap[lang] ?: titleMap["en"].orEmpty()
    fun getInstruction(lang: String): String = instructionMap[lang] ?: instructionMap["en"].orEmpty()
    fun getDescription(lang: String): String = descriptionMap[lang] ?: descriptionMap["en"].orEmpty()
}

data class Emergency(
    val id: String,
    val titleMap: Map<String, String>,
    val category: EmergencyCategory,
    val severity: EmergencySeverity,
    val iconName: String,
    val shortDescriptionMap: Map<String, String> = emptyMap(),
    val immediateActionMap: Map<String, String>,
    val recognitionSignsMap: Map<String, List<String>> = emptyMap(),
    val steps: List<EmergencyStep>,
    val doListMap: Map<String, List<String>>,
    val dontListMap: Map<String, List<String>>,
    val escalationMap: Map<String, String>,
    val warningSignsMap: Map<String, List<String>> = emptyMap(),
    val escalationMap: Map<String, String> = emptyMap(),
    val whenToCall112Map: Map<String, String> = emptyMap(),
    val recoveryGuidanceMap: Map<String, String> = emptyMap(),
    val audioTextMap: Map<String, String> = emptyMap(),
    val keywordsMap: Map<String, List<String>>,
    val relatedEmergencyIds: List<String> = emptyList(),
    val warningSignsMap: Map<String, List<String>> = emptyMap()
    val relatedEmergencyIds: List<String> = emptyList()
) {
    fun getTitle(lang: String): String = titleMap[lang] ?: titleMap["en"].orEmpty()
    fun getShortDescription(lang: String): String = shortDescriptionMap[lang] ?: shortDescriptionMap["en"] ?: getImmediateAction(lang)
    fun getImmediateAction(lang: String): String = immediateActionMap[lang] ?: immediateActionMap["en"].orEmpty()
    fun getRecognitionSigns(lang: String): List<String> = recognitionSignsMap[lang] ?: recognitionSignsMap["en"] ?: emptyList()
    fun getDoList(lang: String): List<String> = doListMap[lang] ?: doListMap["en"] ?: emptyList()
    fun getDontList(lang: String): List<String> = dontListMap[lang] ?: dontListMap["en"] ?: emptyList()
    fun getWarningSigns(lang: String): List<String> = warningSignsMap[lang] ?: warningSignsMap["en"] ?: emptyList()
    fun getEscalation(lang: String): String = escalationMap[lang] ?: escalationMap["en"].orEmpty()
    fun getWhenToCall112(lang: String): String = whenToCall112Map[lang] ?: whenToCall112Map["en"] ?: "Call 112 immediately if condition deteriorates or loss of consciousness occurs."
    fun getRecoveryGuidance(lang: String): String = recoveryGuidanceMap[lang] ?: recoveryGuidanceMap["en"].orEmpty()
    fun getAudioText(lang: String): String = audioTextMap[lang] ?: audioTextMap["en"] ?: "${getTitle(lang)}. ${getImmediateAction(lang)}"
    fun getKeywords(lang: String): List<String> = keywordsMap[lang] ?: keywordsMap["en"] ?: emptyList()
    fun getWarningSigns(lang: String): List<String> = warningSignsMap[lang] ?: warningSignsMap["en"] ?: emptyList()

    fun matchesQuery(query: String, lang: String): Boolean {
        val q = query.trim().lowercase()
        if (q.isBlank()) return true
        val title = getTitle(lang).lowercase()
        if (title.contains(q)) return true
        val keywords = getKeywords(lang).map { it.lowercase() }
        if (keywords.any { it.contains(q) }) return true
        // Fallback cross-search in English
        val enTitle = getTitle("en").lowercase()
        if (enTitle.contains(q)) return true
        return getKeywords("en").any { it.lowercase().contains(q) }
    }
}

data class Hospital(
    val nameMap: Map<String, String>,
    val lat: Double,
    val lng: Double,
    val contact: String,
    val addressMap: Map<String, String>
) {
    fun getName(lang: String): String = nameMap[lang] ?: nameMap["en"].orEmpty()
    fun getAddress(lang: String): String = addressMap[lang] ?: addressMap["en"].orEmpty()
}

data class KitItem(
    val id: String,
    val nameMap: Map<String, String>,
    val categoryMap: Map<String, String>
) {
    fun getName(lang: String): String = nameMap[lang] ?: nameMap["en"].orEmpty()
    fun getCategory(lang: String): String = categoryMap[lang] ?: categoryMap["en"].orEmpty()
}

data class EmergencyContact(
    val name: String,
    val phone: String
)
