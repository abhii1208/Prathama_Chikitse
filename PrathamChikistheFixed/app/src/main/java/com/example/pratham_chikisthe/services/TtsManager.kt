package com.example.pratham_chikisthe.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TtsManager(
    private val context: Context,
    private val onInitResult: (Boolean) -> Unit
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    var isInitialized: Boolean = false
        private set

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.language = Locale.US
            onInitResult(true)
        } else {
            isInitialized = false
            onInitResult(false)
            Log.e("TtsManager", "TTS initialization failed with status $status")
        }
    }

    fun speak(text: String, langCode: String, onUnavailable: () -> Unit = {}) {
        if (!isInitialized || tts == null) {
            onUnavailable()
            return
        }

        val locale = getLocaleForLanguage(langCode)
        val availability = tts?.isLanguageAvailable(locale)

        if (availability == TextToSpeech.LANG_AVAILABLE || availability == TextToSpeech.LANG_COUNTRY_AVAILABLE || availability == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE) {
            tts?.language = locale
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "AID_ASSIST_TTS")
        } else {
            // Attempt speech in English as fallback or invoke callback
            val enAvail = tts?.isLanguageAvailable(Locale.US)
            if (enAvail == TextToSpeech.LANG_AVAILABLE) {
                tts?.language = Locale.US
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "AID_ASSIST_TTS")
            } else {
                onUnavailable()
            }
        }
    }

    fun stop() {
        if (isInitialized) {
            tts?.stop()
        }
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }

    private fun getLocaleForLanguage(code: String): Locale {
        return when (code) {
            "hi" -> Locale("hi", "IN")
            "kn" -> Locale("kn", "IN")
            "bn" -> Locale("bn", "IN")
            "te" -> Locale("te", "IN")
            "mr" -> Locale("mr", "IN")
            "ta" -> Locale("ta", "IN")
            "gu" -> Locale("gu", "IN")
            "ml" -> Locale("ml", "IN")
            "ur" -> Locale("ur", "PK")
            else -> Locale.US
        }
    }
}

