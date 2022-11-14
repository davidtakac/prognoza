package hr.dtakac.prognoza.shared

// todo: move these to classes and inject them in iosApp
/*actual fun formatToDotDecimal(value: Double, decimalPlaces: Int): String =
    NSString.stringWithFormat("%.${decimalPlaces}f")*/

/*actual fun formatToRfc1123(epochMillis: Long): String =
    NSDateFormatter.dateFormatFromTemplate(
        tmplate = "ddd, dd MMM yyyy HH':'mm':'ss 'GMT'",
        options = 0,
        locale = NSLocale("en_US_POSIX")
    )!!

actual fun parseEpochMillisFromRfc1123(rfc1123: String): Long =
    getRfc1123DateFormatter().dateFromString(rfc1123)!!.timeIntervalSince1970.toLong() / 1000*/

//actual fun getLocalRfc2616LanguageCode(): String = NSLocale.currentLocale.languageCode

/*
private fun getRfc1123DateFormatter(): NSDateFormatter = NSDateFormatter().apply {
    locale = NSLocale("en_US_POSIX")
    dateFormat = "ddd, dd MMM yyyy HH':'mm':'ss 'GMT'"
    timeZone = NSTimeZone.timeZoneForSecondsFromGMT(seconds = 0)
}*/
