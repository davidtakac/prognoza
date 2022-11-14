package hr.dtakac.prognoza.ui.forecast

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.forecast.DayHourUi
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun rememberHourItemDimensions(
    hours: List<DayHourUi>,
    context: Context = LocalContext.current,
    density: Density = LocalDensity.current,
    fontFamilyResolver: FontFamily.Resolver = LocalFontFamilyResolver.current,
    style: TextStyle = PrognozaTheme.typography.body
): HourItemDimensions = remember(hours, context, density, fontFamilyResolver, style) {
    HourItemDimensions(
        timeWidth = calculateMaxWidth(
            texts = hours.map { it.time.asString(context) },
            density = density,
            fontFamilyResolver = fontFamilyResolver,
            style = style
        ),
        precipitationWidth = calculateMaxWidth(
            texts = hours.map { it.precipitation.asString(context) },
            density = density,
            fontFamilyResolver = fontFamilyResolver,
            style = style
        ),
        temperatureWidth = calculateMaxWidth(
            texts = hours.map { it.temperature.asString(context) },
            density = density,
            fontFamilyResolver = fontFamilyResolver,
            style = style
        )
    )
}

data class HourItemDimensions(
    val timeWidth: Dp,
    val precipitationWidth: Dp,
    val temperatureWidth: Dp
)

@Composable
fun HourItem(
    hour: DayHourUi,
    dimensions: HourItemDimensions,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        ProvideTextStyle(PrognozaTheme.typography.body) {
            Text(
                modifier = Modifier.width(dimensions.timeWidth),
                text = hour.time.asString(),
                textAlign = TextAlign.Start,
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = hour.description.asString(),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            hour.precipitation.asString().takeIf { it.isNotBlank() }?.let {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    modifier = Modifier.width(dimensions.precipitationWidth),
                    text = it,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                modifier = Modifier.width(dimensions.temperatureWidth),
                text = hour.temperature.asString(),
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = rememberAsyncImagePainter(model = hour.icon),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}