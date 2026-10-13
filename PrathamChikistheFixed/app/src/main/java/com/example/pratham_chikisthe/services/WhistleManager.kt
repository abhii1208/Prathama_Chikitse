package com.example.pratham_chikisthe.services

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.ToneGenerator
import android.media.AudioTrack
import android.util.Log
import kotlin.concurrent.thread
import kotlin.math.sin

enum class WhistleMode {
    EMERGENCY_WHISTLE, // 3 short bursts + pause
    SOS_PATTERN        // ... --- ...
}

class WhistleManager {

    private var toneGenerator: ToneGenerator? = null
    private var audioTrack: AudioTrack? = null
    private var playbackThread: Thread? = null

    @Volatile
    var isPlaying: Boolean = false
        private set

    fun toggleWhistle(onStateChange: (Boolean) -> Unit) {
    var currentMode: WhistleMode = WhistleMode.EMERGENCY_WHISTLE

    fun checkVolume(context: Context): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return true
        val currentVol = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)
        val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
        return (currentVol.toFloat() / maxVol.toFloat()) >= 0.3f
    }

    fun toggleWhistle(context: Context, mode: WhistleMode = WhistleMode.EMERGENCY_WHISTLE, onStateChange: (Boolean) -> Unit) {
        if (isPlaying) {
            stopWhistle()
            onStateChange(false)
        } else {
            startWhistle()
            startWhistle(mode)
            onStateChange(true)
        }
    }

    fun startWhistle() {
        try {
            if (toneGenerator == null) {
                toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)
    fun startWhistle(mode: WhistleMode = WhistleMode.EMERGENCY_WHISTLE) {
        if (isPlaying) return
        currentMode = mode
        isPlaying = true

        playbackThread = thread(start = true, name = "WhistleAudioThread") {
            try {
                val sampleRate = 44100
                val minBufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val bufferSize = minBufferSize.coerceAtLeast( sampleRate * 2 )

                audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack?.play()

                while (isPlaying) {
                    when (currentMode) {
                        WhistleMode.EMERGENCY_WHISTLE -> playEmergencyWhistlePattern(sampleRate)
                        WhistleMode.SOS_PATTERN -> playSosPattern(sampleRate)
                    }
                }
            } catch (e: Exception) {
                Log.e("WhistleManager", "AudioTrack playback error: ${e.message}")
            } finally {
                cleanupAudioTrack()
            }
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 10000)
            isPlaying = true
        } catch (e: Exception) {
            Log.e("WhistleManager", "Failed to play emergency whistle tone: ${e.message}")
            isPlaying = false
        }
    }

    private fun playEmergencyWhistlePattern(sampleRate: Int) {
        // 3 short bursts (~200ms burst, ~100ms gap) + 600ms pause
        for (i in 0 until 3) {
            if (!isPlaying) break
            playWhistleTone(sampleRate, durationMs = 180, startFreq = 2800.0, endFreq = 3200.0)
            playSilence(sampleRate, durationMs = 100)
        }
        if (isPlaying) {
            playSilence(sampleRate, durationMs = 600)
        }
    }

    private fun playSosPattern(sampleRate: Int) {
        // ... --- ... (3 short, 3 long, 3 short)
        val shortDuration = 150
        val longDuration = 450
        val gap = 120

        // 3 Short
        for (i in 0 until 3) {
            if (!isPlaying) break
            playWhistleTone(sampleRate, durationMs = shortDuration, startFreq = 3000.0, endFreq = 3000.0)
            playSilence(sampleRate, durationMs = gap)
        }
        playSilence(sampleRate, durationMs = 250)

        // 3 Long
        for (i in 0 until 3) {
            if (!isPlaying) break
            playWhistleTone(sampleRate, durationMs = longDuration, startFreq = 3000.0, endFreq = 3000.0)
            playSilence(sampleRate, durationMs = gap)
        }
        playSilence(sampleRate, durationMs = 250)

        // 3 Short
        for (i in 0 until 3) {
            if (!isPlaying) break
            playWhistleTone(sampleRate, durationMs = shortDuration, startFreq = 3000.0, endFreq = 3000.0)
            playSilence(sampleRate, durationMs = gap)
        }

        if (isPlaying) {
            playSilence(sampleRate, durationMs = 1000)
        }
    }

    private fun playWhistleTone(sampleRate: Int, durationMs: Int, startFreq: Double, endFreq: Double) {
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val freq = startFreq + (endFreq - startFreq) * progress
            val angle = 2.0 * Math.PI * i.toDouble() * freq / sampleRate
            val envelope = when {
                i < 200 -> i / 200.0
                i > numSamples - 200 -> (numSamples - i) / 200.0
                else -> 1.0
            }
            val sample = (sin(angle) * Short.MAX_VALUE * 0.95 * envelope).toInt()
            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        audioTrack?.write(buffer, 0, buffer.size)
    }

    private fun playSilence(sampleRate: Int, durationMs: Int) {
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        audioTrack?.write(buffer, 0, buffer.size)
    }

    fun stopWhistle() {
        isPlaying = false
        playbackThread?.interrupt()
        playbackThread = null
        cleanupAudioTrack()
    }

    private fun cleanupAudioTrack() {
        try {
            toneGenerator?.stopTone()
            toneGenerator?.release()
            toneGenerator = null
            isPlaying = false
            audioTrack?.apply {
                if (playState == AudioTrack.PLAYSTATE_PLAYING) {
                    stop()
                }
                release()
            }
        } catch (e: Exception) {
            Log.e("WhistleManager", "Failed to stop whistle tone: ${e.message}")
            Log.e("WhistleManager", "Error cleaning AudioTrack: ${e.message}")
        } finally {
            audioTrack = null
        }
    }
}

