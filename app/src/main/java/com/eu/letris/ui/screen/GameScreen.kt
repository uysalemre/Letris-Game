package com.eu.letris.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eu.letris.ui.component.ConfirmationLine
import com.eu.letris.ui.component.GameContainer
import com.eu.letris.ui.component.IconifiedActionButton
import com.eu.letris.ui.component.PointContainer
import com.eu.letris.ui.theme.WordBackgroundColor
import com.eu.letris.ui.util.toWord
import com.eu.letris.ui.viewmodel.MainViewModel

@Composable
fun GameScreen(
    onGameFinished: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.gameItemList.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.startGame()
    }

    BackHandler {
        viewModel.endGame()
        onGameFinished()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isGameFinished) {
            AlertDialog(
                title = {
                    Text(
                        text = "Oyun Bitti 😞",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = WordBackgroundColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        text = "Skor tablosunu kontrol etmeyi unutma 🏆",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        color = WordBackgroundColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                onDismissRequest = { },
                confirmButton = {
                    IconifiedActionButton(
                        isConfirmationButton = true,
                        onButtonClick = {
                            viewModel.addPointsToDb()
                            onGameFinished()
                        }
                    )
                },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )
        }
        PointContainer(totalPoints = uiState.totalPoints, uiState.errorCount)
        GameContainer(
            data = uiState.data,
            onItemClick = { listIndex, itemIndex ->
                viewModel.selectItem(
                    listIndex,
                    itemIndex
                )
            },
            modifier = Modifier.weight(1f)
        )
        ConfirmationLine(
            onConfirmClick = {
                if (uiState.selectedCharacterList.isNotEmpty() && uiState.selectedCharacterList.size >= 3)
                    viewModel.checkWordFromDb()
            },
            onDeleteClick = {
                if (uiState.selectedCharacterList.isNotEmpty())
                    viewModel.deleteSelectedItems()
            },
            word = uiState.selectedCharacterList.toWord()
        )
    }
}