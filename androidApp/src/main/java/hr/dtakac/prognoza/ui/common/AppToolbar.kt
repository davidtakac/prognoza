package hr.dtakac.prognoza.ui.common

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.ui.theme.AppTheme
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun AppToolbar(
  modifier: Modifier = Modifier,
  state: AppToolbarState = rememberAppToolbarState(),
  title: @Composable () -> Unit = {},
  navigation: (@Composable () -> Unit)? = null,
  subtitle: @Composable () -> Unit = {},
  end: @Composable () -> Unit = {},
  windowInsets: WindowInsets = WindowInsets.statusBars
) {
  Surface(
    modifier = modifier,
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp
  ) {
    Content(
      title = title,
      navigation = navigation,
      subtitle = subtitle,
      end = end,
      isTitleVisible = state.isTitleVisible,
      isSubtitleVisible = state.isSubtitleVisible,
      isEndVisible = state.isEndVisible,
      modifier = Modifier
        .padding(windowInsets.asPaddingValues())
        .height(90.dp)
        .padding(horizontal = 24.dp)
        .fillMaxWidth()
    )
  }
}

@Composable
private fun Content(
  title: @Composable () -> Unit,
  navigation: (@Composable () -> Unit)?,
  subtitle: @Composable () -> Unit,
  end: @Composable () -> Unit,
  isTitleVisible: Boolean,
  isSubtitleVisible: Boolean,
  isEndVisible: Boolean,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    navigation?.let {
      it()
      Spacer(modifier = Modifier.width(16.dp))
    }
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight(),
      verticalArrangement = Arrangement.Center
    ) {
      AnimatedComponent(
        content = title,
        isVisible = isTitleVisible,
        textStyle = PrognozaTheme.typography.titleMedium
      )
      AnimatedComponent(
        content = subtitle,
        isVisible = isSubtitleVisible,
        textStyle = PrognozaTheme.typography.subtitleMedium
      )
    }
    Spacer(modifier = Modifier.width(16.dp))
    AnimatedComponent(
      content = end,
      isVisible = isEndVisible,
      textStyle = PrognozaTheme.typography.headlineSmall
    )
  }
}

@Composable
private fun AnimatedComponent(
  content: @Composable () -> Unit,
  isVisible: Boolean,
  textStyle: TextStyle
) {
  val enter = remember { fadeIn() + expandVertically(expandFrom = Alignment.Top) }
  val exit = remember { fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top) }
  AnimatedVisibility(
    visible = isVisible,
    enter = enter,
    exit = exit
  ) {
    ProvideTextStyle(textStyle) { content() }
  }
}

class AppToolbarState(
  isTitleVisible: Boolean,
  isSubtitleVisible: Boolean,
  isEndVisible: Boolean
) {
  private val _isTitleVisible = mutableStateOf(isTitleVisible)
  val isTitleVisible: Boolean by _isTitleVisible

  private val _isSubtitleVisible = mutableStateOf(isSubtitleVisible)
  val isSubtitleVisible: Boolean by _isSubtitleVisible

  private val _isEndVisible = mutableStateOf(isEndVisible)
  val isEndVisible: Boolean by _isEndVisible

  fun setTitleVisible(isVisible: Boolean) {
    _isTitleVisible.value = isVisible
  }

  fun setSubtitleVisible(isVisible: Boolean) {
    _isSubtitleVisible.value = isVisible
  }

  fun setEndVisible(isVisible: Boolean) {
    _isEndVisible.value = isVisible
  }
}

@Composable
fun rememberAppToolbarState(
  isTitleVisible: Boolean = false,
  isSubtitleVisible: Boolean = false,
  isEndVisible: Boolean = false
): AppToolbarState = remember {
  AppToolbarState(isTitleVisible, isSubtitleVisible, isEndVisible)
}

@Composable
private fun ToolbarPreview(
  titleVisible: Boolean = true,
  subtitleVisible: Boolean = true,
  endVisible: Boolean = true,
  navVisible: Boolean = true
) = AppTheme {
  AppToolbar(
    title = { Text("Tenja") },
    navigation = if (!navVisible) {
      null
    } else {
      {
        IconButton(onClick = {}) {
          Icon(
            painter = painterResource(id = R.drawable.ic_menu),
            contentDescription = null
          )
        }
      }
    },
    subtitle = { Text("September 29") },
    end = { Text("23") },
    state = AppToolbarState(titleVisible, subtitleVisible, endVisible)
  )
}

@Preview
@Composable
private fun AllVisiblePreview() = ToolbarPreview()

@Preview
@Composable
private fun EndGonePreview() = ToolbarPreview(endVisible = false)

@Preview
@Composable
private fun TitleVisiblePreview() = ToolbarPreview(
  endVisible = false,
  subtitleVisible = false
)

@Preview
@Composable
private fun NoneVisiblePreview() = ToolbarPreview(
  endVisible = false,
  subtitleVisible = false,
  titleVisible = false
)

@Preview
@Composable
private fun NavGonePreview() = ToolbarPreview(navVisible = false)