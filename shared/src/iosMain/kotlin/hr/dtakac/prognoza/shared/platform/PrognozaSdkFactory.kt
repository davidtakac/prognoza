package hr.dtakac.prognoza.shared.platform

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newFixedThreadPoolContext

class IosPrognozaSdkFactory(userAgent: String) : PrognozaSdkFactory(
    userAgent = userAgent,
    localRfc2616LanguageGetter = LocalRfc2616LanguageGetter(),
    sqlDriverFactory = SqlDriverFactory(),
    dotDecimalFormatter = DotDecimalFormatter(),
    rfc1123UtcDateTimeParser = Rfc1123UtcDateTimeParser(),
    ioDispatcher = newFixedThreadPoolContext(nThreads = 64, name = "iOS IO pool"),
    computationDispatcher = Dispatchers.Default
)