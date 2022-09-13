package hr.dtakac.prognoza.ui.today

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

// Credit: https://medium.com/tech-takeaways/responsive-auto-resizing-text-with-jetpack-compose-b8238aaf0e09
@Composable
fun ResponsiveText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = LocalContentColor.current,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = LocalTextStyle.current,
    targetHeight: TextUnit = style.fontSize,
    maxLines: Int = 1,
) {
    var textSize by remember { mutableStateOf(targetHeight) }
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textAlign = textAlign,
        fontSize = textSize,
        fontFamily = style.fontFamily,
        fontStyle = style.fontStyle,
        fontWeight = style.fontWeight,
        lineHeight = style.lineHeight,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1
            if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                textSize = textSize.times(0.9f)
            }
        }
    )
}