package hr.dtakac.prognoza.platform

import hr.dtakac.prognoza.shared.platform.LocalRfc2616LanguageGetter
import java.util.*

class AndroidLocalRfc2616LanguageGetter : LocalRfc2616LanguageGetter {
    override fun get(): String = Locale.getDefault().language
}