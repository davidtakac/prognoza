package hr.dtakac.prognoza.ui.common

import android.content.Context
import java.util.Locale

// TODO: replace with sophisticated Locale determination once translations are brought back in
val Context.supportedLocaleOrDefault: Locale get() = Locale.US