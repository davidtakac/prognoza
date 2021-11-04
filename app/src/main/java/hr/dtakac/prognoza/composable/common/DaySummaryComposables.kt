package hr.dtakac.prognoza.composable.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.common.utils.ComposeStringFormatting
import hr.dtakac.prognoza.common.utils.shouldShowPrecipitation
import hr.dtakac.prognoza.model.ui.MeasurementUnit
import hr.dtakac.prognoza.model.ui.RepresentativeWeatherDescription
import hr.dtakac.prognoza.theme.AppTheme

@Composable
fun TotalPrecipitation(
    totalPrecipitation: Double?,
    unit: MeasurementUnit
) {
    Text(
        text = if (shouldShowPrecipitation(totalPrecipitation)) {
            ComposeStringFormatting.formatPrecipitationValue(
                precipitation = totalPrecipitation,
                unit = unit
            )
        } else {
            AnnotatedString(text = stringResource(id = R.string.placeholder_precipitation_text))
        },
        style = AppTheme.typography.subtitle1,
        color = AppTheme.textColors.mediumEmphasis
    )
}

@Composable
fun RepresentativeWeatherDescription(
    representativeWeatherDescription: RepresentativeWeatherDescription?
) {
    Text(
        text = ComposeStringFormatting.formatRepresentativeWeatherIconDescription(
            representativeWeatherDescription = representativeWeatherDescription
        ),
        style = AppTheme.typography.subtitle1,
        color = AppTheme.textColors.mediumEmphasis
    )
}