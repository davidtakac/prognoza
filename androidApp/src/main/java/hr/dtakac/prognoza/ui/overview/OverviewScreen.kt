package hr.dtakac.prognoza.ui.overview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hr.dtakac.prognoza.presentation.overview.OverviewScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
    state: OverviewScreenState
) {
    MaterialTheme {
        Scaffold(
            topBar = {
                SearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    active = false,
                    onActiveChange = {}
                ) {
                    Text("My cool search bar!")
                }
            }
        ) {
            if (state.data != null) {
                Text(
                    text = state.data.temperature.asString(),
                    Modifier.padding(it),
                )
            }
        }
    }
}