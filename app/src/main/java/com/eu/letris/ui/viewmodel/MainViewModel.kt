package com.eu.letris.ui.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eu.letris.data.MainRepository
import com.eu.letris.ui.model.Letter
import com.eu.letris.ui.model.SelectedLetter
import com.eu.letris.ui.theme.*
import com.eu.letris.ui.util.calculatePoint
import com.eu.letris.ui.util.toWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameUiState(
    val data: List<SnapshotStateList<Letter>> = listOf(),
    val selectedCharacterList: List<SelectedLetter> = listOf(),
    val totalPoints: Int = 0,
    val isGameFinished: Boolean = false,
    val errorCount: Int = 0,
    val bestScores: List<Int> = listOf()
)

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val vowelAlphabetList = listOf(
        Letter(character = 'A', ColorA, 1),
        Letter(character = 'E', ColorE, 1),
        Letter(character = 'I', ColorI, 2),
        Letter(character = 'İ', ColorIi, 1),
        Letter(character = 'O', ColorO, 2),
        Letter(character = 'Ö', ColorOo, 7),
        Letter(character = 'U', ColorU, 2),
        Letter(character = 'Ü', ColorUu, 3)
    )
    private val normalAlphabetList = listOf(
        Letter(character = 'B', ColorB, 3),
        Letter(character = 'C', ColorC, 4),
        Letter(character = 'Ç', ColorCc, 4),
        Letter(character = 'D', ColorD, 3),
        Letter(character = 'F', ColorF, 7),
        Letter(character = 'G', ColorG, 5),
        Letter(character = 'Ğ', ColorGg, 8),
        Letter(character = 'H', ColorH, 5),
        Letter(character = 'J', ColorJ, 10),
        Letter(character = 'K', ColorK, 1),
        Letter(character = 'L', ColorL, 1),
        Letter(character = 'M', ColorM, 2),
        Letter(character = 'N', ColorN, 1),
        Letter(character = 'P', ColorP, 5),
        Letter(character = 'R', ColorR, 1),
        Letter(character = 'S', ColorS, 2),
        Letter(character = 'Ş', ColorSs, 4),
        Letter(character = 'T', ColorT, 1),
        Letter(character = 'V', ColorV, 7),
        Letter(character = 'Y', ColorY, 3),
        Letter(character = 'Z', ColorZ, 4)
    )
    private val errorAlphabetList = listOf(
        Letter(character = 'X', ColorError, 0),
    )

    private var tickerSender: ReceiveChannel<Unit>? = null
    private val _tickerDelay: MutableStateFlow<Int> = MutableStateFlow(5000)

    private val _gameItemList: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState())
    val gameItemList get() = _gameItemList.asStateFlow()

    fun startGame() {
        generateFirstThreeLines()
        checkTotalPointChanges()
    }

    fun endGame() {
        tickerSender?.cancel()
        tickerSender = null
    }

    private fun generateFirstThreeLines() {
        viewModelScope.launch {
            val firstTreeLineList = (
                    vowelAlphabetList
                        .asSequence()
                        .shuffled()
                        .take(5)
                        .plus(
                            normalAlphabetList
                                .asSequence()
                                .shuffled()
                                .take(19)
                        ).toList()
                    )
                .shuffled()
                .chunked(3)
                .map {
                    it.toCollection(SnapshotStateList())
                }

            _gameItemList.update {
                it.copy(
                    data = firstTreeLineList
                )
            }
        }
    }

    fun selectItem(listIndex: Int, itemIndex: Int) {
        viewModelScope.launch {
            val columnList = _gameItemList.value.data
            val characterList = _gameItemList.value.selectedCharacterList.toMutableList()
            val column = columnList[listIndex]
            column[itemIndex] = column[itemIndex].copy(isSelected = !column[itemIndex].isSelected)
            when (column[itemIndex].isSelected) {
                true -> characterList.add(
                    SelectedLetter(
                        letter = column[itemIndex],
                        columnIndex = listIndex,
                        itemIndex = itemIndex
                    )
                )
                false -> characterList.remove(characterList.find { it.itemIndex == itemIndex && it.columnIndex == listIndex })
            }
            _gameItemList.update {
                it.copy(
                    data = columnList,
                    selectedCharacterList = characterList
                )
            }
        }
    }

    fun deleteSelectedItems() {
        viewModelScope.launch {
            val columnList = _gameItemList.value.data
            columnList.forEach {
                it.map {
                    it.isSelected = false
                }
            }
            _gameItemList.update {
                it.copy(
                    data = columnList,
                    selectedCharacterList = listOf()
                )
            }
        }
    }

    fun checkWordFromDb() {
        viewModelScope.launch {
            repository.checkWordIsExists(_gameItemList.value.selectedCharacterList.toWord())
                .collect {
                    val columnList = _gameItemList.value.data
                    if (it != null) {
                        val totalPoints =
                            _gameItemList.value.totalPoints + _gameItemList.value.selectedCharacterList.calculatePoint()
                        columnList.map { letters ->
                            letters.removeIf { it.isSelected }
                        }
                        when {
                            totalPoints in 100..199 && _tickerDelay.value == 5000 -> _tickerDelay.emit(
                                4000
                            )
                            totalPoints in 200..299 && _tickerDelay.value == 4000 -> _tickerDelay.emit(
                                3000
                            )
                            totalPoints in 300..399 && _tickerDelay.value == 3000 -> _tickerDelay.emit(
                                2000
                            )
                            totalPoints in 400..Int.MAX_VALUE && _tickerDelay.value == 2000 -> _tickerDelay.emit(
                                1000
                            )
                        }
                        _gameItemList.update { state ->
                            state.copy(
                                totalPoints = totalPoints,
                                data = columnList,
                                selectedCharacterList = listOf()
                            )
                        }
                    } else {
                        deleteSelectedItems()
                        if (_gameItemList.value.errorCount >= 2) {
                            columnList.map { letters ->
                                letters.add(errorAlphabetList[0])
                            }
                            _gameItemList.update { state ->
                                state.copy(
                                    data = columnList,
                                    errorCount = 0
                                )
                            }
                        } else {
                            _gameItemList.update { state ->
                                state.copy(
                                    errorCount = state.errorCount + 1
                                )
                            }
                        }
                    }
                }
        }
    }

    fun addPointsToDb() {
        if (_gameItemList.value.totalPoints > 0) {
            viewModelScope.launch {
                repository.addPoint(_gameItemList.value.totalPoints).collect()
            }
        }
    }

    fun getBestScores() {
        viewModelScope.launch {
            repository.getTopPointList().collect { scores ->
                _gameItemList.update { uiState ->
                    uiState.copy(
                        bestScores = scores.map { it.score }
                    )
                }
            }
        }
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    fun setTickerSender(delayMillis: Long) {
        viewModelScope.launch {
            tickerSender?.cancel()
            tickerSender = ticker(delayMillis = delayMillis, context = this.coroutineContext)
            tickerSender?.consumeEach {
                val columnList = _gameItemList.value.data
                columnList.random().add(
                    if ((0..1).random() == 0)
                        vowelAlphabetList.random()
                    else
                        normalAlphabetList.random()
                )
                _gameItemList.update {
                    it.copy(
                        data = columnList
                    )
                }
                if (columnList.any { it.size >= 10 }) {
                    tickerSender?.cancel()
                    _gameItemList.update {
                        it.copy(
                            isGameFinished = true
                        )
                    }
                }
            }
        }
    }

    private fun checkTotalPointChanges() {
        viewModelScope.launch {
            _tickerDelay.collect {
                setTickerSender(it.toLong())
            }
        }
    }
}