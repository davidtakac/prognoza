package hr.dtakac.prognoza.ui.common

import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

// https://stackoverflow.com/a/69267808
fun LazyListLayoutInfo.keyVisibilityPercent(key: Any): Float =
  visibleItemsInfo.firstOrNull { it.key == key }?.let {
    val cutTop = max(0, viewportStartOffset - it.offset)
    val cutBottom = max(0, it.offset + it.size - viewportEndOffset)
    max(0f, 100f - (cutTop + cutBottom) * 100f / it.size)
  } ?: 0f

// https://stackoverflow.com/a/73335483
fun calculateMaxWidth(
  texts: List<String>,
  density: Density,
  fontFamilyResolver: FontFamily.Resolver,
  style: TextStyle
): Dp = with(density) {
  texts.map {
    Paragraph(
      text = it,
      style = style,
      density = this,
      fontFamilyResolver = fontFamilyResolver,
      constraints = Constraints(maxWidth = Int.MAX_VALUE)
    )
  }.maxOfOrNull { it.getLineWidth(0) }?.toDp() ?: 0.dp
}