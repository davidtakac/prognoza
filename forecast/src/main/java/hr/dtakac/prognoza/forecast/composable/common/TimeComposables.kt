package hr.dtakac.prognoza.forecast.composable.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import hr.dtakac.prognoza.core.formatting.formatDaySummaryTime
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import java.time.ZonedDateTime

@Composable
fun DaySummaryTime(time: ZonedDateTime) {
    Text(
        text = formatDaySummaryTime(time = time),
        style = PrognozaTheme.typography.subtitle1,
        color = PrognozaTheme.textColors.highEmphasis
    )
}