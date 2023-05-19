package com.eu.letris.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.eu.letris.ui.viewmodel.GameViewModel

@Composable
fun GameScreen(
    onGameFinished: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val gamingState by viewModel.gamingState.collectAsStateWithLifecycle()
    val selectedLetterState by viewModel.selectedLetterState.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.endGame()
        onGameFinished()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (gamingState.isGameFinished) {
            AlertDialog(
                title = {
                    Text(
                        text = "THE END 😞",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = WordBackgroundColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        text = "CHECK SCORE TABLE 🏆",
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
        PointContainer(totalPoints = gamingState.totalPoints, gamingState.errorCount)
        GameContainer(
            data = gamingState.data,
            onItemClick = { listIndex, itemIndex ->
                viewModel.selectUnselectItem(
                    listIndex,
                    itemIndex
                )
            },
            modifier = Modifier.weight(1f)
        )

        ConfirmationLine(
            onConfirmClick = {
                if (selectedLetterState.data.isNotEmpty() && selectedLetterState.data.size >= 3)
                    viewModel.checkWordFromDb()
            },
            onDeleteClick = {
                if (selectedLetterState.data.isNotEmpty())
                    viewModel.unSelectAllItems()
            },
            word = selectedLetterState.data.toWord()
        )
    }
}