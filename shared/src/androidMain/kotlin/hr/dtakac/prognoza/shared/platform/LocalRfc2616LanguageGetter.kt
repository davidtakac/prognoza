package hr.dtakac.prognoza.shared.platform

import java.util.*

internal actual class LocalRfc2616LanguageGetter {
  actual fun get(): String = Locale.getDefault().language
}