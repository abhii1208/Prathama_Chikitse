package com.example.pratham_chikisthe.ui.screens.emergency

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pratham_chikisthe.data.models.Emergency
import com.example.pratham_chikisthe.ui.components.EmergencyIllustrationView
import com.example.pratham_chikisthe.ui.theme.EmergencyRed
import com.example.pratham_chikisthe.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EmergencyStepModeScreen(
    emergency: Emergency,
    currentLangCode: String,
    onBack: () -> Unit,
    onSpeak: (String) -> Unit,
    onCall112: () -> Unit
) {
    var currentStepIndex by remember { mutableIntStateOf(0) }
    val totalSteps = emergency.steps.size
    val currentStep = emergency.steps.getOrNull(currentStepIndex) ?: emergency.steps.first()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = emergency.getTitle(currentLangCode),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                        color = Color.White,
                        fontSize = 18.sp
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
                        val textToRead = currentStep.getInstruction(currentLangCode) + ". " + currentStep.getDescription(currentLangCode)
                        onSpeak(textToRead)
                        val currentStep = emergency.steps.getOrNull(currentStepIndex)
                        if (currentStep != null) {
                            val textToRead = currentStep.getInstruction(currentLangCode) + ". " + currentStep.getDescription(currentLangCode)
                            onSpeak(textToRead)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Speak Step",
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
                .padding(20.dp),
                .background(Color(0xFF0F172A)) // High contrast dark slate background
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Step Progress Header
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // High-Contrast Step Progress Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "STEP ${currentStepIndex + 1} OF $totalSteps",
                    fontSize = 14.sp,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryBlue
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Progress Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(totalSteps) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == currentStepIndex) 12.dp else 8.dp)
                                .clip(CircleShape)
                                .background(if (index == currentStepIndex) PrimaryBlue else Color.White.copy(alpha = 0.4f))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { (currentStepIndex + 1).toFloat() / totalSteps.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = PrimaryBlue
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = PrimaryBlue,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
            }

            // Animated Step Card Content
            // Step Content Card
            AnimatedContent(
                targetState = currentStepIndex,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "step_animation",
                modifier = Modifier.weight(1f)
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { targetIndex ->
                val step = emergency.steps[targetIndex]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(vertical = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)) // High contrast dark card
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        EmergencyIllustrationView(type = step.illustrationType)

                        Spacer(modifier = Modifier.height(16.dp))
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = step.getInstruction(currentLangCode),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                            color = Color.White, // Crisp white title
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = step.getDescription(currentLangCode),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                            color = Color(0xFFCBD5E1), // Light neutral body text
                            fontSize = 15.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // Controls & Immediate Call Button
            Column {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { if (currentStepIndex > 0) currentStepIndex-- },
                        enabled = currentStepIndex > 0,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp)
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                            disabledContentColor = Color.White.copy(alpha = 0.3f)
                        )
                    ) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "Previous")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PREVIOUS", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (currentStepIndex < totalSteps - 1) currentStepIndex++
                            else onBack()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text(
                            text = if (currentStepIndex < totalSteps - 1) "NEXT" else "FINISH",
                            text = if (currentStepIndex < totalSteps - 1) "NEXT →" else "FINISH ✓",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.ChevronRight, contentDescription = "Next", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onCall112,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Call 112", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CALL 112", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.White)
                    Text("CALL 112 NOW", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                }
            }
        }
    }
}

