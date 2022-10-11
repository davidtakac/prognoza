package hr.dtakac.prognoza.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun TextResource.asString(): String = asString(LocalContext.current)

@Composable
fun TextResource.asGlanceString(): String = asString(context = androidx.glance.LocalContext.current)