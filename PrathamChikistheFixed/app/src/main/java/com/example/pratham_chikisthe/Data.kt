package com.example.pratham_chikisthe

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Masks
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.ui.graphics.vector.ImageVector

data class Step(
    val instruction: String,
    val description: String,
    val instructionKn: String,
    val descriptionKn: String,
    val isDo: Boolean? = null
)

data class Emergency(
    val title: String,
    val titleKn: String,
    val icon: ImageVector,
    val steps: List<Step>
)

data class Hospital(
    val name: String,
    val nameKn: String,
    val lat: Double,
    val lng: Double,
    val contact: String,
    val address: String,
    val addressKn: String
)

data class KitItem(
    val name: String,
    val nameKn: String,
    val category: String,
    val categoryKn: String
)

val emergencyList = listOf(
    Emergency(
        title = "CPR",
        titleKn = "à²¸à²¿ à²ªà²¿ à²†à²°à³",
        icon = Icons.Default.Favorite,
        steps = listOf(
            Step(
                instruction = "Check response and breathing",
                description = "Tap the person and shout. If there is no normal breathing, call 108 immediately.",
                instructionKn = "à²ªà³à²°à²¤à²¿à²•à³à²°à²¿à²¯à³† à²®à²¤à³à²¤à³ à²‰à²¸à²¿à²°à²¾à²Ÿ à²ªà²°à²¿à²¶à³€à²²à²¿à²¸à²¿",
                descriptionKn = "à²µà³à²¯à²•à³à²¤à²¿à²¯à²¨à³à²¨à³ à²¸à³Œà²®à³à²¯à²µà²¾à²—à²¿ à²¤à²Ÿà³à²Ÿà²¿ à²•à²°à³†à²¯à²¿à²°à²¿. à²¸à²¾à²®à²¾à²¨à³à²¯ à²‰à²¸à²¿à²°à²¾à²Ÿ à²‡à²²à³à²²à²¦à²¿à²¦à³à²¦à²°à³† à²¤à²•à³à²·à²£ 108 à²—à³† à²•à²°à³† à²®à²¾à²¡à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Start chest compressions",
                description = "Place both hands in the center of the chest. Push hard and fast, about 100-120 compressions per minute.",
                instructionKn = "à²Žà²¦à³† à²’à²¤à³à²¤à³à²µà²¿à²•à³† à²ªà³à²°à²¾à²°à²‚à²­à²¿à²¸à²¿",
                descriptionKn = "à²Žà²°à²¡à³ à²•à³ˆà²—à²³à²¨à³à²¨à³ à²Žà²¦à³†à²¯ à²®à²§à³à²¯à²¦à²²à³à²²à²¿ à²‡à²¡à²¿. à²¨à²¿à²®à²¿à²·à²•à³à²•à³† à²¸à³à²®à²¾à²°à³ 100-120 à²¬à²¾à²°à²¿ à²¬à²²à²µà²¾à²—à²¿ à²®à²¤à³à²¤à³ à²µà³‡à²—à²µà²¾à²—à²¿ à²’à²¤à³à²¤à²¿à²°à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Continue until help arrives",
                description = "Do not stop unless the person breathes normally, an AED is ready, or medical help takes over.",
                instructionKn = "à²¸à²¹à²¾à²¯ à²¬à²°à³à²µà²µà²°à³†à²—à³† à²®à³à²‚à²¦à³à²µà²°à²¿à²¸à²¿",
                descriptionKn = "à²µà³à²¯à²•à³à²¤à²¿ à²¸à²¾à²®à²¾à²¨à³à²¯à²µà²¾à²—à²¿ à²‰à²¸à²¿à²°à²¾à²¡à³à²µà²µà²°à³†à²—à³†, à²Ž à²‡ à²¡à²¿ à²¸à²¿à²¦à³à²§à²µà²¾à²—à³à²µà²µà²°à³†à²—à³† à²…à²¥à²µà²¾ à²µà³ˆà²¦à³à²¯à²•à³€à²¯ à²¸à²¹à²¾à²¯ à²¬à²°à³à²µà²µà²°à³†à²—à³† à²¨à²¿à²²à³à²²à²¿à²¸à²¬à³‡à²¡à²¿.",
                isDo = true
            )
        )
    ),
    Emergency(
        title = "Bleeding",
        titleKn = "à²¹à³†à²šà³à²šà³ à²°à²•à³à²¤à²¸à³à²°à²¾à²µ",
        icon = Icons.Default.Opacity,
        steps = listOf(
            Step(
                instruction = "Apply firm pressure",
                description = "Use clean cloth or gauze and press directly on the wound for at least 10-20 minutes.",
                instructionKn = "à²¬à²²à²µà²¾à²—à²¿ à²’à²¤à³à²¤à²¡ à²¨à³€à²¡à²¿",
                descriptionKn = "à²¸à³à²µà²šà³à²› à²¬à²Ÿà³à²Ÿà³† à²…à²¥à²µà²¾ à²—à²¾à²œà³ à²¬à²³à²¸à²¿ à²—à²¾à²¯à²¦ à²®à³‡à²²à³† à²¨à³‡à²°à²µà²¾à²—à²¿ à²•à²¨à²¿à²·à³à²  10-20 à²¨à²¿à²®à²¿à²· à²’à²¤à³à²¤à²¿à²°à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Raise the injured part",
                description = "If possible, raise the bleeding limb above heart level while keeping pressure on the wound.",
                instructionKn = "à²—à²¾à²¯à²µà²¾à²¦ à²­à²¾à²—à²µà²¨à³à²¨à³ à²Žà²¤à³à²¤à²¿à²°à²¿",
                descriptionKn = "à²¸à²¾à²§à³à²¯à²µà²¾à²¦à²°à³†, à²—à²¾à²¯à²¦ à²®à³‡à²²à³† à²’à²¤à³à²¤à²¡ à²‡à²Ÿà³à²Ÿà³à²•à³Šà²‚à²¡à³‡ à²°à²•à³à²¤à²¸à³à²°à²¾à²µà²µà²¾à²—à³à²¤à³à²¤à²¿à²°à³à²µ à²…à²‚à²—à²µà²¨à³à²¨à³ à²¹à³ƒà²¦à²¯ à²®à²Ÿà³à²Ÿà²•à³à²•à²¿à²‚à²¤ à²®à³‡à²²à²•à³à²•à³† à²Žà²¤à³à²¤à²¿à²°à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Do not remove soaked cloth",
                description = "If blood soaks through, add another cloth on top and keep pressing. Call emergency help.",
                instructionKn = "à²¨à³†à²¨à³†à²¦ à²¬à²Ÿà³à²Ÿà³†à²¯à²¨à³à²¨à³ à²¤à³†à²—à³†à²¦à³à²¹à²¾à²•à²¬à³‡à²¡à²¿",
                descriptionKn = "à²°à²•à³à²¤ à²¬à²Ÿà³à²Ÿà³†à²¯à²²à³à²²à²¿ à²¨à³†à²¨à³†à²¦à²°à³†, à²…à²¦à²° à²®à³‡à²²à³† à²®à²¤à³à²¤à³Šà²‚à²¦à³ à²¬à²Ÿà³à²Ÿà³† à²‡à²Ÿà³à²Ÿà³ à²’à²¤à³à²¤à²¡ à²®à³à²‚à²¦à³à²µà²°à²¿à²¸à²¿. à²¤à³à²°à³à²¤à³ à²¸à²¹à²¾à²¯à²•à³à²•à³† à²•à²°à³† à²®à²¾à²¡à²¿.",
                isDo = false
            )
        )
    ),
    Emergency(
        title = "Burn",
        titleKn = "à²¸à³à²Ÿà³à²Ÿ à²—à²¾à²¯",
        icon = Icons.Default.Whatshot,
        steps = listOf(
            Step(
                instruction = "Cool the burn",
                description = "Hold the burn under cool running water for 20 minutes. Remove tight items near the burn.",
                instructionKn = "à²¸à³à²Ÿà³à²Ÿ à²­à²¾à²—à²µà²¨à³à²¨à³ à²¤à²£à³à²£à²—à²¾à²—à²¿à²¸à²¿",
                descriptionKn = "à²¸à³à²Ÿà³à²Ÿ à²­à²¾à²—à²µà²¨à³à²¨à³ 20 à²¨à²¿à²®à²¿à²· à²¤à²‚à²ªà²¾à²¦ à²¹à²°à²¿à²¯à³à²µ à²¨à³€à²°à²¿à²¨à²¡à²¿ à²‡à²¡à²¿. à²¸à³à²Ÿà³à²Ÿ à²­à²¾à²—à²¦ à²¹à²¤à³à²¤à²¿à²°à²¦ à²¬à²¿à²—à²¿à²¯à²¾à²¦ à²µà²¸à³à²¤à³à²—à²³à²¨à³à²¨à³ à²¤à³†à²—à³†à²¦à³à²¹à²¾à²•à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Cover loosely",
                description = "Cover with a sterile non-stick dressing or clean cloth. Keep the person warm.",
                instructionKn = "à²¸à²¡à²¿à²²à²µà²¾à²—à²¿ à²®à³à²šà³à²šà²¿à²°à²¿",
                descriptionKn = "à²¸à³à²Ÿà³†à²°à³ˆà²²à³ à²…à²‚à²Ÿà²¿à²•à³Šà²³à³à²³à²¦ à²¡à³à²°à³†à²¸à²¿à²‚à²—à³ à²…à²¥à²µà²¾ à²¸à³à²µà²šà³à²› à²¬à²Ÿà³à²Ÿà³†à²¯à²¿à²‚à²¦ à²®à³à²šà³à²šà²¿à²°à²¿. à²µà³à²¯à²•à³à²¤à²¿à²¯à²¨à³à²¨à³ à²¬à³†à²šà³à²šà²—à³† à²‡à²¡à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Do not apply creams or ice",
                description = "Do not use butter, toothpaste, oils, or ice. Seek care for large, deep, face, hand, foot, or genital burns.",
                instructionKn = "à²•à³à²°à³€à²®à³ à²…à²¥à²µà²¾ à²à²¸à³ à²¹à²¾à²•à²¬à³‡à²¡à²¿",
                descriptionKn = "à²¬à³†à²£à³à²£à³†, à²Ÿà³‚à²¤à³â€Œà²ªà³‡à²¸à³à²Ÿà³, à²Žà²£à³à²£à³† à²…à²¥à²µà²¾ à²à²¸à³ à²¬à²³à²¸à²¬à³‡à²¡à²¿. à²¦à³Šà²¡à³à²¡, à²†à²³à²µà²¾à²¦, à²®à³à²–, à²•à³ˆ, à²•à²¾à²²à³ à²…à²¥à²µà²¾ à²œà²¨à²¨à²¾à²‚à²—à²¦ à²¸à³à²Ÿà³à²Ÿ à²—à²¾à²¯à²•à³à²•à³† à²šà²¿à²•à²¿à²¤à³à²¸à³† à²ªà²¡à³†à²¯à²¿à²°à²¿.",
                isDo = false
            )
        )
    ),
    Emergency(
        title = "Choking",
        titleKn = "à²‰à²¸à²¿à²°à³à²—à²Ÿà³à²Ÿà²¿à²•à³†",
        icon = Icons.Default.Restaurant,
        steps = listOf(
            Step(
                instruction = "Ask if they can cough",
                description = "If the person can cough or speak, encourage coughing. Do not hit their back unnecessarily.",
                instructionKn = "à²•à³†à²®à³à²®à²²à³ à²¸à²¾à²§à³à²¯à²µà³‡ à²Žà²‚à²¦à³ à²•à³‡à²³à²¿",
                descriptionKn = "à²µà³à²¯à²•à³à²¤à²¿ à²•à³†à²®à³à²®à²²à³ à²…à²¥à²µà²¾ à²®à²¾à²¤à²¨à²¾à²¡à²²à³ à²¸à²¾à²§à³à²¯à²µà²¾à²¦à²°à³† à²•à³†à²®à³à²®à²²à³ à²ªà³à²°à³‹à²¤à³à²¸à²¾à²¹à²¿à²¸à²¿. à²…à²¨à²µà²¶à³à²¯à²•à²µà²¾à²—à²¿ à²¬à³†à²¨à³à²¨à²¿à²—à³† à²¹à³Šà²¡à³†à²¯à²¬à³‡à²¡à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Give back blows and abdominal thrusts",
                description = "For severe choking, give 5 back blows, then 5 abdominal thrusts. Repeat and call 108.",
                instructionKn = "à²¬à³†à²¨à³à²¨à²¿à²—à³† à²¹à³Šà²¡à³†à²¤ à²®à²¤à³à²¤à³ à²¹à³Šà²Ÿà³à²Ÿà³† à²’à²¤à³à²¤à³à²µà²¿à²•à³† à²¨à³€à²¡à²¿",
                descriptionKn = "à²¤à³€à²µà³à²° à²‰à²¸à²¿à²°à³à²—à²Ÿà³à²Ÿà²¿à²•à³†à²¯à²¾à²¦à²°à³† 5 à²¬à²¾à²°à²¿ à²¬à³†à²¨à³à²¨à²¿à²—à³† à²¹à³Šà²¡à³†à²¤ à²¨à³€à²¡à²¿, à²¨à²‚à²¤à²° 5 à²¬à²¾à²°à²¿ à²¹à³Šà²Ÿà³à²Ÿà³† à²’à²¤à³à²¤à³à²µà²¿à²•à³† à²¨à³€à²¡à²¿. à²®à²°à³à²•à²³à²¿à²¸à²¿ à²®à²¤à³à²¤à³ 108 à²—à³† à²•à²°à³† à²®à²¾à²¡à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Start CPR if unconscious",
                description = "If the person becomes unconscious, lower them safely and begin CPR. Check the mouth before breaths only if trained.",
                instructionKn = "à²ªà³à²°à²œà³à²žà³† à²¤à²ªà³à²ªà²¿à²¦à²°à³† à²¸à²¿ à²ªà²¿ à²†à²°à³ à²ªà³à²°à²¾à²°à²‚à²­à²¿à²¸à²¿",
                descriptionKn = "à²µà³à²¯à²•à³à²¤à²¿ à²ªà³à²°à²œà³à²žà³† à²¤à²ªà³à²ªà²¿à²¦à²°à³† à²¸à³à²°à²•à³à²·à²¿à²¤à²µà²¾à²—à²¿ à²®à²²à²—à²¿à²¸à²¿ à²¸à²¿ à²ªà²¿ à²†à²°à³ à²ªà³à²°à²¾à²°à²‚à²­à²¿à²¸à²¿. à²¤à²°à²¬à³‡à²¤à²¿ à²‡à²¦à³à²¦à²°à³† à²®à²¾à²¤à³à²° à²‰à²¸à²¿à²°à³ à²•à³Šà²¡à³à²µ à²®à³Šà²¦à²²à³ à²¬à²¾à²¯à²¿à²¯à²¨à³à²¨à³ à²ªà²°à²¿à²¶à³€à²²à²¿à²¸à²¿.",
                isDo = true
            )
        )
    ),
    Emergency(
        title = "Snake Bite",
        titleKn = "à²¹à²¾à²µà³ à²•à²šà³à²šà²¿à²¦à²°à³†",
        icon = Icons.Default.BugReport,
        steps = listOf(
            Step(
                instruction = "Keep still and call help",
                description = "Keep the person calm and still. Call emergency services and arrange transport to hospital quickly.",
                instructionKn = "à²šà²²à²¿à²¸à²¦à²‚à²¤à³† à²‡à²Ÿà³à²Ÿà³ à²¸à²¹à²¾à²¯à²•à³à²•à³† à²•à²°à³† à²®à²¾à²¡à²¿",
                descriptionKn = "à²µà³à²¯à²•à³à²¤à²¿à²¯à²¨à³à²¨à³ à²¶à²¾à²‚à²¤à²µà²¾à²—à²¿ à²®à²¤à³à²¤à³ à²šà²²à²¿à²¸à²¦à²‚à²¤à³† à²‡à²¡à²¿. à²¤à³à²°à³à²¤à³ à²¸à³‡à²µà³†à²—à³† à²•à²°à³† à²®à²¾à²¡à²¿ à²®à²¤à³à²¤à³ à²¬à³‡à²— à²†à²¸à³à²ªà²¤à³à²°à³† à²¤à²²à³à²ªà²¿à²¸à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Remove tight items",
                description = "Remove rings, bangles, watches, or tight clothing near the bite before swelling starts.",
                instructionKn = "à²¬à²¿à²—à²¿à²¯à²¾à²¦ à²µà²¸à³à²¤à³à²—à²³à²¨à³à²¨à³ à²¤à³†à²—à³†à²¦à³à²¹à²¾à²•à²¿",
                descriptionKn = "à²Šà²¤ à²¶à³à²°à³à²µà²¾à²—à³à²µ à²®à³Šà²¦à²²à³ à²•à²šà³à²šà²¿à²¦ à²­à²¾à²—à²¦ à²¹à²¤à³à²¤à²¿à²°à²¦ à²‰à²‚à²—à³à²°, à²¬à²³à³†, à²—à²¡à²¿à²¯à²¾à²° à²…à²¥à²µà²¾ à²¬à²¿à²—à²¿à²¯à²¾à²¦ à²¬à²Ÿà³à²Ÿà³†à²¯à²¨à³à²¨à³ à²¤à³†à²—à³†à²¦à³à²¹à²¾à²•à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Do not cut or suck the wound",
                description = "Do not use ice, electric shock, alcohol, or a tourniquet. Do not try to catch the snake.",
                instructionKn = "à²—à²¾à²¯à²µà²¨à³à²¨à³ à²•à²¤à³à²¤à²°à²¿à²¸à²¬à³‡à²¡à²¿ à²…à²¥à²µà²¾ à²¹à³€à²°à²¬à³‡à²¡à²¿",
                descriptionKn = "à²à²¸à³, à²µà²¿à²¦à³à²¯à³à²¤à³ à²¶à²¾à²•à³, à²®à²¦à³à²¯ à²…à²¥à²µà²¾ à²Ÿà³‚à²°à³à²¨à²¿à²•à³†à²Ÿà³ à²¬à²³à²¸à²¬à³‡à²¡à²¿. à²¹à²¾à²µà²¨à³à²¨à³ à²¹à²¿à²¡à²¿à²¯à²²à³ à²ªà³à²°à²¯à²¤à³à²¨à²¿à²¸à²¬à³‡à²¡à²¿.",
                isDo = false
            )
        )
    ),
    Emergency(
        title = "Fracture",
        titleKn = "à²®à³‚à²³à³† à²®à³à²°à²¿à²¤ à²…à²¥à²µà²¾ à²®à²£à²¿à²•à²Ÿà³à²Ÿà³",
        icon = Icons.Default.Healing,
        steps = listOf(
            Step(
                instruction = "Immobilize the injured part",
                description = "Keep the limb in the same position found and support it with a splint or folded cloth. Do not force the bone back into place.",
                instructionKn = "à²šà²²à²¨à³† à²¨à²¿à²²à³à²²à²¿à²¸à²¿",
                descriptionKn = "à²—à²¾à²¯à²µà²¾à²¦ à²­à²¾à²—à²µà²¨à³à²¨à³ à²‡à²°à³à²µ à²¸à³à²¥à²¿à²¤à²¿à²¯à²²à³à²²à³‡ à²¬à³†à²‚à²¬à²²à²¿à²¸à²¿. à²¬à²²à²µà²‚à²¤à²µà²¾à²—à²¿ à²¨à³‡à²°à²—à³Šà²³à²¿à²¸à²¬à³‡à²¡à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Control swelling and bleeding",
                description = "Apply a cold pack wrapped in cloth for 15-20 minutes. If there is bleeding, apply gentle pressure around the wound (not directly on protruding bone).",
                instructionKn = "à²¤à²£à³à²£à²¨à³†à²¯ à²ªà³à²¯à²¾à²•à³ à²¬à²³à²¸à²¿",
                descriptionKn = "à²à²¸à³ à²…à²¥à²µà²¾ à²¤à²£à³à²£à²¨à³†à²¯ à²ªà³à²¯à²¾à²•à³ à²…à²¨à³à²¨à³ à²¬à²Ÿà³à²Ÿà³†à²¯à²²à³à²²à²¿ à²¸à³à²¤à³à²¤à²¿ 15-20 à²¨à²¿à²®à²¿à²· à²‡à²Ÿà³à²Ÿà³ à²Šà²¤ à²•à²¡à²¿à²®à³† à²®à²¾à²¡à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Watch danger signs and call 108",
                description = "Call emergency services immediately for severe deformity, open fracture, numbness, pale/cold fingers or toes, severe pain, or inability to move.",
                instructionKn = "à²µà³ˆà²¦à³à²¯à²•à³€à²¯ à²šà²¿à²•à²¿à²¤à³à²¸à³† à²ªà²¡à³†à²¯à²¿à²°à²¿",
                descriptionKn = "à²†à²•à²¾à²° à²¬à²¦à²²à²¾à²µà²£à³†, à²¤à³€à²µà³à²° à²¨à³‹à²µà³, à²¸à³à²®à³à²®à²¨à²¾à²—à³à²µà³à²¦à³, à²¤à³†à²°à³†à²¦ à²—à²¾à²¯ à²…à²¥à²µà²¾ à²šà²²à²¿à²¸à²²à²¾à²—à²¦à²¿à²¦à³à²¦à²°à³† à²¤à²•à³à²·à²£ à²šà²¿à²•à²¿à²¤à³à²¸à³† à²ªà²¡à³†à²¯à²¿à²°à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Do not massage, heat, or move repeatedly",
                description = "Do not massage the area, do not apply hot oil/heat, and do not give food or drink if surgery may be needed soon.",
                instructionKn = "à²®à²¾à²¸à²¾à²œà³ à²…à²¥à²µà²¾ à²‰à²·à³à²£ à²šà²¿à²•à²¿à²¤à³à²¸à³† à²®à²¾à²¡à²¬à³‡à²¡à²¿",
                descriptionKn = "à²®à²¸à²¾à²œà³ à²®à²¾à²¡à²¬à³‡à²¡à²¿, à²‰à²·à³à²£ à²Žà²£à³à²£à³†/à²šà²¿à²•à²¿à²¤à³à²¸à³† à²®à²¾à²¡à²¬à³‡à²¡à²¿, à²†à²ªà²°à³‡à²¶à²¨à³ à²¸à²¾à²§à³à²¯à²¤à³† à²‡à²¦à³à²¦à²°à³† à²¬à²¾à²¯à²¿à²‚à²¦ à²à²¨à²¨à³à²¨à³‚ à²•à³Šà²¡à²¬à³‡à²¡à²¿.",
                isDo = false
            )
        )
    ),
    Emergency(
        title = "Heart Attack",
        titleKn = "à²¹à³ƒà²¦à²¯à²¾à²˜à²¾à²¤",
        icon = Icons.Default.Favorite,
        steps = listOf(
            Step(
                instruction = "Call emergency help",
                description = "Call 108 immediately for chest pressure, pain spreading to arm/jaw/back, sweating, nausea, or breathlessness.",
                instructionKn = "à²¤à³à²°à³à²¤à³ à²¸à²¹à²¾à²¯à²•à³à²•à³† à²•à²°à³† à²®à²¾à²¡à²¿",
                descriptionKn = "à²Žà²¦à³† à²’à²¤à³à²¤à²¡, à²•à³ˆ/à²¦à²µà²¡à³†/à²¬à³†à²¨à³à²¨à²¿à²—à³† à²¹à²°à²¡à³à²µ à²¨à³‹à²µà³, à²¬à³†à²µà²°à³, à²µà²¾à²‚à²¤à²¿ à²­à²¾à²µà²¨à³† à²…à²¥à²µà²¾ à²‰à²¸à²¿à²°à²¾à²Ÿ à²¤à³Šà²‚à²¦à²°à³† à²‡à²¦à³à²¦à²°à³† à²¤à²•à³à²·à²£ 108 à²—à³† à²•à²°à³† à²®à²¾à²¡à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Rest in a comfortable position",
                description = "Help the person sit down and loosen tight clothing. Keep them calm and monitor breathing.",
                instructionKn = "à²†à²°à²¾à²®à²¦ à²¸à³à²¥à²¿à²¤à²¿à²¯à²²à³à²²à²¿ à²•à³à²³à³à²³à²¿à²°à²¿à²¸à²¿",
                descriptionKn = "à²µà³à²¯à²•à³à²¤à²¿à²¯à²¨à³à²¨à³ à²•à³à²³à³à²³à²¿à²°à²¿à²¸à²¿ à²¬à²¿à²—à²¿à²¯à²¾à²¦ à²¬à²Ÿà³à²Ÿà³† à²¸à²¡à²¿à²²à²¿à²¸à²¿. à²¶à²¾à²‚à²¤à²µà²¾à²—à²¿à²°à²¿à²¸à²¿ à²®à²¤à³à²¤à³ à²‰à²¸à²¿à²°à²¾à²Ÿ à²—à²®à²¨à²¿à²¸à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Give aspirin only if safe",
                description = "If the person is not allergic and has no bleeding risk, they may chew aspirin while waiting for help.",
                instructionKn = "à²¸à³à²°à²•à³à²·à²¿à²¤à²µà²¾à²¦à²°à³† à²®à²¾à²¤à³à²° à²†à²¸à³à²ªà²¿à²°à²¿à²¨à³ à²¨à³€à²¡à²¿",
                descriptionKn = "à²…à²²à²°à³à²œà²¿ à²‡à²²à³à²²à²¦à³† à²°à²•à³à²¤à²¸à³à²°à²¾à²µà²¦ à²…à²ªà²¾à²¯ à²‡à²²à³à²²à²¦à²¿à²¦à³à²¦à²°à³†, à²¸à²¹à²¾à²¯ à²¬à²°à³à²µà²µà²°à³†à²—à³† à²†à²¸à³à²ªà²¿à²°à²¿à²¨à³ à²®à³†à²²à³à²²à²²à³ à²•à³Šà²¡à²¬à²¹à³à²¦à³.",
                isDo = true
            )
        )
    ),
    Emergency(
        title = "Seizure",
        titleKn = "à²…à²ªà²¸à³à²®à²¾à²° à²¦à²¾à²³à²¿",
        icon = Icons.Default.Psychology,
        steps = listOf(
            Step(
                instruction = "Protect from injury",
                description = "Move dangerous objects away. Cushion the head and time the seizure.",
                instructionKn = "à²—à²¾à²¯à²¦à²¿à²‚à²¦ à²°à²•à³à²·à²¿à²¸à²¿",
                descriptionKn = "à²…à²ªà²¾à²¯à²•à²¾à²°à²¿ à²µà²¸à³à²¤à³à²—à²³à²¨à³à²¨à³ à²¦à³‚à²° à²®à²¾à²¡à²¿. à²¤à²²à³†à²—à³† à²®à³ƒà²¦à³à²µà²¾à²¦ à²¬à³†à²‚à²¬à²² à²¨à³€à²¡à²¿ à²®à²¤à³à²¤à³ à²¦à²¾à²³à²¿à²¯ à²¸à²®à²¯à²µà²¨à³à²¨à³ à²—à²®à²¨à²¿à²¸à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Do not restrain or put anything in mouth",
                description = "Do not hold the person down and do not put water, food, fingers, or objects in the mouth.",
                instructionKn = "à²¹à²¿à²¡à²¿à²¦à³à²•à³Šà²³à³à²³à²¬à³‡à²¡à²¿ à²…à²¥à²µà²¾ à²¬à²¾à²¯à²¿à²—à³† à²à²¨à²¨à³à²¨à³‚ à²¹à²¾à²•à²¬à³‡à²¡à²¿",
                descriptionKn = "à²µà³à²¯à²•à³à²¤à²¿à²¯à²¨à³à²¨à³ à²¬à²²à²µà²¾à²—à²¿ à²¹à²¿à²¡à²¿à²¦à³à²•à³Šà²³à³à²³à²¬à³‡à²¡à²¿. à²¬à²¾à²¯à²¿à²—à³† à²¨à³€à²°à³, à²†à²¹à²¾à²°, à²¬à³†à²°à²³à³ à²…à²¥à²µà²¾ à²µà²¸à³à²¤à³à²—à²³à²¨à³à²¨à³ à²¹à²¾à²•à²¬à³‡à²¡à²¿.",
                isDo = false
            ),
            Step(
                instruction = "Place on side after shaking stops",
                description = "When movements stop, turn the person onto their side and check breathing. Call help if it lasts over 5 minutes.",
                instructionKn = "à²¦à²¾à²³à²¿ à²¨à²¿à²‚à²¤ à²¨à²‚à²¤à²° à²¬à²¦à²¿à²—à³† à²®à²²à²—à²¿à²¸à²¿",
                descriptionKn = "à²šà²²à²¨à³† à²¨à²¿à²‚à²¤ à²¨à²‚à²¤à²° à²µà³à²¯à²•à³à²¤à²¿à²¯à²¨à³à²¨à³ à²¬à²¦à²¿à²—à³† à²®à²²à²—à²¿à²¸à²¿ à²‰à²¸à²¿à²°à²¾à²Ÿ à²ªà²°à²¿à²¶à³€à²²à²¿à²¸à²¿. 5 à²¨à²¿à²®à²¿à²·à²•à³à²•à²¿à²‚à²¤ à²¹à³†à²šà³à²šà³ à²‡à²¦à³à²¦à²°à³† à²¸à²¹à²¾à²¯à²•à³à²•à³† à²•à²°à³† à²®à²¾à²¡à²¿.",
                isDo = true
            )
        )
    ),
    Emergency(
        title = "Heat Stroke",
        titleKn = "à²¬à²¿à²¸à²¿à²²à²¾à²˜à²¾à²¤",
        icon = Icons.Default.AccessibilityNew,
        steps = listOf(
            Step(
                instruction = "Move to a cool place",
                description = "Call emergency help. Move the person to shade or AC and remove extra clothing.",
                instructionKn = "à²¤à²‚à²ªà²¾à²¦ à²¸à³à²¥à²³à²•à³à²•à³† à²•à²°à³†à²¦à³Šà²¯à³à²¯à²¿à²°à²¿",
                descriptionKn = "à²¤à³à²°à³à²¤à³ à²¸à²¹à²¾à²¯à²•à³à²•à³† à²•à²°à³† à²®à²¾à²¡à²¿. à²µà³à²¯à²•à³à²¤à²¿à²¯à²¨à³à²¨à³ à²¨à³†à²°à²³à³ à²…à²¥à²µà²¾ à²Ž à²¸à²¿ à²‡à²°à³à²µ à²¸à³à²¥à²³à²•à³à²•à³† à²•à²°à³†à²¦à³Šà²¯à³à²¦à³ à²¹à³†à²šà³à²šà³à²µà²°à²¿ à²¬à²Ÿà³à²Ÿà³† à²¤à³†à²—à³†à²¦à³à²¹à²¾à²•à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Cool rapidly",
                description = "Apply cool wet cloths, fan the person, or pour cool water over the body while waiting for help.",
                instructionKn = "à²µà³‡à²—à²µà²¾à²—à²¿ à²¤à²£à³à²£à²—à²¾à²—à²¿à²¸à²¿",
                descriptionKn = "à²¸à²¹à²¾à²¯ à²¬à²°à³à²µà²µà²°à³†à²—à³† à²¤à²‚à²ªà²¾à²¦ à²’à²¦à³à²¦à³† à²¬à²Ÿà³à²Ÿà³† à²‡à²¡à²¿, à²—à²¾à²³à²¿ à²¬à³€à²¸à²¿ à²…à²¥à²µà²¾ à²¦à³‡à²¹à²¦ à²®à³‡à²²à³† à²¤à²‚à²ªà³ à²¨à³€à²°à³ à²¸à³à²°à²¿à²¸à²¿.",
                isDo = true
            ),
            Step(
                instruction = "Do not force drinks if confused",
                description = "If the person is confused, unconscious, or vomiting, do not give anything by mouth.",
                instructionKn = "à²—à³Šà²‚à²¦à²²à²¦à²²à³à²²à²¿à²¦à³à²¦à²°à³† à²ªà²¾à²¨à³€à²¯à²µà²¨à³à²¨à³ à²¬à²²à²µà²‚à²¤à²µà²¾à²—à²¿ à²•à³Šà²¡à²¬à³‡à²¡à²¿",
                descriptionKn = "à²µà³à²¯à²•à³à²¤à²¿ à²—à³Šà²‚à²¦à²²à²¦à²²à³à²²à²¿à²¦à³à²¦à²°à³†, à²ªà³à²°à²œà³à²žà³† à²‡à²²à³à²²à²¦à²¿à²¦à³à²¦à²°à³† à²…à²¥à²µà²¾ à²µà²¾à²‚à²¤à²¿ à²®à²¾à²¡à³à²¤à³à²¤à²¿à²¦à³à²¦à²°à³† à²¬à²¾à²¯à²¿à²‚à²¦ à²à²¨à²¨à³à²¨à³‚ à²•à³Šà²¡à²¬à³‡à²¡à²¿.",
                isDo = false
            )
        )
    ),
    Emergency(
        title = "Electric Shock",
        titleKn = "Electric Shock",
        icon = Icons.Default.ElectricalServices,
        steps = listOf(
            Step(
                instruction = "Switch off the power source",
                description = "Turn off mains power before touching the victim. Use dry non-conductive material if needed.",
                instructionKn = "Switch off the power source",
                descriptionKn = "Turn off mains power before touching the victim. Use dry non-conductive material if needed.",
                isDo = true
            ),
            Step(
                instruction = "Check breathing and pulse",
                description = "If unresponsive and not breathing normally, start CPR and call 108 immediately.",
                instructionKn = "Check breathing and pulse",
                descriptionKn = "If unresponsive and not breathing normally, start CPR and call 108 immediately.",
                isDo = true
            ),
            Step(
                instruction = "Do not touch live wires or apply water",
                description = "Never touch the person while current may still be flowing. Do not pour water on electrical source.",
                instructionKn = "Do not touch live wires or apply water",
                descriptionKn = "Never touch the person while current may still be flowing. Do not pour water on electrical source.",
                isDo = false
            )
        )
    ),
    Emergency(
        title = "Drowning",
        titleKn = "Drowning",
        icon = Icons.Default.Water,
        steps = listOf(
            Step(
                instruction = "Rescue safely",
                description = "Reach or throw support first. Enter water only if trained and safe.",
                instructionKn = "Rescue safely",
                descriptionKn = "Reach or throw support first. Enter water only if trained and safe.",
                isDo = true
            ),
            Step(
                instruction = "Call emergency services and check breathing",
                description = "Call 108. If no normal breathing, begin CPR immediately.",
                instructionKn = "Call emergency services and check breathing",
                descriptionKn = "Call 108. If no normal breathing, begin CPR immediately.",
                isDo = true
            ),
            Step(
                instruction = "Do not delay CPR trying to remove water",
                description = "Do not waste time trying to drain water from lungs. Prioritize breathing support.",
                instructionKn = "Do not delay CPR trying to remove water",
                descriptionKn = "Do not waste time trying to drain water from lungs. Prioritize breathing support.",
                isDo = false
            )
        )
    ),
    Emergency(
        title = "Poisoning",
        titleKn = "Poisoning",
        icon = Icons.Default.Masks,
        steps = listOf(
            Step(
                instruction = "Identify poison and call 108",
                description = "Keep sample/container safely and call emergency services immediately.",
                instructionKn = "Identify poison and call 108",
                descriptionKn = "Keep sample/container safely and call emergency services immediately.",
                isDo = true
            ),
            Step(
                instruction = "Keep person safe and monitor",
                description = "Check breathing and consciousness. Place on side if drowsy or vomiting.",
                instructionKn = "Keep person safe and monitor",
                descriptionKn = "Check breathing and consciousness. Place on side if drowsy or vomiting.",
                isDo = true
            ),
            Step(
                instruction = "Do not force vomiting",
                description = "Do not give milk, lemon, or home remedies unless advised by medical professionals.",
                instructionKn = "Do not force vomiting",
                descriptionKn = "Do not give milk, lemon, or home remedies unless advised by medical professionals.",
                isDo = false
            )
        )
    )
)

