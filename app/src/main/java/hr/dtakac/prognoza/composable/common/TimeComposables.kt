package hr.dtakac.prognoza.composable.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import hr.dtakac.prognoza.common.utils.ComposeStringFormatting
import hr.dtakac.prognoza.theme.AppTheme
import java.time.ZonedDateTime

@Composable
fun DaySummaryTime(time: ZonedDateTime) {
    Text(
        text = ComposeStringFormatting.formatDaySummaryTime(time = time),
        style = AppTheme.typography.subtitle1,
        color = AppTheme.textColors.highEmphasis
    )
}