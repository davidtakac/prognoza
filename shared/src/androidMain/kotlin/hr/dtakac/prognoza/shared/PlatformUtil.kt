package hr.dtakac.prognoza.shared

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

actual fun formatToDotDecimal(value: Double, decimalPlaces: Int): String =
    String.format(Locale.ROOT, "%.${decimalPlaces}f", value)

actual fun formatToRfc1123(epochMillis: Long): String {
    val zdt = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(epochMillis),
        ZoneOffset.UTC
    )
    return DateTimeFormatter.RFC_1123_DATE_TIME.format(zdt)
}

actual fun parseEpochMillisFromRfc1123(rfc1123: String): Long {
    return ZonedDateTime.parse(
        rfc1123,
        DateTimeFormatter.RFC_1123_DATE_TIME
    ).toInstant().toEpochMilli()
}

actual fun getLocalRfc2616LanguageCode(): String =
    Locale.getDefault().language