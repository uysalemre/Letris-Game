package com.eu.letris.ui.state

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.eu.letris.ui.model.LetterModel

data class GameUiState(
    val data: List<SnapshotStateList<LetterModel>> = listOf(),
    val totalPoints: Int = 0,
    val isGameFinished: Boolean = false,
    val errorCount: Int = 0
)