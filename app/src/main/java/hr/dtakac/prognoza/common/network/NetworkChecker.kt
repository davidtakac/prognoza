package hr.dtakac.prognoza.common.network

interface NetworkChecker {
    fun hasInternetConnection(): Boolean
}