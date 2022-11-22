package hr.dtakac.prognoza.shared.platform

import android.content.Context
import kotlinx.coroutines.Dispatchers

class AndroidPrognozaSdkFactory(
    context: Context,
    userAgent: String
) : PrognozaSdkFactory(
    userAgent = userAgent,
    localRfc2616LanguageGetter = LocalRfc2616LanguageGetter(),
    sqlDriverFactory = SqlDriverFactory(context),
    dotDecimalFormatter = DotDecimalFormatter(),
    rfc1123UtcDateTimeParser = Rfc1123UtcDateTimeParser(),
    ioDispatcher = Dispatchers.IO,
    computationDispatcher = Dispatchers.Default
)