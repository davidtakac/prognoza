package hr.dtakac.prognoza.ui.forecast

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.forecast.ComingHourUi
import hr.dtakac.prognoza.presentation.forecast.DayUi
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.theme.asWeatherIconResId

@Composable
fun rememberComingItemDimensions(
    days: List<DayUi>,
    context: Context = LocalContext.current,
    density: Density = LocalDensity.current,
    fontFamilyResolver: FontFamily.Resolver = LocalFontFamilyResolver.current,
    style: TextStyle = PrognozaTheme.typography.body
): ComingItemDimensions = remember(days, context, density, fontFamilyResolver, style) {
    ComingItemDimensions(
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ComingItem(
    day: DayUi,
    dimensions: ComingItemDimensions,
    isExpanded: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val expandTransition = updateTransition(
        label = "Expand coming item",
        targetState = isExpanded
    )
    val chevronRotation by expandTransition.animateFloat(label = "Rotate chevron") {
        if (it) 180f else 0f
    }
    val verticalMargin by expandTransition.animateDp(label = "Animate vertical margin") {
        if (it) 12.dp else 0.dp
    }
    val horizontalMargin by expandTransition.animateDp(label = "Animate horizontal margin") {
        if (it) 8.dp else 0.dp
    }
    val verticalPadding by expandTransition.animateDp(label = "Animate vertical padding") {
        if (it) 16.dp else 12.dp
    }
    val horizontalPadding by expandTransition.animateDp(label = "Animate horizontal padding") {
        if (it) 16.dp else 24.dp
    }
    val cornerRadius by expandTransition.animateDp(label = "Animate corner radius") {
        if (it) 16.dp else 0.dp
    }
    val backgroundColor by expandTransition.animateColor(label = "Animate background color") {
        if (it) PrognozaTheme.colors.surface2 else Color.Transparent
    }
    Column(
        modifier = modifier
            .padding(
                vertical = verticalMargin,
                horizontal = horizontalMargin
            )
            .graphicsLayer {
                shape = RoundedCornerShape(cornerRadius)
                clip = true
            }
            .drawBehind {
                drawRect(color = backgroundColor)
            }
            .clickable(
                onClick = onClick,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(
                vertical = verticalPadding,
                horizontal = horizontalPadding
            )
    ) {
        ProvideTextStyle(PrognozaTheme.typography.body) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = day.date.asString(),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                day.precipitation.asString().takeIf { it.isNotBlank() }?.let {
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
                    modifier = Modifier.width(dimensions.lowHighTemperatureWidth),
                    text = day.lowHighTemperature.asString(),
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_expand_more),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer { rotationZ = chevronRotation },
                    colorFilter = ColorFilter.tint(LocalContentColor.current)
                )
            }
        }
        ProvideTextStyle(PrognozaTheme.typography.bodySmall) {
            expandTransition.AnimatedVisibility(
                visible = { targetIsExpanded -> targetIsExpanded },
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    itemsIndexed(day.hours) { idx, hour ->
                        ComingHourItem(
                            hour = hour,
                            modifier = when (idx) {
                                0 -> Modifier.padding(end = 8.dp)
                                day.hours.lastIndex -> Modifier.padding(start = 8.dp)
                                else -> Modifier.padding(horizontal = 8.dp)
                            }
                        )
                    }
                }
            }
        }
    }
}

data class ComingItemDimensions(
    val precipitationWidth: Dp,
    val lowHighTemperatureWidth: Dp
)

@Composable
private fun ComingHourItem(
    hour: ComingHourUi,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = hour.temperature.asString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Image(
            painter = rememberAsyncImagePainter(model = hour.weatherIconDescription.asWeatherIconResId()),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = hour.time.asString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp),
            color = LocalContentColor.current
        )
    }
}