val firstAidKitItems = listOf(
    KitItem("Adhesive bandages", "à²…à²‚à²Ÿà³à²µ à²¬à³à²¯à²¾à²‚à²¡à³‡à²œà³", "Wound Care", "à²—à²¾à²¯à²¦ à²†à²°à³ˆà²•à³†"),
    KitItem("Sterile gauze pads", "à²¸à³à²Ÿà³†à²°à³ˆà²²à³ à²—à²¾à²œà³ à²ªà³à²¯à²¾à²¡à³", "Wound Care", "à²—à²¾à²¯à²¦ à²†à²°à³ˆà²•à³†"),
    KitItem("Roller bandage", "à²°à³‹à²²à²°à³ à²¬à³à²¯à²¾à²‚à²¡à³‡à²œà³", "Wound Care", "à²—à²¾à²¯à²¦ à²†à²°à³ˆà²•à³†"),
    KitItem("Medical tape", "à²®à³†à²¡à²¿à²•à²²à³ à²Ÿà³‡à²ªà³", "Wound Care", "à²—à²¾à²¯à²¦ à²†à²°à³ˆà²•à³†"),
    KitItem("Antiseptic wipes", "à²†à²‚à²Ÿà²¿à²¸à³†à²ªà³à²Ÿà²¿à²•à³ à²µà³ˆà²ªà³à²¸à³", "Wound Care", "à²—à²¾à²¯à²¦ à²†à²°à³ˆà²•à³†"),
    KitItem("Disposable gloves", "à²¬à²¿à²¸à²¾à²¡à²¬à²¹à³à²¦à²¾à²¦ à²•à³ˆà²—à²µà²¸à³", "Protection", "à²°à²•à³à²·à²£à³†"),
    KitItem("Face mask", "à²®à³à²– à²®à²¾à²¸à³à²•à³", "Protection", "à²°à²•à³à²·à²£à³†"),
    KitItem("CPR face shield", "à²¸à²¿ à²ªà²¿ à²†à²°à³ à²«à³‡à²¸à³ à²¶à³€à²²à³à²¡à³", "Protection", "à²°à²•à³à²·à²£à³†"),
    KitItem("Digital thermometer", "à²¡à²¿à²œà²¿à²Ÿà²²à³ à²¥à²°à³à²®à²¾à²®à³€à²Ÿà²°à³", "Tools", "à²ªà²°à²¿à²•à²°à²—à²³à³"),
    KitItem("Small scissors", "à²šà²¿à²•à³à²• à²•à²¤à³à²¤à²°à²¿", "Tools", "à²ªà²°à²¿à²•à²°à²—à²³à³"),
    KitItem("Tweezers", "à²Ÿà³à²µà³€à²œà²°à³à²¸à³", "Tools", "à²ªà²°à²¿à²•à²°à²—à²³à³"),
    KitItem("Instant cold pack", "à²¤à²•à³à²·à²£à²¦ à²¤à²£à³à²£à²¨à³†à²¯ à²ªà³à²¯à²¾à²•à³", "Tools", "à²ªà²°à²¿à²•à²°à²—à²³à³"),
    KitItem("Paracetamol", "à²ªà³à²¯à²¾à²°à²¾à²¸à²¿à²Ÿà²®à²¾à²²à³", "Medicines", "à²”à²·à²§à²¿à²—à²³à³"),
    KitItem("ORS packets", "à²“ à²†à²°à³ à²Žà²¸à³ à²ªà³à²¯à²¾à²•à³†à²Ÿà³", "Medicines", "à²”à²·à²§à²¿à²—à²³à³"),
    KitItem("Burn dressing", "à²¸à³à²Ÿà³à²Ÿ à²—à²¾à²¯à²¦ à²¡à³à²°à³†à²¸à²¿à²‚à²—à³", "Medicines", "à²”à²·à²§à²¿à²—à²³à³")
)

