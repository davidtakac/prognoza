package hr.dtakac.prognoza

import hr.dtakac.prognoza.domain.place.Rfc2616LanguageGetter
import java.util.*

class AndroidRfc2616LanguageGetter : Rfc2616LanguageGetter {
    override fun get(): String {
        return Locale.getDefault().language
    }
}