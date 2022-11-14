package hr.dtakac.prognoza.shared

expect fun formatToDotDecimal(value: Double, decimalPlaces: Int): String

expect fun formatToRfc1123(epochMillis: Long): String

expect fun parseEpochMillisFromRfc1123(rfc1123: String): Long

expect fun getLocalRfc2616LanguageCode(): String