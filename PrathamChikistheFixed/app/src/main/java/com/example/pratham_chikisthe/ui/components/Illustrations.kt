package com.example.pratham_chikisthe.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pratham_chikisthe.ui.theme.PrimaryBlue

@Composable
fun EmergencyIllustrationView(
    type: String?,
    modifier: Modifier = Modifier
) {
    if (type.isNullOrBlank()) return

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        color = Color(0xFF1E1B4B) // Dark indigo container for high contrast
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (type) {
                "cpr_placement", "cpr_compressions" -> IllustrationTemplate(
                    icon = Icons.Default.Favorite,
                    title = "CPR CHEST COMPRESSIONS",
                    sub = "Place hand heel on chest center. Push 2 inches deep."
                    sub = "Place hand heel on chest center. Push 2 inches deep at 100-120 bpm."
                )
                "pressure_gauze", "bandage_wrap" -> IllustrationTemplate(
                    icon = Icons.Default.Healing,
                    title = "DIRECT PRESSURE ON WOUND",
                    sub = "Press clean cloth firmly. Do not remove soaked layers."
                    sub = "Press clean sterile cloth firmly. Do not remove soaked layers."
                )
                "cool_burn" -> IllustrationTemplate(
                    icon = Icons.Default.WaterDrop,
                    title = "COOL RUNNING WATER",
                    sub = "Hold under cool tap water for 20 mins. Avoid ice."
                    sub = "Hold under cool tap water for 20 mins. Never apply ice or butter."
                )
                "choking_backblow", "choking_heimlich" -> IllustrationTemplate(
                    icon = Icons.Default.AccessibilityNew,
                    title = "5 BACK BLOWS & 5 ABDOMINAL THRUSTS",
                    sub = "Lean forward. Deliver blows between shoulder blades."
                    sub = "Lean victim forward. Deliver firm blows between shoulder blades."
                )
                "splint_limb" -> IllustrationTemplate(
                    icon = Icons.Default.Healing,
                    title = "IMMOBILIZE BROKEN BONE",
                    sub = "Support limb above and below joint. Do not force straight."
                    sub = "Support limb above and below joint. Do not attempt to realign."
                )
                "ice_pack" -> IllustrationTemplate(
                    icon = Icons.Default.AcUnit,
                    title = "COLD PACK FOR SWELLING",
                    sub = "Wrap ice in towel. Apply for 15-20 minutes."
                    sub = "Wrap ice in cloth towel. Apply for 15-20 minutes."
                )
                else -> IllustrationTemplate(
                    icon = Icons.Default.CheckCircle,
                    title = "FIRST AID SOP GUIDANCE",
                    sub = "Follow step-by-step instructions carefully."
                )
            }
        }
    }
}

@Composable
private fun IllustrationTemplate(
    icon: ImageVector,
    title: String,
    sub: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = PrimaryBlue,
            tint = Color(0xFF90CAF9), // Bright soft blue icon
            modifier = Modifier.height(40.dp)
        )
        Text(
            text = title,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp,
            color = PrimaryBlue
            color = Color.White // Bright white header for maximum contrast
        )
        Text(
            text = sub,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
            color = Color(0xFFE0E0E0) // Crisp light grey description
        )
    }
}

