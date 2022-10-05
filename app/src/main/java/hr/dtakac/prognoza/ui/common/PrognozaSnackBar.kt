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
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import kotlinx.coroutines.delay

class PrognozaSnackBarState {
    var isVisible: Boolean = false
        private set
    
    var currentText: String = ""
        private set
    
    suspend fun showSnackBar(message: String) {
        currentText = message
        isVisible = true
        delay(5000L)
        isVisible = false
    }
}

@Composable
fun PrognozaSnackBar(
    state: PrognozaSnackBarState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = PrognozaTheme.colors.inverseSurface1,
    contentColor: Color = PrognozaTheme.colors.onInverseSurface,
) {
    AnimatedVisibility(
        visible = state.isVisible,
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
                text = state.currentText,
                style = PrognozaTheme.typography.body,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = contentColor
            )
        }
    }
}