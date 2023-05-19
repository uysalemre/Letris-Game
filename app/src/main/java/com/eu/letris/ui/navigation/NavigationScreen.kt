package com.eu.letris.ui.navigation

sealed class NavigationScreen(val route: String) {
    object LandingScreen : NavigationScreen("Landing")
    object GameScreen : NavigationScreen("Game")
    object ScoreScreen : NavigationScreen("Score")
}