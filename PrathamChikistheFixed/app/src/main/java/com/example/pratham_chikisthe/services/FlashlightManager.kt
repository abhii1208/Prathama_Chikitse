package com.example.pratham_chikisthe.services

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Log

class FlashlightManager(private val context: Context) {

    private val cameraManager: CameraManager? =
        context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager

    fun isFlashAvailable(): Boolean {
        return try {
            val cameraIds = cameraManager?.cameraIdList ?: return false
            for (id in cameraIds) {
                val hasFlash = cameraManager.getCameraCharacteristics(id)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                if (hasFlash == true) return true
            }
            false
        } catch (e: Exception) {
            Log.e("FlashlightManager", "Error checking flash availability: ${e.message}")
            false
        }
    }

    fun setFlashlight(enabled: Boolean, onError: (String) -> Unit = {}): Boolean {
        return try {
            val cameraIds = cameraManager?.cameraIdList ?: run {
                onError("Camera service unavailable")
                return false
            }
            var success = false
            for (id in cameraIds) {
                val hasFlash = cameraManager.getCameraCharacteristics(id)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                if (hasFlash == true) {
                    cameraManager.setTorchMode(id, enabled)
                    success = true
                    break
                }
            }
            if (!success) onError("Flash hardware not found")
            success
        } catch (e: Exception) {
            Log.e("FlashlightManager", "Error setting torch mode: ${e.message}")
            onError(e.localizedMessage ?: "Flashlight error")
            false
        }
    }
}

