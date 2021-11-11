package hr.dtakac.prognoza.core.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable

@ExperimentalAnimationApi
@Composable
fun ContentLoader(isLoading: Boolean) {
    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        CircularProgressIndicator()
    }
}