package com.eu.letris.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eu.letris.R
import com.eu.letris.ui.navigation.Screen
import com.eu.letris.ui.screen.GameScreen
import com.eu.letris.ui.screen.LandingScreen
import com.eu.letris.ui.screen.ScoreScreen
import com.eu.letris.ui.theme.LETRISTheme
import com.eu.letris.ui.util.ComposableLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LETRISTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.app_background),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    AppNavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun AppNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
        startDestination: String = Screen.LandingScreen.route
    ) {
        var isMusicEnabled by rememberSaveable {
            mutableStateOf(false)
        }
        val context = LocalContext.current

        val mediaPlayer: MediaPlayer = remember {
            MediaPlayer.create(context, R.raw.letris_music)
        }

        ComposableLifecycle { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                if (mediaPlayer.isPlaying)
                    isMusicEnabled = false
            }
        }

        if (isMusicEnabled) {
            mediaPlayer.prepare()
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        } else {
            mediaPlayer.stop()
        }

        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable(Screen.LandingScreen.route) {
                LandingScreen(
                    onMusicBtnClick = { isMusicEnabled = !isMusicEnabled },
                    onGameBtnClick = { navController.navigate(Screen.GameScreen.route) },
                    onScoreBtnClick = { navController.navigate(Screen.ScoreScreen.route) },
                    isMusicEnabled = isMusicEnabled
                )
            }
            composable(Screen.GameScreen.route) {
                GameScreen(onGameFinished = { navController.navigateUp() })
            }
            composable(Screen.ScoreScreen.route) {
                ScoreScreen()
            }
        }
    }
}