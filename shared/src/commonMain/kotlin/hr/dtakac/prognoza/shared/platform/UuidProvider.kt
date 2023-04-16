package hr.dtakac.prognoza.shared.platform

internal expect class UuidProvider {
    fun randomUuid(): String
}