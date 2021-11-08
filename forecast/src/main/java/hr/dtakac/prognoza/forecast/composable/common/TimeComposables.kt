package hr.dtakac.prognoza.forecast.composable.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import hr.dtakac.prognoza.core.formatting.ComposeStringFormatting
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import java.time.ZonedDateTime

@Composable
fun DaySummaryTime(time: ZonedDateTime) {
    Text(
        text = ComposeStringFormatting.formatDaySummaryTime(time = time),
        style = PrognozaTheme.typography.subtitle1,
        color = PrognozaTheme.textColors.highEmphasis
    )
}