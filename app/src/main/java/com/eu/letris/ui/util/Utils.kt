package com.eu.letris.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.eu.letris.ui.model.SelectedLetterModel

fun Char.isVowel() = arrayOf('A', 'E', 'I', 'İ', 'O', 'Ö', 'U', 'Ü').contains(this)

fun List<SelectedLetterModel>.toWord(): String {
    return this.map {
        it.letter
    }.joinToString(separator = "")
}

fun List<SelectedLetterModel>.calculatePoint(): Int {
    return this.sumOf { it.point }
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
