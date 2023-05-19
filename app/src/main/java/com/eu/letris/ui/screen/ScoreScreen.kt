package com.eu.letris.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eu.letris.ui.theme.ColorError
import com.eu.letris.ui.theme.White
import com.eu.letris.ui.theme.WordBackgroundColor
import com.eu.letris.ui.viewmodel.ScoresViewModel

@Composable
fun ScoreScreen(modifier: Modifier = Modifier, scoresViewModel: ScoresViewModel = hiltViewModel()) {
    val scores by scoresViewModel.scoresState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🏆 BEST SCORES 🏆",
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            color = WordBackgroundColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(White, CircleShape)
                .border(BorderStroke(3.dp, ColorError), CircleShape)
                .wrapContentHeight()
                .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            userScrollEnabled = true
        ) {
            itemsIndexed(scores) { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${
                            when (index.plus(1)) {
                                1 -> "🥇"
                                2 -> "🥈"
                                3 -> "🥉"
                                else -> index.plus(1)
                            }
                        }",
                        modifier = Modifier
                            .width(64.dp)
                            .height(48.dp)
                            .background(White, CircleShape)
                            .border(BorderStroke(3.dp, ColorError), CircleShape)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        color = WordBackgroundColor
                    )
                    Text(
                        text = "$item",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(White, CircleShape)
                            .border(BorderStroke(3.dp, ColorError), CircleShape)
                            .padding(start = 24.dp, top = 8.dp, bottom = 8.dp, end = 24.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        color = WordBackgroundColor,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
