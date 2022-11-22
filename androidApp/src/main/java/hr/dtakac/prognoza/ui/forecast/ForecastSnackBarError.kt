package hr.dtakac.prognoza.ui.forecast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import hr.dtakac.prognoza.ui.common.AppSnackBar
import hr.dtakac.prognoza.ui.common.rememberAppSnackBarState
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun ForecastSnackBarError(
    error: String,
    modifier: Modifier = Modifier
) {
    val snackBarState = rememberAppSnackBarState()
    LaunchedEffect(error) {
        snackBarState.showSnackBar(error)
    }
    AppSnackBar(
        modifier = modifier,
        state = snackBarState,
        backgroundColor = PrognozaTheme.colors.inverseSurface1,
        contentColor = PrognozaTheme.colors.onInverseSurface
    )
}