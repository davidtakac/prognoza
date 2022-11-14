package hr.dtakac.prognoza.platform

import hr.dtakac.prognoza.shared.platform.Rfc1123UtcDateTimeParser
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AndroidRfc1123UtcDateTimeParser : Rfc1123UtcDateTimeParser {
    override fun format(epochMillis: Long): String =
        DateTimeFormatter.RFC_1123_DATE_TIME.format(
            ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                ZoneOffset.UTC
            )
        )

    override fun parseToEpochMillis(rfc1123String: String): Long =
        ZonedDateTime.parse(
            rfc1123String,
            DateTimeFormatter.RFC_1123_DATE_TIME
        ).toInstant().toEpochMilli()
}