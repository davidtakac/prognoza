package hr.dtakac.prognoza.ui.forecast

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.forecast.ComingDayHourUi
import hr.dtakac.prognoza.presentation.forecast.ComingDayUi
import hr.dtakac.prognoza.ui.common.calculateMaxWidth
import hr.dtakac.prognoza.ui.theme.AppTheme
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.theme.asWeatherIconResId

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ComingDay(
    data: ComingDayUi,
    dimensions: HeaderDimensions,
    isExpanded: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isExpandedTransition = updateTransition(
        label = "Expand coming item",
        targetState = isExpanded
    )
    val chevronRotation by isExpandedTransition.animateFloat(label = "Rotate chevron") {
        if (it) 180f else 0f
    }
    val verticalMargin by isExpandedTransition.animateDp(label = "Animate vertical margin") {
        if (it) 12.dp else 0.dp
    }
    val horizontalMargin by isExpandedTransition.animateDp(label = "Animate horizontal margin") {
        if (it) 8.dp else 0.dp
    }
    val verticalPadding by isExpandedTransition.animateDp(label = "Animate vertical padding") {
        if (it) 12.dp else 8.dp
    }
    val horizontalPadding by isExpandedTransition.animateDp(label = "Animate horizontal padding") {
        if (it) 16.dp else 24.dp
    }
    val cornerRadius by isExpandedTransition.animateDp(label = "Animate corner radius") {
        if (it) 16.dp else 0.dp
    }
    val tonalElevation by isExpandedTransition.animateDp(label = "Animate elevation") {
        if (it) 1.dp else 0.dp
    }

    Surface(
        modifier = modifier
            .padding(
                vertical = verticalMargin,
                horizontal = horizontalMargin
            ),
        tonalElevation = tonalElevation,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(size = cornerRadius),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = verticalPadding,
                horizontal = horizontalPadding
            )
        ) {
            Header(
                date = data.date.asString(),
                summaryContent = {
                    isExpandedTransition.AnimatedContent(
                        transitionSpec = {
                            if (targetState) {
                                slideInVertically { height -> -height } + fadeIn() with
                                        slideOutVertically { height -> height } + fadeOut()
                            } else {
                                slideInVertically { height -> height } + fadeIn() with
                                        slideOutVertically { height -> -height } + fadeOut()
                            }
                        }
                    ) {
                        if (it) {
                            PrecipitationAndLowHighTemperature(
                                precipitation = data.precipitation.asString(),
                                precipitationWidth = dimensions.precipitationWidth,
                                lowHighTemperature = data.lowHighTemperature.asString(),
                                lowHighTemperatureWidth = dimensions.lowHighTemperatureWidth
                            )
                        } else {
                            WeatherIcons(weatherIcons = data
                                .weatherIconDescriptions
                                .map { desc -> desc?.asWeatherIconResId() }
                            )
                        }
                    }
                },
                chevronRotation = chevronRotation,
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
            )
            isExpandedTransition.AnimatedVisibility(
                visible = { targetIsExpanded -> targetIsExpanded },
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Hours(
                    hours = data.hours,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun Header(
    date: String,
    chevronRotation: Float,
    summaryContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    ProvideTextStyle(PrognozaTheme.typography.body) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = date,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            summaryContent()
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_expand_more),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer { rotationZ = chevronRotation }
            )
        }
    }
}

@Composable
private fun PrecipitationAndLowHighTemperature(
    precipitation: String,
    precipitationWidth: Dp,
    lowHighTemperature: String,
    lowHighTemperatureWidth: Dp,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        precipitation.takeIf { it.isNotBlank() }?.let {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                modifier = Modifier.width(precipitationWidth),
                text = it,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            modifier = Modifier.width(lowHighTemperatureWidth),
            text = lowHighTemperature,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun WeatherIcons(
    weatherIcons: List<Int?>,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.Start, modifier = modifier) {
        weatherIcons.forEachIndexed { idx, icon ->
            val iconModifier = Modifier
                .padding(start = if (idx == 0) 0.dp else 4.dp)
                .height(IntrinsicSize.Max)
                .aspectRatio(1f)
            if (icon != null) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = iconModifier
                )
            } else Box(iconModifier)
        }
    }
}

