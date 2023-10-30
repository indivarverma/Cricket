package com.indivar.cricketapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun <T> Flow<T>.collectAsEffect(
    coroutineScope: CoroutineScope,
    context: CoroutineContext = EmptyCoroutineContext,
    block: (T) -> Unit
) {
    DisposableEffect(key1 = LocalLifecycleOwner.current) {
        val job = onEach(block).flowOn(context).launchIn(coroutineScope)
        onDispose {
            job.cancel()
        }
    }
}