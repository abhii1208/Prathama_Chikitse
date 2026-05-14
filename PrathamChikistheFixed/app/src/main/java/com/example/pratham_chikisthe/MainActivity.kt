package com.example.pratham_chikisthe

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.location.Location
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pratham_chikisthe.ui.theme.PrathamChikistheTheme
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private val isTtsInitialized = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferenceManager = PreferenceManager(this)
        tts = TextToSpeech(this, this)
        enableEdgeToEdge()

        setContent {
            PrathamChikistheTheme {
                AppNavigation(
                    tts = tts,
                    isTtsEnabled = isTtsInitialized.value,
                    preferenceManager = preferenceManager
                )
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized.value = true
            tts.language = Locale.US
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(tts: TextToSpeech, isTtsEnabled: Boolean, preferenceManager: PreferenceManager) {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }
    var selectedEmergency by remember { mutableStateOf<Emergency?>(null) }
    var useKannada by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val userName by preferenceManager.getName.collectAsStateWithLifecycle(initialValue = "")
    val checkedItems by preferenceManager.getCheckedItems.collectAsStateWithLifecycle(initialValue = emptySet())
    val iceContact by preferenceManager.getEmergencyContact.collectAsStateWithLifecycle(initialValue = "")

    Scaffold(
        bottomBar = {
            if (currentScreen != AppScreen.Details) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.Home,
                        onClick = { currentScreen = AppScreen.Home },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text(if (useKannada) "ಮುಖಪುಟ" else "Home") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.Hospitals,
                        onClick = { currentScreen = AppScreen.Hospitals },
                        icon = { Icon(Icons.Default.LocalHospital, contentDescription = "Hospitals") },
                        label = { Text(if (useKannada) "ಆಸ್ಪತ್ರೆಗಳು" else "Hospitals") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.Kit,
                        onClick = { currentScreen = AppScreen.Kit },
                        icon = { Icon(Icons.Default.MedicalServices, contentDescription = "Kit") },
                        label = { Text(if (useKannada) "ಕಿಟ್" else "Kit") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.Profile,
                        onClick = { currentScreen = AppScreen.Profile },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text(if (useKannada) "ಪ್ರೊಫೈಲ್" else "Profile") }
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentScreen != AppScreen.Details) {
                val infiniteTransition = rememberInfiniteTransition(label = "sos_pulse")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.15f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "sos_scale"
                )

                FloatingActionButton(
                    onClick = { dialNumber(context, "108") },
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
                ) {
                    Text("SOS", fontWeight = FontWeight.Bold)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(250))
                },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    AppScreen.Home -> HomeScreen(
                        onEmergencyClick = { emergency ->
                            selectedEmergency = emergency
                            currentScreen = AppScreen.Details
                        },
                        useKannada = useKannada,
                        onLanguageToggle = { useKannada = it },
                        userName = userName,
                        kitProgress = checkedItems.size.toFloat() / firstAidKitItems.size.toFloat(),
                        iceContact = iceContact
                    )

                    AppScreen.Details -> selectedEmergency?.let { emergency ->
                        EmergencyDetailScreen(
                            emergency = emergency,
                            onBack = { currentScreen = AppScreen.Home },
                            tts = tts,
                            isTtsEnabled = isTtsEnabled,
                            useKannada = useKannada
                        )
                    }

                    AppScreen.Hospitals -> HospitalScreen(useKannada = useKannada)
                    AppScreen.Kit -> KitScreen(useKannada = useKannada, preferenceManager = preferenceManager)
                    AppScreen.Profile -> ProfileScreen(
                        useKannada = useKannada,
                        preferenceManager = preferenceManager,
                        tts = tts,
                        isTtsEnabled = isTtsEnabled
                    )
                }
            }
        }
    }
}

sealed class AppScreen {
    data object Home : AppScreen()
    data object Details : AppScreen()
    data object Hospitals : AppScreen()
    data object Kit : AppScreen()
    data object Profile : AppScreen()
}

