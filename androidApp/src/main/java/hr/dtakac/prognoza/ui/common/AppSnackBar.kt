package hr.dtakac.prognoza.ui.common

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import hr.dtakac.prognoza.ui.theme.AppTheme
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import kotlinx.coroutines.delay

@Composable
fun AppSnackBar(
  state: AppSnackBarState,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = state.isVisible,
    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
    modifier = modifier
  ) {
    Snackbar {
      Text(
        text = state.currentMessage,
        style = PrognozaTheme.typography.body,
        modifier = Modifier.fillMaxWidth(),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}

class AppSnackBarState(
  isVisible: Boolean,
  initialMessage: String
) {
  private val _isVisible = mutableStateOf(isVisible)
  val isVisible: Boolean by _isVisible

  private val _currentMessage = mutableStateOf(initialMessage)
  val currentMessage: String by _currentMessage

  suspend fun showSnackBar(message: String) {
    _currentMessage.value = message
    _isVisible.value = true
    delay(4000L)
    _isVisible.value = false
  }
}

@Composable
fun rememberAppSnackBarState(
  isVisible: Boolean = false,
  initialMessage: String = ""
) = remember { AppSnackBarState(isVisible, initialMessage) }

@Preview
@Composable
private fun AppSnackBarPreview() = AppTheme {
  AppSnackBar(
    state = rememberAppSnackBarState(
      isVisible = true,
      initialMessage = "An error occurred."
    )
  )
}