package hr.dtakac.prognoza.ui.forecast

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun DescriptionAndPrecipitation(
    description: String,
    @DrawableRes
    icon: Int,
    precipitation: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProvideTextStyle(value = PrognozaTheme.typography.titleLarge) {
            DescriptionWithIcon(
                description = description,
                icon = icon,
                modifier = Modifier.weight(1f).alignBy(FirstBaseline)
            )
            precipitation.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .width(IntrinsicSize.Min)
                        .alignBy(FirstBaseline),
                )
            }
        }
    }
}

@Composable
private fun DescriptionWithIcon(
    description: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    val annotatedString = buildAnnotatedString {
        append("$description ")
        appendInlineContent(id = "icon")
    }
    val inlineContentMap = mapOf(
        "icon" to InlineTextContent(
            Placeholder(
                36.sp,
                36.sp,
                PlaceholderVerticalAlign.TextCenter
            )
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = icon),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    )
    Text(
        text = annotatedString,
        inlineContent = inlineContentMap,
        modifier = modifier,
    )
}