@Composable
fun HomeScreen(
    onEmergencyClick: (Emergency) -> Unit,
    useKannada: Boolean,
    onLanguageToggle: (Boolean) -> Unit,
    userName: String,
    kitProgress: Float,
    iceContact: String
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredEmergencies = remember(searchQuery, useKannada) {
        val query = searchQuery.trim()
        if (query.isBlank()) {
            emergencyList
        } else {
            emergencyList.filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.titleKn.contains(query, ignoreCase = true)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .padding(top = 48.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (userName.isNotBlank()) {
                                if (useKannada) "ನಮಸ್ಕಾರ, $userName" else "Hello, $userName"
                            } else {
                                if (useKannada) "ನಮಸ್ಕಾರ" else "Hello!"
                            },
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (useKannada) "ಪ್ರಥಮ ಚಿಕಿತ್ಸೆ" else "Pratham-Chikitse",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "EN",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = if (!useKannada) FontWeight.Bold else FontWeight.Normal
                            )
                            Box(
                                modifier = Modifier
                                    .width(44.dp)
                                    .height(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Switch(
                                    checked = useKannada,
                                    onCheckedChange = onLanguageToggle,
                                    modifier = Modifier.scale(0.6f),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        uncheckedThumbColor = Color.White.copy(alpha = 0.8f)
                                    )
                                )
                            }
                            Text(
                                "KN",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = if (useKannada) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = kitProgress.coerceIn(0f, 1f),
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(CircleShape),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (useKannada) {
                            "ಕಿಟ್ ಸಿದ್ಧತೆ: ${(kitProgress.coerceIn(0f, 1f) * 100).toInt()}%"
                        } else {
                            "Kit Ready: ${(kitProgress.coerceIn(0f, 1f) * 100).toInt()}%"
                        },
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            if (useKannada) "ಹುಡುಕಿ..." else "Search emergency...",
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                HomeEmergencyTools(useKannada = useKannada, iceContact = iceContact)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (useKannada) {
                        "ಈ ಆಪ್ ಮೂಲಭೂತ ಪ್ರಥಮ ಚಿಕಿತ್ಸೆ ಮಾರ್ಗದರ್ಶನ ನೀಡುತ್ತದೆ. ಗಂಭೀರ ಪರಿಸ್ಥಿತಿಯಲ್ಲಿ ತಕ್ಷಣ ತುರ್ತು ಸೇವೆಗೆ ಕರೆ ಮಾಡಿ."
                    } else {
                        "This app provides basic first-aid guidance. Call emergency services immediately in serious situations."
                    },
                    color = Color.White.copy(alpha = 0.95f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            gridItems(filteredEmergencies, key = { it.title }) { emergency ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(350)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(350))
                ) {
                    EmergencyTile(emergency = emergency, useKannada = useKannada) {
                        onEmergencyClick(emergency)
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeEmergencyTools(useKannada: Boolean, iceContact: String) {
    val context = LocalContext.current
    val cameraManager = remember { context.getSystemService(Context.CAMERA_SERVICE) as CameraManager }
    val cameraId = remember { findFlashCameraId(cameraManager) }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Camera permission is needed for flashlight", Toast.LENGTH_SHORT).show()
        }
    }

    var flashOn by remember { mutableStateOf(false) }
    var whistleOn by remember { mutableStateOf(false) }
    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_ALARM, 100) }

    LaunchedEffect(whistleOn) {
        if (whistleOn) {
            try {
                while (true) {
                    toneGenerator.startTone(ToneGenerator.TONE_SUP_PIP, 500)
                    delay(650)
                }
            } finally {
                toneGenerator.stopTone()
            }
        } else {
            toneGenerator.stopTone()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (flashOn && cameraId != null) {
                runCatching { cameraManager.setTorchMode(cameraId, false) }
            }
            toneGenerator.release()
        }
    }

    fun toggleFlash() {
        if (cameraId == null) {
            Toast.makeText(context, "Flashlight is not available on this device", Toast.LENGTH_SHORT).show()
            return
        }
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            return
        }
        flashOn = !flashOn
        runCatching { cameraManager.setTorchMode(cameraId, flashOn) }
            .onFailure {
                flashOn = false
                Toast.makeText(context, "Unable to control flashlight", Toast.LENGTH_SHORT).show()
            }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EmergencySmallCard(
            modifier = Modifier.weight(1f),
            active = flashOn,
            icon = if (flashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
            label = if (useKannada) "ಟಾರ್ಚ್" else "Flashlight",
            activeColor = Color.Yellow.copy(alpha = 0.3f),
            onClick = { toggleFlash() }
        )

        EmergencySmallCard(
            modifier = Modifier.weight(1f),
            active = whistleOn,
            icon = if (whistleOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
            label = if (useKannada) "ಸೀಟಿ" else "Whistle",
            activeColor = Color.Red.copy(alpha = 0.3f),
            onClick = { whistleOn = !whistleOn }
        )

        if (iceContact.isNotBlank()) {
            EmergencySmallCard(
                modifier = Modifier.weight(1f),
                active = true,
                icon = Icons.Default.Call,
                label = if (useKannada) "ಕರೆ" else "Call ICE",
                activeColor = MaterialTheme.colorScheme.primary,
                onClick = { dialNumber(context, iceContact) }
            )
        }
    }
}

@Composable
private fun EmergencySmallCard(
    modifier: Modifier,
    active: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    activeColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (active) activeColor else Color.White.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmergencyTile(emergency: Emergency, useKannada: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = emergency.icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (useKannada) emergency.titleKn else emergency.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun refineKannadaTTS(text: String): String {
    return text.replace("CPR", "ಸಿ ಪಿ ಆರ್")
        .replace("AED", "ಎ ಇ ಡಿ")
        .replace("10-20", "ಹತ್ತರಿಂದ ಇಪ್ಪತ್ತು")
        .replace("15-20", "ಹದಿನೈದರಿಂದ ಇಪ್ಪತ್ತು")
        .replace("100-120", "ನೂರರಿಂದ ನೂರ ಇಪ್ಪತ್ತು")
        .replace("20", "ಇಪ್ಪತ್ತು")
        .replace("12", "ಹನ್ನೆರಡು")
        .replace("5", "ಐದು")
        .replace("ಹೈಮ್ಲಿಚ್", "ಹೈಮ್ ಲಿಚ್")
        .replace("ಆಸ್ಪಿರಿನ್", "ಆಸ್ ಪಿರಿನ್")
        .replace("ಟೂರ್ನಿಕೆಟ್", "ಟೂರ್ ನಿ ಕೆಟ್")
        .replace("ಪ್ಯಾರಾಮೆಡಿಕ್", "ಪ್ಯಾರಾ ಮೆಡಿಕ್")
        .replace("ಬ್ಯಾಂಡೇಜ್", "ಬ್ಯಾಂ ಡೇಜ್")
        .replace("108", "ಒಂದು ಸೊನ್ನೆ ಎಂಟು")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmergencyDetailScreen(
    emergency: Emergency,
    onBack: () -> Unit,
    tts: TextToSpeech,
    isTtsEnabled: Boolean,
    useKannada: Boolean
) {
    val pagerState = rememberPagerState(pageCount = { emergency.steps.size })
    var isAudioMode by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(pagerState.currentPage, isAudioMode, useKannada, isTtsEnabled) {
        if (isAudioMode && isTtsEnabled) {
            val canUseKannada = configureTtsLanguage(tts, useKannada, context)
            val step = emergency.steps[pagerState.currentPage]
            val textToSpeak = if (useKannada && canUseKannada) {
                "${refineKannadaTTS(step.instructionKn)}. ${refineKannadaTTS(step.descriptionKn)}"
            } else {
                "${step.instruction}. ${step.description}"
            }
            tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "step-${pagerState.currentPage}")
        }
    }

    DisposableEffect(Unit) {
        onDispose { tts.stop() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        AppTopAppBar(
            title = {
                Text(
                    if (useKannada) emergency.titleKn else emergency.title,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        isAudioMode = !isAudioMode
                        if (!isAudioMode) tts.stop()
                    },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isAudioMode) MaterialTheme.colorScheme.errorContainer
                            else MaterialTheme.colorScheme.primaryContainer
                        )
                ) {
                    Icon(
                        if (isAudioMode) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Audio Mode",
                        tint = if (isAudioMode) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }
            }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            pageSpacing = 16.dp
        ) { pageIndex ->
            val step = emergency.steps[pageIndex]
            StepCard(step = step, stepNumber = pageIndex + 1, useKannada = useKannada)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { scope.launch { pagerState.animateScrollToPage((pagerState.currentPage - 1).coerceAtLeast(0)) } },
                enabled = pagerState.currentPage > 0,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (useKannada) "ಹಿಂದೆ" else "Previous")
            }

            Text(
                text = if (useKannada) {
                    "ಹಂತ ${pagerState.currentPage + 1} / ${emergency.steps.size}"
                } else {
                    "Step ${pagerState.currentPage + 1} of ${emergency.steps.size}"
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(emergency.steps.size) { index ->
                    val color = if (pagerState.currentPage == index) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.LightGray
                    }
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(if (pagerState.currentPage == index) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
            Button(
                onClick = { scope.launch { pagerState.animateScrollToPage((pagerState.currentPage + 1).coerceAtMost(emergency.steps.lastIndex)) } },
                enabled = pagerState.currentPage < emergency.steps.lastIndex,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (useKannada) "ಮುಂದೆ" else "Next")
            }
        }
    }
}

@Composable
fun StepCard(step: Step, stepNumber: Int, useKannada: Boolean) {
    Card(
        modifier = Modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = CircleShape
            ) {
                Text(
                    text = "$stepNumber",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (step.isDo != null) {
                val badgeColor = if (step.isDo) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                val textColor = if (step.isDo) Color(0xFF2E7D32) else Color(0xFFC62828)
                val icon = if (step.isDo) Icons.Default.CheckCircle else Icons.Default.Cancel
                val badgeText = if (step.isDo) {
                    if (useKannada) "ಮಾಡಿ" else "DO"
                } else {
                    if (useKannada) "ಬೇಡ" else "DON'T"
                }

                Surface(
                    color = badgeColor,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = badgeText,
                            color = textColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Text(
                text = if (useKannada) step.instructionKn else step.instruction,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (useKannada) step.descriptionKn else step.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
                color = Color(0xFF333333)
            )
        }
    }
}

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadiusKm = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
        cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
        sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadiusKm * c
}

@Composable
fun HospitalScreen(useKannada: Boolean) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    Log.d("PrathamLocation", "Fetched location: ${location?.latitude}, ${location?.longitude}")
                    userLocation = location
                }
            } catch (_: SecurityException) {
                userLocation = null
            }
        }
    }

    val sortedHospitals: List<Pair<Hospital, Double?>> = remember(userLocation) {
        if (userLocation != null) {
            simulatedHospitals.map { hospital ->
                val distance = calculateDistance(
                    userLocation!!.latitude,
                    userLocation!!.longitude,
                    hospital.lat,
                    hospital.lng
                )
                hospital to distance
            }.sortedBy { it.second }
        } else {
            simulatedHospitals.map { hospital -> hospital to null }
        }
    }.take(3)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = if (useKannada) "ಕರ್ನಾಟಕ ತುರ್ತು ಕೇಂದ್ರಗಳು" else "Karnataka Emergency Centers",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (userLocation != null) {
                if (useKannada) "ನಿಮ್ಮ ಹತ್ತಿರದ ಆಸ್ಪತ್ರೆಗಳನ್ನು ತೋರಿಸಲಾಗುತ್ತಿದೆ" else "Showing hospitals near you"
            } else {
                if (useKannada) "ಹತ್ತಿರದ ಆಫ್‌ಲೈನ್ ಫಲಿತಾಂಶಗಳನ್ನು ತೋರಿಸಲಾಗುತ್ತಿದೆ" else "Showing offline emergency centers"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (!hasPermission) {
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                Text(if (useKannada) "ಸ್ಥಳ ಅನುಮತಿ ನೀಡಿ" else "Allow location for sorting")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            lazyItems(sortedHospitals) { (hospital, distance) ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally(animationSpec = tween(350)) { it / 3 } +
                        fadeIn(animationSpec = tween(350))
                ) {
                    HospitalCard(
                        hospital = hospital,
                        distance = distance,
                        useKannada = useKannada,
                        onMap = { openHospitalMap(context, hospital) },
                        onCall = { dialNumber(context, hospital.contact) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HospitalCard(
    hospital: Hospital,
    distance: Double?,
    useKannada: Boolean,
    onMap: () -> Unit,
    onCall: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color.White
            ) {
                Icon(
                    Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (useKannada) hospital.nameKn else hospital.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DirectionsRun,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    val distanceText = distance?.let {
                        String.format(Locale.getDefault(), "%.1f km", it)
                    } ?: if (useKannada) "ಸ್ಥಳ ಲಭ್ಯವಿಲ್ಲ" else "Location unavailable"
                    Text(
                        text = if (useKannada && distance != null) "$distanceText ದೂರ" else distanceText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "Phone: ${hospital.contact}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (useKannada) hospital.addressKn else hospital.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onMap,
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(Icons.Default.Map, contentDescription = "Map", tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onCall,
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White)
            }
        }
    }
}

@Composable
fun KitScreen(useKannada: Boolean, preferenceManager: PreferenceManager) {
    val checkedItems by preferenceManager.getCheckedItems.collectAsStateWithLifecycle(initialValue = emptySet())
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (useKannada) "ಪ್ರಥಮ ಚಿಕಿತ್ಸಾ ಕಿಟ್" else "First Aid Kit",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (useKannada) "ವಸ್ತುಗಳನ್ನು ಪರಿಶೀಲಿಸಿ" else "Checklist",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            if (checkedItems.isNotEmpty()) {
                TextButton(onClick = { scope.launch { preferenceManager.updateCheckedItems(emptySet()) } }) {
                    Text(if (useKannada) "ಎಲ್ಲವನ್ನೂ ಅಳಿಸಿ" else "Clear All")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val categories = firstAidKitItems
            .map { if (useKannada) it.categoryKn else it.category }
            .distinct()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            categories.forEach { category ->
                item {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                val itemsInCategory = firstAidKitItems.filter {
                    (if (useKannada) it.categoryKn else it.category) == category
                }
                lazyItems(itemsInCategory, key = { it.name }) { kitItem ->
                    KitChecklistCard(
                        item = kitItem,
                        useKannada = useKannada,
                        checked = checkedItems.contains(kitItem.name),
                        onCheckedChange = { isChecked ->
                            val newItems = if (isChecked) {
                                checkedItems + kitItem.name
                            } else {
                                checkedItems - kitItem.name
                            }
                            scope.launch { preferenceManager.updateCheckedItems(newItems) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun KitChecklistCard(
    item: KitItem,
    useKannada: Boolean,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (checked) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .clickable { onCheckedChange(!checked) }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = { onCheckedChange(it) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (useKannada) item.nameKn else item.name,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (checked) TextDecoration.LineThrough else null
            )
        }
    }
}

@Composable
fun ProfileScreen(
    useKannada: Boolean,
    preferenceManager: PreferenceManager,
    tts: TextToSpeech,
    isTtsEnabled: Boolean
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val savedName by preferenceManager.getName.collectAsStateWithLifecycle(initialValue = "")
    val savedBloodGroup by preferenceManager.getBloodGroup.collectAsStateWithLifecycle(initialValue = "")
    val savedEmergencyContact by preferenceManager.getEmergencyContact.collectAsStateWithLifecycle(initialValue = "")
    val savedContacts by preferenceManager.getEmergencyContacts.collectAsStateWithLifecycle(initialValue = emptySet())

    var name by remember(savedName) { mutableStateOf(savedName) }
    var bloodGroup by remember(savedBloodGroup) { mutableStateOf(savedBloodGroup) }
    var emergencyContact by remember(savedEmergencyContact) { mutableStateOf(savedEmergencyContact) }
    var contactName by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = if (useKannada) "ವೈಯಕ್ತಿಕ ತುರ್ತು ಪ್ರೊಫೈಲ್" else "Personal Emergency Profile (ICE)",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (useKannada) "ತುರ್ತು ಸಂದರ್ಭದಲ್ಲಿ ಈ ಮಾಹಿತಿ ಸಹಾಯ ಮಾಡುತ್ತದೆ" else "This information helps in emergencies",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isTtsEnabled) {
            AudioTestCard(useKannada = useKannada, tts = tts)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (useKannada) "ಹೆಸರು" else "Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = bloodGroup,
                    onValueChange = { bloodGroup = it },
                    label = { Text(if (useKannada) "ರಕ್ತದ ಗುಂಪು" else "Blood Group") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = emergencyContact,
                    onValueChange = { emergencyContact = it },
                    label = { Text(if (useKannada) "ತುರ್ತು ಸಂಪರ್ಕ ಸಂಖ್ಯೆ" else "Emergency Contact") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        if (emergencyContact.isNotBlank()) {
                            IconButton(onClick = { dialNumber(context, emergencyContact) }) {
                                Icon(Icons.Default.Call, contentDescription = "Call", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            preferenceManager.saveData(name, bloodGroup, emergencyContact)
                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (useKannada) "ಉಳಿಸಿ" else "Save Details")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (useKannada) "ತುರ್ತು ಸಂಪರ್ಕಗಳು" else "Emergency Contacts",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = contactName,
                    onValueChange = { contactName = it },
                    label = { Text(if (useKannada) "ಹೆಸರು" else "Contact Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = contactPhone,
                    onValueChange = { contactPhone = it },
                    label = { Text(if (useKannada) "ದೂರವಾಣಿ ಸಂಖ್ಯೆ" else "Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (contactName.isNotBlank() && contactPhone.isNotBlank()) {
                            scope.launch {
                                preferenceManager.addEmergencyContact(contactName, contactPhone)
                                contactName = ""
                                contactPhone = ""
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (useKannada) "ಸಂಪರ್ಕ ಸೇರಿಸಿ" else "Add Contact")
                }
            }
        }

        savedContacts.sorted().forEach { contactStr ->
            val parts = contactStr.split("|", limit = 2)
            if (parts.size == 2) {
                ContactCard(
                    name = parts[0],
                    phone = parts[1],
                    onCall = { dialNumber(context, parts[1]) },
                    onDelete = { scope.launch { preferenceManager.removeEmergencyContact(contactStr) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (useKannada) "ತುರ್ತು ಪರಿಕರಗಳು" else "Emergency Tools",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileEmergencyTools(useKannada = useKannada)
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun AudioTestCard(useKannada: Boolean, tts: TextToSpeech) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (useKannada) "ಧ್ವನಿ ಪರೀಕ್ಷೆ" else "Audio Voice Test",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (useKannada) "ಧ್ವನಿ ಸ್ಪಷ್ಟತೆಯನ್ನು ಪರೀಕ್ಷಿಸಿ" else "Test TTS clarity",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Button(
                onClick = {
                    val canUseKannada = configureTtsLanguage(tts, useKannada, context)
                    val text = if (useKannada && canUseKannada) {
                        refineKannadaTTS("ನಮಸ್ಕಾರ, ಇದು ಪ್ರಥಮ ಚಿಕಿತ್ಸೆ ಆಪ್ ಪರೀಕ್ಷೆ. ಸಿ ಪಿ ಆರ್ ಮತ್ತು ಬ್ಯಾಂಡೇಜ್ ಸರಿಯಾಗಿ ಕೇಳಿಸುತ್ತಿದೆಯೇ?")
                    } else {
                        "Hello, this is a test of the Pratham Chikisthe app. Is the audio clear?"
                    }
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts-test")
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.VolumeUp, contentDescription = null)
            }
        }
    }
}

@Composable
private fun ContactCard(
    name: String,
    phone: String,
    onCall: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold)
                Text(phone, style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                IconButton(onClick = onCall) {
                    Icon(Icons.Default.Call, contentDescription = "Call", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun ProfileEmergencyTools(useKannada: Boolean) {
    val context = LocalContext.current
    val cameraManager = remember { context.getSystemService(Context.CAMERA_SERVICE) as CameraManager }
    val cameraId = remember { findFlashCameraId(cameraManager) }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted -> hasCameraPermission = isGranted }

    var isFlashlightOn by remember { mutableStateOf(false) }
    var isWhistleOn by remember { mutableStateOf(false) }
    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_ALARM, 100) }

    LaunchedEffect(isWhistleOn) {
        if (isWhistleOn) {
            try {
                while (true) {
                    toneGenerator.startTone(ToneGenerator.TONE_SUP_PIP, 500)
                    delay(650)
                }
            } finally {
                toneGenerator.stopTone()
            }
        } else {
            toneGenerator.stopTone()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (isFlashlightOn && cameraId != null) {
                runCatching { cameraManager.setTorchMode(cameraId, false) }
            }
            toneGenerator.release()
        }
    }

    fun toggleFlash() {
        if (cameraId == null) {
            Toast.makeText(context, "Flashlight is not available on this device", Toast.LENGTH_SHORT).show()
            return
        }
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            return
        }
        isFlashlightOn = !isFlashlightOn
        runCatching { cameraManager.setTorchMode(cameraId, isFlashlightOn) }
            .onFailure {
                isFlashlightOn = false
                Toast.makeText(context, "Unable to control flashlight", Toast.LENGTH_SHORT).show()
            }
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        val infiniteTransition = rememberInfiniteTransition(label = "profile_tools")
        val flashlightScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isFlashlightOn) 1.05f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "flashlight_scale"
        )
        val whistleScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isWhistleOn) 1.1f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "whistle_scale"
        )

        ProfileToolCard(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer(scaleX = flashlightScale, scaleY = flashlightScale),
            active = isFlashlightOn,
            icon = if (isFlashlightOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
            title = if (useKannada) "ಟಾರ್ಚ್" else "Flashlight",
            activeTint = Color(0xFFFFA000),
            onClick = { toggleFlash() }
        )

        ProfileToolCard(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer(scaleX = whistleScale, scaleY = whistleScale),
            active = isWhistleOn,
            icon = if (isWhistleOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
            title = if (useKannada) "ಸೀಟಿ" else "Whistle",
            activeTint = Color.Red,
            onClick = { isWhistleOn = !isWhistleOn }
        )
    }
}

@Composable
private fun ProfileToolCard(
    modifier: Modifier,
    active: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    activeTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (active) activeTint.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (active) activeTint else Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

private fun configureTtsLanguage(tts: TextToSpeech, useKannada: Boolean, context: Context): Boolean {
    if (!useKannada) {
        tts.language = Locale.US
        return false
    }
    val result = tts.setLanguage(Locale.forLanguageTag("kn-IN"))
    val canUseKannada = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
    if (!canUseKannada) {
        tts.language = Locale.US
        Toast.makeText(context, "Kannada TTS unavailable. Falling back to English audio.", Toast.LENGTH_SHORT).show()
    }
    return canUseKannada
}

private fun findFlashCameraId(cameraManager: CameraManager): String? {
    return try {
        cameraManager.cameraIdList.firstOrNull { id ->
            cameraManager.getCameraCharacteristics(id)
                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        }
    } catch (_: Exception) {
        null
    }
}

private fun dialNumber(context: Context, phone: String) {
    val cleanPhone = phone.trim()
    if (cleanPhone.isBlank()) return
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(cleanPhone)}"))
    runCatching { context.startActivity(intent) }
        .onFailure { Toast.makeText(context, "No dialer app found", Toast.LENGTH_SHORT).show() }
}

private fun openHospitalMap(context: Context, hospital: Hospital) {
    val label = Uri.encode(hospital.name)
    val uri = Uri.parse("geo:0,0?q=${hospital.lat},${hospital.lng}($label)")
    val googleMapsIntent = Intent(Intent.ACTION_VIEW, uri).setPackage("com.google.android.apps.maps")
    try {
        context.startActivity(googleMapsIntent)
    } catch (_: ActivityNotFoundException) {
        runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, uri)) }
            .onFailure { Toast.makeText(context, "No map app found", Toast.LENGTH_SHORT).show() }
    }
}
