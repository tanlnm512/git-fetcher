@file:JvmMultifileClass
@file:JvmName("AppExtensionsKt")

package io.tanlnm.gitfetcher.core.extension

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

// Transformer
typealias Transformer<I, O> = (I) -> O

inline fun <T> Flow<T>.collectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.CREATED,
    crossinline action: suspend (value: T) -> Unit,
): Job = this
    .flowWithLifecycle(owner.lifecycle, minActiveState)
    .onEach {
        Timber.d("Start collecting $owner $minActiveState...")
        action.invoke(it)
    }
    .launchIn(owner.lifecycleScope)

//<editor-fold desc="Single Click">
inline fun <reified T : View> T.click(time: Long = 250, crossinline block: (T) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            block(it as T)
        }
    }
}

var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else 250
    set(value) {
        setTag(1123461123, value)
    }

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else -250
    set(value) {
        setTag(1123460103, value)
    }

fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}
//</editor-fold>