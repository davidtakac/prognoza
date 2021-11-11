package hr.dtakac.prognoza.core.network

interface NetworkChecker {
    fun hasInternetConnection(): Boolean
}