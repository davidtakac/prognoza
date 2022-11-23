package hr.dtakac.prognoza.ui.places

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun PlacesLoadingUnderline(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Crossfade(targetState = isLoading, modifier = modifier) {
        if (it) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = PrognozaTheme.colors.onSurface,
                trackColor = Color.Transparent
            )
        } else {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = PrognozaTheme.colors.onSurface,
                trackColor = Color.Transparent,
                progress = 1f
            )
        }
    }
}