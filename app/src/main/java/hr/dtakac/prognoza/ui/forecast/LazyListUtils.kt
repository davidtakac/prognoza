package hr.dtakac.prognoza.ui.forecast

import androidx.compose.foundation.lazy.LazyListLayoutInfo
import kotlin.math.max

// https://stackoverflow.com/a/69267808
fun LazyListLayoutInfo.keyVisibilityPercent(key: Any): Float =
    visibleItemsInfo.firstOrNull { it.key == key }?.let {
        val cutTop = max(0, viewportStartOffset - it.offset)
        val cutBottom = max(0, it.offset + it.size - viewportEndOffset)
        max(0f, 100f - (cutTop + cutBottom) * 100f / it.size)
    } ?: 0f