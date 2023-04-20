package com.eu.letris.ui.util

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.eu.letris.ui.model.SelectedLetter

fun Char.isVowel() = arrayOf('A', 'E', 'I', 'İ', 'O', 'Ö', 'U', 'Ü').find { this == it } != null

fun List<SelectedLetter>.toWord(): String {
    return this.map {
        it.letter.character
    }.joinToString(separator = "")
}

fun List<SelectedLetter>.calculatePoint(): Int {
    return this.sumOf { it.letter.point }
}

@Composable
fun ComposableLifecycle(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