@Composable
private fun Hours(
    hours: List<ComingDayHourUi>,
    modifier: Modifier = Modifier
) {
    ProvideTextStyle(PrognozaTheme.typography.bodySmall) {
        LazyRow(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
        ) {
            itemsIndexed(hours) { idx, hour ->
                Hour(
                    temperature = hour.temperature.asString(),
                    icon = hour.weatherIconDescription.asWeatherIconResId(),
                    time = hour.time.asString(),
                    modifier = when (idx) {
                        0 -> Modifier.padding(end = 8.dp)
                        hours.lastIndex -> Modifier.padding(start = 8.dp)
                        else -> Modifier.padding(horizontal = 8.dp)
                    }
                )
            }
        }
    }
}

@Composable
private fun Hour(
    temperature: String,
    @DrawableRes icon: Int,
    time: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = temperature,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Image(
            painter = rememberAsyncImagePainter(icon),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = time,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

data class HeaderDimensions(
    val precipitationWidth: Dp,
    val lowHighTemperatureWidth: Dp
)

@Composable
fun rememberHeaderDimensions(
    days: List<ComingDayUi>,
    context: Context = LocalContext.current,
    density: Density = LocalDensity.current,
    fontFamilyResolver: FontFamily.Resolver = LocalFontFamilyResolver.current,
    style: TextStyle = PrognozaTheme.typography.body
): HeaderDimensions = remember(days, context, density, fontFamilyResolver, style) {
    HeaderDimensions(
        precipitationWidth = calculateMaxWidth(
            texts = days.map { it.precipitation.asString(context) },
            density = density,
            fontFamilyResolver = fontFamilyResolver,
            style = style
        ),
        lowHighTemperatureWidth = calculateMaxWidth(
            texts = days.map { it.lowHighTemperature.asString(context) },
            density = density,
            fontFamilyResolver = fontFamilyResolver,
            style = style
        )
    )
}

@Preview
@Composable
private fun DayPreview() = AppTheme {
    ComingDay(
        data = fakeDay,
        dimensions = rememberHeaderDimensions(days = listOf(fakeDay)),
        isExpanded = true
    )
}

@Preview
@Composable
private fun HourPreview() = AppTheme {
    val fakeHour = fakeDay.hours.random()
    Hour(
        temperature = fakeHour.temperature.asString(),
        icon = fakeHour.weatherIconDescription.asWeatherIconResId(),
        time = fakeHour.time.asString()
    )
}

@Preview
@Composable
private fun HeaderPreview() = AppTheme {
    val dimensions = rememberHeaderDimensions(days = listOf(fakeDay))
    Header(
        date = fakeDay.date.asString(),
        chevronRotation = 0f,
        modifier = Modifier.height(32.dp),
        summaryContent = {
            WeatherIcons(weatherIcons = fakeDay.weatherIconDescriptions.map { it?.asWeatherIconResId() })
        }
    )
}

private val fakeDay by lazy {
    ComingDayUi(
        date = TextResource.fromString("Wed, Nov 23"),
        lowHighTemperature = TextResource.fromString("5 · 3"),
        precipitation = TextResource.fromString("0.1 mm"),
        hours = (0..24).map {
            ComingDayHourUi(
                time = TextResource.fromString("$it:00"),
                temperature = TextResource.fromString("${5 + it}°"),
                weatherIconDescription = Description.values().random()
            )
        },
        weatherIconDescriptions = listOf(
            Description.CLEAR_SKY_NIGHT,
            Description.FOG,
            Description.PARTLY_CLOUDY_DAY,
            Description.RAIN_SHOWERS_NIGHT
        )
    )
}