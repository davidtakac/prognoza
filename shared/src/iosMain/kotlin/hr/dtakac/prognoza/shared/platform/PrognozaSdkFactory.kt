package hr.dtakac.prognoza.shared.platform

import hr.dtakac.prognoza.shared.PrognozaSdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newFixedThreadPoolContext

actual class PrognozaSdkFactory(private val userAgent: String) {
    actual fun create(): PrognozaSdk = InternalPrognozaSdkFactory(
        userAgent = userAgent,
        localRfc2616LanguageGetter = LocalRfc2616LanguageGetter(),
        sqlDriverFactory = SqlDriverFactory(),
        dotDecimalFormatter = DotDecimalFormatter(),
        rfc1123UtcDateTimeParser = Rfc1123UtcDateTimeParser(),
        ioDispatcher = newFixedThreadPoolContext(nThreads = 64, name = "iOS IO pool"),
        computationDispatcher = Dispatchers.Default
    ).create()
}