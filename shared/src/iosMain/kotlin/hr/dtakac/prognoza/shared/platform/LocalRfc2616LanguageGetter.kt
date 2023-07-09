package hr.dtakac.prognoza.shared.platform

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

internal actual class LocalRfc2616LanguageGetter {
  actual fun get(): String = NSLocale.currentLocale.languageCode
}