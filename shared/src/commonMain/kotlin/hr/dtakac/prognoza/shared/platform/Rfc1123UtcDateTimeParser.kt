package hr.dtakac.prognoza.shared.platform

internal expect class Rfc1123UtcDateTimeParser {
    fun parseToEpochMillis(rfc1123String: String): Long
    fun format(epochMillis: Long): String
}