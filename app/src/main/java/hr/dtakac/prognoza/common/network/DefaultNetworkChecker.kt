package hr.dtakac.prognoza.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class DefaultNetworkChecker(
    private val context: Context
) : NetworkChecker {
    override fun hasInternetConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }
}