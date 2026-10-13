package com.example.pratham_chikisthe.ui.navigation

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pratham_chikisthe.PreferenceManager
import com.example.pratham_chikisthe.data.models.Emergency
import com.example.pratham_chikisthe.data.models.supportedLanguages
import com.example.pratham_chikisthe.data.repository.EmergencyRepository
import com.example.pratham_chikisthe.data.repository.KitRepository
import com.example.pratham_chikisthe.services.EmergencyCallManager
import com.example.pratham_chikisthe.services.FlashlightManager
import com.example.pratham_chikisthe.services.TtsManager
import com.example.pratham_chikisthe.services.WhistleManager
import com.example.pratham_chikisthe.ui.components.SOSButton
import com.example.pratham_chikisthe.ui.screens.emergency.EmergencyDetailScreen
import com.example.pratham_chikisthe.ui.screens.emergency.EmergencyStepModeScreen
import com.example.pratham_chikisthe.ui.screens.home.HomeScreen
import com.example.pratham_chikisthe.ui.screens.hospital.HospitalScreen
import com.example.pratham_chikisthe.ui.screens.kit.KitScreen
import com.example.pratham_chikisthe.ui.screens.language.LanguageSelectionModalScreen
import com.example.pratham_chikisthe.ui.screens.profile.ProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    preferenceManager: PreferenceManager,
    ttsManager: TtsManager,
    flashlightManager: FlashlightManager,
    whistleManager: WhistleManager,
    callManager: EmergencyCallManager,
    userLat: Double? = null,
    userLng: Double? = null,
    onRequestLocation: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val isLangSet by preferenceManager.isLanguageSet.collectAsStateWithLifecycle(initialValue = false)
    val persistentLangCode by preferenceManager.getSelectedLanguage.collectAsStateWithLifecycle(initialValue = "en")

    val userName by preferenceManager.getName.collectAsStateWithLifecycle(initialValue = "")
    val bloodGroup by preferenceManager.getBloodGroup.collectAsStateWithLifecycle(initialValue = "")
    val iceContact by preferenceManager.getEmergencyContact.collectAsStateWithLifecycle(initialValue = "")
    val emergencyContacts by preferenceManager.getEmergencyContacts.collectAsStateWithLifecycle(initialValue = emptySet())
    val checkedItemIds by preferenceManager.getCheckedItems.collectAsStateWithLifecycle(initialValue = emptySet())

    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }
    var selectedEmergency by remember { mutableStateOf<Emergency?>(null) }
    var isFlashlightOn by remember { mutableStateOf(false) }
    var isWhistleOn by remember { mutableStateOf(false) }

    val emergencyRepository = remember { EmergencyRepository() }
    val kitRepository = remember { KitRepository() }
    val kitProgress = kitRepository.calculateProgress(checkedItemIds)

    val currentLang = supportedLanguages.find { it.code == persistentLangCode } ?: supportedLanguages.first()
    val layoutDirection = if (currentLang.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        if (!isLangSet) {
            LanguageSelectionModalScreen(
                onLanguageSelected = { chosenCode ->
                    scope.launch {
                        preferenceManager.setSelectedLanguage(chosenCode)
                        currentScreen = AppScreen.Home
                    }
                }
            )
        } else {
            Scaffold(
                bottomBar = {
                    if (currentScreen != AppScreen.Details && currentScreen != AppScreen.StepMode) {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.Home,
                                onClick = { currentScreen = AppScreen.Home },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home") }
                            )
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.Hospitals,
                                onClick = { currentScreen = AppScreen.Hospitals },
                                icon = { Icon(Icons.Default.LocalHospital, contentDescription = "Hospitals") },
                                label = { Text("Hospitals") }
                            )
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.Kit,
                                onClick = { currentScreen = AppScreen.Kit },
                                icon = { Icon(Icons.Default.MedicalServices, contentDescription = "Kit") },
                                label = { Text("Kit") }
                            )
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.Profile,
                                onClick = { currentScreen = AppScreen.Profile },
                                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                                label = { Text("Profile") }
                            )
                        }
                    }
                },
                floatingActionButton = {
                    if (currentScreen != AppScreen.Details && currentScreen != AppScreen.StepMode) {
                        SOSButton(
                            onClick = { callManager.dialEmergency("112") }
                        )
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            AppScreen.LanguageModal -> LanguageSelectionModalScreen(
                                onLanguageSelected = { chosenCode ->
                                    scope.launch {
                                        preferenceManager.setSelectedLanguage(chosenCode)
                                        currentScreen = AppScreen.Home
                                    }
                                }
                            )

                            AppScreen.Home -> HomeScreen(
                                emergencies = emergencyRepository.getEmergencies(),
                                currentLangCode = persistentLangCode,
                                onLanguageSelected = { chosenCode ->
                                    scope.launch { preferenceManager.setSelectedLanguage(chosenCode) }
                                },
                                onEmergencyClick = { emergency ->
                                    selectedEmergency = emergency
                                    currentScreen = AppScreen.Details
                                },
                                onQuickCall = { callManager.dialEmergency("112") },
                                onToggleFlashlight = {
                                    val newStatus = flashlightManager.setFlashlight(!isFlashlightOn)
                                    isFlashlightOn = newStatus
                                },
                                onToggleWhistle = {
                                    whistleManager.toggleWhistle(context) { isWhistleOn = it }
                                },
                                onNavigateKit = { currentScreen = AppScreen.Kit },
                                isFlashlightOn = isFlashlightOn,
                                isWhistleOn = isWhistleOn,
                                kitProgress = kitProgress
                            )

                            AppScreen.Details -> selectedEmergency?.let { emergency ->
                                EmergencyDetailScreen(
                                    emergency = emergency,
                                    currentLangCode = persistentLangCode,
                                    onBack = { currentScreen = AppScreen.Home },
                                    onStartStepMode = { currentScreen = AppScreen.StepMode },
                                    onSpeak = { text -> ttsManager.speak(text, persistentLangCode) },
                                    onCall112 = { callManager.dialEmergency("112") },
                                    onSelectRelatedEmergency = { rel ->
                                        selectedEmergency = rel
                                    }
                                )
                            }

                            AppScreen.StepMode -> selectedEmergency?.let { emergency ->
                                EmergencyStepModeScreen(
                                    emergency = emergency,
                                    currentLangCode = persistentLangCode,
                                    onBack = { currentScreen = AppScreen.Details },
                                    onSpeak = { text -> ttsManager.speak(text, persistentLangCode) },
                                    onCall112 = { callManager.dialEmergency("112") }
                                )
                            }

                            AppScreen.Hospitals -> HospitalScreen(
                                currentLangCode = persistentLangCode,
                                onLanguageSelected = { chosenCode ->
                                    scope.launch { preferenceManager.setSelectedLanguage(chosenCode) }
                                },
                                userLat = userLat,
                                userLng = userLng,
                                onRequestLocation = onRequestLocation
                            )

                            AppScreen.Kit -> KitScreen(
                                checkedItemIds = checkedItemIds,
                                currentLangCode = persistentLangCode,
                                onLanguageSelected = { chosenCode ->
                                    scope.launch { preferenceManager.setSelectedLanguage(chosenCode) }
                                },
                                onToggleItem = { itemId ->
                                    val newSet = if (checkedItemIds.contains(itemId)) checkedItemIds - itemId else checkedItemIds + itemId
                                    scope.launch { preferenceManager.updateCheckedItems(newSet) }
                                }
                            )

                            AppScreen.Profile -> ProfileScreen(
                                currentLangCode = persistentLangCode,
                                onLanguageSelected = { chosenCode ->
                                    scope.launch { preferenceManager.setSelectedLanguage(chosenCode) }
                                },
                                userName = userName,
                                bloodGroup = bloodGroup,
                                iceContact = iceContact,
                                emergencyContacts = emergencyContacts,
                                onSaveProfile = { name, blood, ice ->
                                    scope.launch { preferenceManager.saveData(name, blood, ice) }
                                },
                                onAddContact = { name, phone ->
                                    scope.launch { preferenceManager.addEmergencyContact(name, phone) }
                                },
                                onRemoveContact = { contact ->
                                    scope.launch { preferenceManager.removeEmergencyContact(contact) }
                                },
                                onCallContact = { phone ->
                                    callManager.dialEmergency(phone)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
