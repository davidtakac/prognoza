package hr.dtakac.prognoza.shared.platform

interface Rfc1123UtcDateTimeParser {
    fun parseToEpochMillis(rfc1123String: String): Long
    fun format(epochMillis: Long): String
}