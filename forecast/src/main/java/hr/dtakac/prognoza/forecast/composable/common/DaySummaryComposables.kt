package hr.dtakac.prognoza.forecast.composable.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import hr.dtakac.prognoza.core.formatting.ComposeStringFormatting
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.model.ui.RepresentativeWeatherDescription
import hr.dtakac.prognoza.core.utils.shouldShowPrecipitation
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.forecast.R

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
        style = PrognozaTheme.typography.subtitle1,
        color = PrognozaTheme.textColors.mediumEmphasis
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
        style = PrognozaTheme.typography.subtitle1,
        color = PrognozaTheme.textColors.mediumEmphasis
    )
}