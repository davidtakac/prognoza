package hr.dtakac.prognoza.shared.platform

import platform.Foundation.*
import kotlin.math.roundToLong

internal actual class Rfc1123UtcDateTimeParser {
    actual fun parseToEpochMillis(rfc1123String: String): Long {
        return (getRfc1123DateFormatter().dateFromString(
            rfc1123String
        )!!.timeIntervalSince1970 * 1000.0).roundToLong()
    }

    actual fun format(epochMillis: Long): String {
        return getRfc1123DateFormatter().stringFromDate(
            NSDate.dateWithTimeIntervalSince1970(epochMillis / 1000.0)
        )
    }

    private fun getRfc1123DateFormatter(): NSDateFormatter = NSDateFormatter().apply {
        locale = NSLocale("en_US_POSIX")
        dateFormat = "ddd, dd MMM yyyy HH':'mm':'ss 'GMT'"
        timeZone = NSTimeZone.timeZoneForSecondsFromGMT(seconds = 0)
    }
}