package hr.dtakac.prognoza.shared.platform

import android.content.Context
import hr.dtakac.prognoza.shared.PrognozaSdk
import kotlinx.coroutines.Dispatchers

actual class PrognozaSdkFactory(
  private val context: Context,
  private val userAgent: String
) {
  actual fun create(): PrognozaSdk = InternalPrognozaSdkFactory(
    userAgent = userAgent,
    localRfc2616LanguageGetter = LocalRfc2616LanguageGetter(),
    dotDecimalFormatter = DotDecimalFormatter(),
    rfc1123UtcDateTimeParser = Rfc1123UtcDateTimeParser(),
    ioDispatcher = Dispatchers.IO,
    computationDispatcher = Dispatchers.Default
  ).create()
}