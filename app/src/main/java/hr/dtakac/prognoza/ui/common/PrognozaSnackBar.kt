package hr.dtakac.prognoza.ui.common

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun PrognozaSnackBar(
    text: String,
    visible: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = PrognozaTheme.colors.inverseSurface1,
    contentColor: Color = PrognozaTheme.colors.onInverseSurface,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = text,
                style = PrognozaTheme.typography.body,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = contentColor
            )
        }
    }
}

@Preview
@Composable
private fun LightPreview() = PrognozaTheme(useDarkTheme = false) {
    PrognozaSnackBar(text = "An error occurred. Please try again later.", visible = true)
}

@Preview
@Composable
private fun DarkPreview() = PrognozaTheme(useDarkTheme = true) {
    PrognozaSnackBar(text = "An error occurred. Please try again later.", visible = true)
}