val simulatedHospitals = listOf(
    Hospital("Victoria Hospital, Bengaluru", "à²µà²¿à²•à³à²Ÿà³‹à²°à²¿à²¯à²¾ à²†à²¸à³à²ªà²¤à³à²°à³†, à²¬à³†à²‚à²—à²³à³‚à²°à³", 12.9630, 77.5737, "108", "Fort Rd, Bengaluru", "Fort Rd, Bengaluru"),
    Hospital("Bowring and Lady Curzon Hospital, Bengaluru", "à²¬à³Œà²°à²¿à²‚à²—à³ à²®à²¤à³à²¤à³ à²²à³‡à²¡à²¿ à²•à²°à³à²œà²¨à³ à²†à²¸à³à²ªà²¤à³à²°à³†, à²¬à³†à²‚à²—à²³à³‚à²°à³", 12.9822, 77.6033, "108", "Shivaji Nagar, Bengaluru", "Shivaji Nagar, Bengaluru"),
    Hospital("NIMHANS, Bengaluru", "à²¨à²¿à²®à³à²¹à²¾à²¨à³à²¸à³, à²¬à³†à²‚à²—à²³à³‚à²°à³", 12.9430, 77.5963, "108", "Hosur Rd, Bengaluru", "Hosur Rd, Bengaluru"),
    Hospital("K. R. Hospital, Mysuru", "à²•à³†. à²†à²°à³. à²†à²¸à³à²ªà²¤à³à²°à³†, à²®à³ˆà²¸à³‚à²°à³", 12.3140, 76.6510, "108", "Sayyaji Rao Rd, Mysuru", "Sayyaji Rao Rd, Mysuru"),
    Hospital("KIMS Hospital, Hubballi", "à²•à²¿à²®à³à²¸à³ à²†à²¸à³à²ªà²¤à³à²°à³†, à²¹à³à²¬à³à²¬à²³à³à²³à²¿", 15.3647, 75.1240, "108", "Vidyanagar, Hubballi", "Vidyanagar, Hubballi"),
    Hospital("Wenlock District Hospital, Mangaluru", "à²µà³†à²¨à³à²²à²¾à²•à³ à²œà²¿à²²à³à²²à²¾ à²†à²¸à³à²ªà²¤à³à²°à³†, à²®à²‚à²—à²³à³‚à²°à³", 12.8680, 74.8420, "108", "Hampankatta, Mangaluru", "Hampankatta, Mangaluru"),
    Hospital("McGann Hospital, Shivamogga", "à²®à³à²¯à²¾à²•à³â€Œà²—à³à²¯à²¾à²¨à³ à²†à²¸à³à²ªà²¤à³à²°à³†, à²¶à²¿à²µà²®à³Šà²—à³à²—", 13.9299, 75.5681, "108", "Sagar Rd, Shivamogga", "Sagar Rd, Shivamogga"),
    Hospital("District Hospital, Belagavi", "à²œà²¿à²²à³à²²à²¾ à²†à²¸à³à²ªà²¤à³à²°à³†, à²¬à³†à²³à²—à²¾à²µà²¿", 15.8497, 74.4977, "108", "Civil Hospital Rd, Belagavi", "Civil Hospital Rd, Belagavi"),
    Hospital("District Hospital, Kalaburagi", "à²œà²¿à²²à³à²²à²¾ à²†à²¸à³à²ªà²¤à³à²°à³†, à²•à²²à²¬à³à²°à²—à²¿", 17.3297, 76.8343, "108", "Sedam Rd, Kalaburagi", "Sedam Rd, Kalaburagi")
)
