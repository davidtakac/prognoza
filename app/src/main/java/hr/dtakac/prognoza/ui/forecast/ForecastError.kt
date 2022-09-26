package hr.dtakac.prognoza.ui.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun ForecastError(
    text: String,
    surfaceColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_error),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                colorFilter = ColorFilter.tint(color = LocalContentColor.current.copy(alpha = 0.6f))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = text, style = PrognozaTheme.typography.subtitleMedium)
        }
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    ForecastError(text = "Test error: something went wrong.")
}