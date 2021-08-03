package hr.dtakac.prognoza.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class DefaultNetworkChecker(
    private val context: Context
): NetworkChecker {
    override fun hasInternetConnection(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}