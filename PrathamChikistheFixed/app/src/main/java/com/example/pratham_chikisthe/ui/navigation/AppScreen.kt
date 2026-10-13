package com.example.pratham_chikisthe.ui.navigation

sealed class AppScreen {
    data object LanguageModal : AppScreen()
    data object Home : AppScreen()
    data object Details : AppScreen()
    data object StepMode : AppScreen()
    data object Hospitals : AppScreen()
    data object Kit : AppScreen()
    data object Profile : AppScreen()
}

