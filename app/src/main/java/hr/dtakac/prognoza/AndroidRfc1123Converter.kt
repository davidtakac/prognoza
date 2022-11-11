package hr.dtakac.prognoza

import hr.dtakac.prognoza.metnorwayforecastprovider.Rfc1123Converter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset
import java.time.Instant as JavaInstant

class AndroidRfc1123Converter : Rfc1123Converter {
    override fun format(epochMillis: Long): String {
        val zdt = ZonedDateTime.ofInstant(
            JavaInstant.ofEpochMilli(epochMillis),
            ZoneOffset.UTC
        )
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(zdt)
    }

    override fun toEpochMillis(rfc1123DateTime: String): Long {
        val zdt = ZonedDateTime.parse(rfc1123DateTime, DateTimeFormatter.RFC_1123_DATE_TIME)
        return zdt.toInstant().toEpochMilli()
    }
}