package com.eu.letris.ui.state

import com.eu.letris.ui.model.SelectedLetterModel

data class SelectedLetterUiState(
    val data: List<SelectedLetterModel> = listOf(),
)