package hr.dtakac.prognoza.metnorwayforecastprovider

// todo: make this an actual/expect combo in kmm
interface Rfc1123Converter {
    fun toEpochMillis(rfc1123DateTime: String): Long
    fun format(epochMillis: Long): String
}