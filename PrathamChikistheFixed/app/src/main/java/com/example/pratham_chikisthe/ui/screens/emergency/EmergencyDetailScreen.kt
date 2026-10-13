package com.example.pratham_chikisthe.ui.screens.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pratham_chikisthe.data.local.EmergencyDataset
import com.example.pratham_chikisthe.data.models.Emergency
import com.example.pratham_chikisthe.data.models.EmergencySeverity
import com.example.pratham_chikisthe.ui.components.EmergencyIllustrationView
import com.example.pratham_chikisthe.ui.theme.EmergencyRed
import com.example.pratham_chikisthe.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyDetailScreen(
    emergency: Emergency,
    currentLangCode: String,
    onBack: () -> Unit,
    onStartStepMode: () -> Unit,
    onSpeak: (String) -> Unit,
    onCall112: () -> Unit,
    onSelectRelatedEmergency: (Emergency) -> Unit = {}
) {
    val (severityColor, severityBanner) = when (emergency.severity) {
        EmergencySeverity.CRITICAL -> Pair(EmergencyRed, "🔴 CRITICAL — CALL 112 IMMEDIATELY")
        EmergencySeverity.URGENT -> Pair(Color(0xFFF57C00), "🟠 URGENT — SEEK PROMPT MEDICAL HELP")
        EmergencySeverity.NON_CRITICAL -> Pair(Color(0xFF2E7D32), "🟢 FIRST AID & MONITORING")
    }

    val relatedEmergencies = remember(emergency) {
        emergency.relatedEmergencyIds.mapNotNull { id ->
            EmergencyDataset.emergencies.firstOrNull { it.id == id }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = emergency.getTitle(currentLangCode),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val fullTts = emergency.getTitle(currentLangCode) + ". " + emergency.getImmediateAction(currentLangCode)
                        onSpeak(fullTts)
                    }) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Audio Guidance",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PrimaryBlue)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Severity Header Tag
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                color = severityColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = severityBanner,
                    color = severityColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }

            // Start Step-by-Step Mode Prominent Banner
            Button(
                onClick = onStartStepMode,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Interactive Mode",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "START INTERACTIVE STEP-BY-STEP MODE",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Immediate Action Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "IMMEDIATE ACTION",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = EmergencyRed
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = emergency.getImmediateAction(currentLangCode),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Stroke FAST Protocol Diagram if Stroke
            if (emergency.id == "stroke") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("STROKE F.A.S.T. RECOGNITION GUIDE", fontWeight = FontWeight.ExtraBold, color = PrimaryBlue, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("F — Face: Ask to smile. Does one side droop?", fontSize = 12.sp)
                        Text("A — Arms: Raise both arms. Does one drift down?", fontSize = 12.sp)
                        Text("S — Speech: Repeat a phrase. Is speech slurred?", fontSize = 12.sp)
                        Text("T — Time: If ANY sign present, CALL 112 NOW!", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmergencyRed)
                    }
                }
            }

            // SOP Steps Listing
            Text(
                text = "Standard Operating Procedure (SOP)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            emergency.steps.forEach { step ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = PrimaryBlue,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${step.stepNumber}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = step.getInstruction(currentLangCode),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = step.getDescription(currentLangCode),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (!step.illustrationType.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            EmergencyIllustrationView(type = step.illustrationType)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // DO & DON'T Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // DO Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "DO", tint = Color(0xFF2E7D32))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("DO", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        emergency.getDoList(currentLangCode).forEach { item ->
                            Text("• $item", fontSize = 11.sp, color = Color(0xFF1B5E20), modifier = Modifier.padding(bottom = 4.dp))
                        }
                    }
                }

                // DON'T Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Cancel, contentDescription = "DON'T", tint = Color(0xFFC62828))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("DON'T", fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        emergency.getDontList(currentLangCode).forEach { item ->
                            Text("• $item", fontSize = 11.sp, color = Color(0xFFB71C1C), modifier = Modifier.padding(bottom = 4.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Related Emergencies Navigation Links
            if (relatedEmergencies.isNotEmpty()) {
                Text(
                    text = "Related Emergencies",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(relatedEmergencies) { rel ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectRelatedEmergency(rel) },
                            color = PrimaryBlue.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "→ ${rel.getTitle(currentLangCode)}",
                                color = PrimaryBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Call 112 Escalation Button
            Button(
                onClick = onCall112,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Call, contentDescription = "Call 112", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("WHEN TO CALL 112", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
