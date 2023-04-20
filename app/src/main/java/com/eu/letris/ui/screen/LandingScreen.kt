package com.eu.letris.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eu.letris.R
import com.eu.letris.ui.theme.ColorError
import com.eu.letris.ui.theme.White

@Composable
fun LandingScreen(
    onMusicBtnClick: () -> Unit,
    onScoreBtnClick: () -> Unit,
    onGameBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
    isMusicEnabled: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.letris),
            contentDescription = "",
            contentScale = ContentScale.Fit,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onScoreBtnClick,
                shape = CircleShape,
                modifier = Modifier
                    .size(100.dp)
                    .border(BorderStroke(3.dp, ColorError), shape = CircleShape),
                colors = ButtonDefaults.buttonColors(White, White),
            ) {
                Text(
                    text = "🏆",
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            Button(
                onClick = onGameBtnClick,
                shape = CircleShape,
                modifier = Modifier
                    .size(100.dp)
                    .border(BorderStroke(3.dp, ColorError), shape = CircleShape),
                colors = ButtonDefaults.buttonColors(White, White)
            ) {
                Text(
                    text = "🏁",
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onMusicBtnClick,
                shape = CircleShape,
                modifier = Modifier
                    .size(100.dp)
                    .border(BorderStroke(3.dp, ColorError), shape = CircleShape),
                colors = ButtonDefaults.buttonColors(White, White),
            ) {
                Text(
                    text = if (isMusicEnabled) "🔔" else "🔕",
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}