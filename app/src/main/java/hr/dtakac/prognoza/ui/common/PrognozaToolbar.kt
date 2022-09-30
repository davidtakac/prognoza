package hr.dtakac.prognoza.ui.toolbar

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun PrognozaToolbar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    subtitle: @Composable () -> Unit = {},
    end: @Composable () -> Unit = {},
    titleVisible: Boolean = true,
    subtitleVisible: Boolean = false,
    endVisible: Boolean = false,
    backgroundColor: Color = PrognozaTheme.colors.surface2,
    contentColor: Color = LocalContentColor.current,
    windowInsets: WindowInsets = WindowInsets.statusBars
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            modifier = modifier
                .background(backgroundColor)
                .padding(windowInsets.asPaddingValues())
        ) {
            Row(
                modifier = Modifier
                    .height(90.dp)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                navigationIcon?.let {
                    it()
                    Spacer(modifier = Modifier.width(16.dp))
                }

                val enter = remember { fadeIn() + expandVertically(expandFrom = Alignment.Top) }
                val exit = remember { fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top) }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedVisibility(visible = titleVisible, enter = enter, exit = exit) {
                        ProvideTextStyle(PrognozaTheme.typography.titleMedium) {
                            title()
                        }
                    }
                    AnimatedVisibility(visible = subtitleVisible, enter = enter, exit = exit) {
                        ProvideTextStyle(PrognozaTheme.typography.subtitleMedium) {
                            subtitle()
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

                AnimatedVisibility(visible = endVisible, enter = enter, exit = exit) {
                    ProvideTextStyle(PrognozaTheme.typography.headlineSmall) {
                        end()
                    }
                }
            }
        }
    }
}