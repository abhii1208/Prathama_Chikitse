package com.example.pratham_chikisthe.data.models

data class Language(
    val code: String,
    val nameEn: String,
    val nativeName: String,
    val isRtl: Boolean = false
)

val supportedLanguages = listOf(
    Language("en", "English", "English"),
    Language("hi", "Hindi", "हिन्दी"),
    Language("kn", "Kannada", "ಕನ್ನಡ"),
    Language("bn", "Bengali", "বাংলা"),
    Language("te", "Telugu", "తెలుగు"),
    Language("mr", "Marathi", "मराठी"),
    Language("ta", "Tamil", "தமிழ்"),
    Language("gu", "Gujarati", "ગુજરાતી"),
    Language("ml", "Malayalam", "മലയാളം"),
    Language("ur", "Urdu", "اردو", isRtl = true)
)

