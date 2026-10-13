package com.example.pratham_chikisthe.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pratham_chikisthe.data.models.Emergency
import com.example.pratham_chikisthe.data.models.EmergencyCategory
import com.example.pratham_chikisthe.data.models.EmergencySeverity
import com.example.pratham_chikisthe.ui.components.BrandHeader
import com.example.pratham_chikisthe.ui.theme.EmergencyRed
import com.example.pratham_chikisthe.ui.theme.PrimaryBlue

@Composable
fun HomeScreen(
    emergencies: List<Emergency>,
    currentLangCode: String,
    onLanguageSelected: (String) -> Unit,
    onEmergencyClick: (Emergency) -> Unit,
    onQuickCall: () -> Unit,
    onToggleFlashlight: () -> Unit,
    onToggleWhistle: () -> Unit,
    onNavigateKit: () -> Unit,
    isFlashlightOn: Boolean = false,
    isWhistleOn: Boolean = false,
    kitProgress: Float = 0f
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<EmergencyCategory?>(null) }

    val filteredList = remember(searchQuery, selectedCategory, currentLangCode, emergencies) {
        emergencies.filter { emergency ->
            val matchesCategory = selectedCategory == null || emergency.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() || emergency.matchesQuery(searchQuery, currentLangCode)
            matchesCategory && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BrandHeader(
            currentLangCode = currentLangCode,
            onLanguageSelected = onLanguageSelected
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search 280+ First Aid SOPs (e.g. burn, blood, stroke)...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Actions Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionChip(
                    title = "SOS 112",
                    icon = Icons.Default.Call,
                    containerColor = EmergencyRed,
                    contentColor = Color.White,
                    onClick = onQuickCall,
                    modifier = Modifier.weight(1f)
                )

                QuickActionChip(
                    title = if (isFlashlightOn) "Flash ON" else "Flashlight",
                    icon = Icons.Default.FlashlightOn,
                    containerColor = if (isFlashlightOn) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isFlashlightOn) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = onToggleFlashlight,
                    modifier = Modifier.weight(1f)
                )

                QuickActionChip(
                    title = if (isWhistleOn) "Stop Sound" else "Whistle",
                    icon = Icons.Default.VolumeUp,
                    containerColor = if (isWhistleOn) Color(0xFFF57C00) else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isWhistleOn) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = onToggleWhistle,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dedicated Road Accident & Mass Casualty Banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            val acc = emergencies.firstOrNull { it.id == "road_accident" }
                            if (acc != null) onEmergencyClick(acc)
                        },
                    color = EmergencyRed
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.DirectionsRun, contentDescription = "Road Accident", tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ROAD ACCIDENT MODE", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            val mass = emergencies.firstOrNull { it.id == "mass_casualty" }
                            if (mass != null) onEmergencyClick(mass)
                        },
                    color = Color(0xFF2828B8)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.MedicalServices, contentDescription = "Mass Casualty", tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("MASS CASUALTY MODE", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Categories Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All SOPs") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryBlue, selectedLabelColor = Color.White)
                    )
                }
                items(EmergencyCategory.values()) { category ->
                    val label = when (category) {
                        EmergencyCategory.LIFE_THREATENING -> "❤️ Life Threatening"
                        EmergencyCategory.BLEEDING_WOUNDS -> "🩸 Bleeding & Wounds"
                        EmergencyCategory.BURNS -> "🔥 Burns & Scalds"
                        EmergencyCategory.FRACTURES -> "🦴 Fractures & Joints"
                        EmergencyCategory.HEAD_FACE -> "👁️ Head & Face"
                        EmergencyCategory.BREATHING -> "🫁 Breathing"
                        EmergencyCategory.HEART -> "❤️ Heart & Stroke"
                        EmergencyCategory.NEUROLOGICAL -> "🧠 Brain & Seizures"
                        EmergencyCategory.ALLERGIES -> "🌾 Allergies"
                        EmergencyCategory.POISONING -> "☠️ Poisoning"
                        EmergencyCategory.HEAT_COLD -> "🌡️ Heat & Cold"
                        EmergencyCategory.WATER -> "🌊 Water"
                        EmergencyCategory.ELECTRICAL -> "⚡ Electrical"
                        EmergencyCategory.FIRE_SMOKE -> "🔥 Fire & Smoke"
                        EmergencyCategory.BITES_STINGS -> "🐍 Bites & Stings"
                        EmergencyCategory.DIGESTIVE -> "🤢 Food & Stomach"
                        EmergencyCategory.DIABETIC -> "🩸 Diabetic"
                        EmergencyCategory.BEHAVIORAL -> "🧠 Behavioral"
                        EmergencyCategory.PREGNANCY -> "🤰 Pregnancy"
                        EmergencyCategory.PEDIATRIC -> "👶 Infant & Child"
                        EmergencyCategory.ELDERLY -> "🧓 Elderly & Falls"
                        EmergencyCategory.ROAD_ACCIDENTS -> "🚗 Road Accidents"
                        EmergencyCategory.WORKPLACE_OUTDOOR -> "🏭 Workplace"
                    }
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = if (selectedCategory == category) null else category },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryBlue, selectedLabelColor = Color.White)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Emergency Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredList) { emergency ->
                    EmergencyGridCard(
                        emergency = emergency,
                        currentLangCode = currentLangCode,
                        onClick = { onEmergencyClick(emergency) }
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionChip(
    title: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = contentColor, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = contentColor, maxLines = 1)
        }
    }
}

@Composable
fun EmergencyGridCard(
    emergency: Emergency,
    currentLangCode: String,
    onClick: () -> Unit
) {
    val icon = when (emergency.iconName) {
        "Favorite" -> Icons.Default.Favorite
        "Opacity" -> Icons.Default.Opacity
        "Whatshot" -> Icons.Default.Whatshot
        "Restaurant" -> Icons.Default.Restaurant
        "BugReport" -> Icons.Default.BugReport
        "Healing" -> Icons.Default.Healing
        "Psychology" -> Icons.Default.Psychology
        "AccessibilityNew" -> Icons.Default.AccessibilityNew
        "DirectionsRun" -> Icons.Default.DirectionsRun
        else -> Icons.Default.Healing
    }

    val (severityColor, severityText) = when (emergency.severity) {
        EmergencySeverity.CRITICAL -> Pair(EmergencyRed, "🔴 CRITICAL")
        EmergencySeverity.URGENT -> Pair(Color(0xFFF57C00), "🟠 URGENT")
        EmergencySeverity.NON_CRITICAL -> Pair(Color(0xFF2E7D32), "🟢 FIRST AID")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = emergency.getTitle(currentLangCode), tint = PrimaryBlue, modifier = Modifier.size(28.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = severityColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = severityText,
                        color = severityColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = emergency.getTitle(currentLangCode),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
