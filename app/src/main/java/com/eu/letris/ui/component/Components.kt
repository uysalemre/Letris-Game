package com.eu.letris.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material.icons.sharp.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.eu.letris.ui.model.LetterModel
import com.eu.letris.ui.theme.*
import com.eu.letris.ui.util.isVowel

@Composable
fun LetterItem(
    letter: LetterModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = if (letter.character.isVowel()) CircleShape else RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .width(48.dp)
            .height(48.dp)
            .background(if (letter.isSelected) White else Color(letter.backgroundColor), shape)
            .border(BorderStroke(2.dp, Color(letter.backgroundColor)), shape = shape)
            .clickable { if (letter.character != 'X') onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${letter.character}",
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(align = Alignment.CenterVertically),
            color = if (letter.isSelected) Color(letter.backgroundColor) else White,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun IconifiedActionButton(
    isConfirmationButton: Boolean,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onButtonClick,
        modifier = modifier
            .width(52.dp)
            .height(52.dp)
            .background(
                White,
                CircleShape
            )
            .border(width = 2.dp, color = ColorError, CircleShape)
    ) {
        Icon(
            imageVector = if (isConfirmationButton) Icons.Sharp.Done else Icons.Sharp.Close,
            "",
            tint = if (isConfirmationButton) ConfirmBtnColor else DeleteBtnColor,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}

@Composable
fun ConfirmationLine(
    onConfirmClick: () -> Unit,
    onDeleteClick: () -> Unit,
    word: String,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (deleteBtn, textArea, confirmBtn) = createRefs()
        IconifiedActionButton(
            isConfirmationButton = false,
            onButtonClick = onDeleteClick,
            modifier = Modifier.constrainAs(deleteBtn) {
                start.linkTo(parent.start)
            }
        )
        Box(
            modifier = Modifier
                .background(White, CircleShape)
                .border(width = 2.dp, color = ColorError, CircleShape)
                .constrainAs(textArea) {
                    start.linkTo(deleteBtn.end)
                    top.linkTo(deleteBtn.top)
                    bottom.linkTo(deleteBtn.bottom)
                    end.linkTo(confirmBtn.start)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = word,
                style = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = WordBackgroundColor,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 6.sp
                ),
                maxLines = 1
            )
        }
        IconifiedActionButton(
            isConfirmationButton = true,
            onButtonClick = onConfirmClick,
            modifier = Modifier.constrainAs(confirmBtn) {
                end.linkTo(parent.end)
            }
        )
    }
}

@Composable
fun PointContainer(totalPoints: Int, errorCount: Int, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(White, CircleShape)
            .border(BorderStroke(3.dp, ColorError), CircleShape)
    ) {
        Text(
            text = "👑 $totalPoints",
            modifier = modifier
                .padding(top = 10.dp, bottom = 10.dp, start = 4.dp, end = 4.dp)
                .weight(1f),
            color = WordBackgroundColor,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
        Text(
            text = "💀 $errorCount",
            modifier = modifier
                .padding(top = 10.dp, bottom = 10.dp, start = 4.dp, end = 4.dp)
                .weight(1f),
            color = WordBackgroundColor,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GameContainer(
    data: List<List<LetterModel>>,
    onItemClick: (listIndex: Int, itemIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        data.forEachIndexed { index, letters ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                state = rememberLazyListState(),
                reverseLayout = true,
                userScrollEnabled = false
            ) {
                itemsIndexed(letters) { itemIndex, item ->
                    LetterItem(
                        letter = item,
                        onClick = { onItemClick(index, itemIndex) },
                        modifier = Modifier.padding(1.dp)
                    )
                }
            }
        }
    }
}