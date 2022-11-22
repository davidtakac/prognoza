package hr.dtakac.prognoza.shared.platform

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

internal actual class Rfc1123UtcDateTimeParser {
    actual fun format(epochMillis: Long): String =
        DateTimeFormatter.RFC_1123_DATE_TIME.format(
            ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                ZoneOffset.UTC
            )
        )

    actual fun parseToEpochMillis(rfc1123String: String): Long =
        ZonedDateTime.parse(
            rfc1123String,
            DateTimeFormatter.RFC_1123_DATE_TIME
        ).toInstant().toEpochMilli()
}