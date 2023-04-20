package com.eu.letris.ui.navigation

sealed class Screen(val route: String) {
    object LandingScreen : Screen("Landing")
    object GameScreen : Screen("Game")
    object ScoreScreen : Screen("Score")
}