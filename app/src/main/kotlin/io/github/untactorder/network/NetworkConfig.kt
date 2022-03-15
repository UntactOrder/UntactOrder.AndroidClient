package io.github.untactorder.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Base64
import io.github.untactorder.BuildConfig
import io.github.untactorder.printLog
import java.io.FileNotFoundException
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URL
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


val storeNetworkCallback = object : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        printLog("Test", "wifi available")
    }

    override fun onLost(network: Network) {
        printLog("Test", "wifi unavailable")
    }
}

val commonNetworkCallback = object : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        printLog("Test", "wifi available")
    }

    override fun onLost(network: Network) {
        printLog("Test", "wifi unavailable")
    }
}

fun Context.registerNetworkCallback(networkCallback: ConnectivityManager.NetworkCallback) {
    val wifiNetworkRequest = NetworkRequest.Builder().build()
    getSystemService(ConnectivityManager::class.java).registerNetworkCallback(wifiNetworkRequest, networkCallback)
}

fun Context.unregisterNetworkCallback(networkCallback: ConnectivityManager.NetworkCallback) {
    getSystemService(ConnectivityManager::class.java).unregisterNetworkCallback(networkCallback)
}

/**
 * get the public ip address of the device and compare it to the registered PosServer ip address.
 * @see "https://stackoverflow.com/questions/6308000/determine-device-public-ip"
 * @see "https://stackoverflow.com/questions/46177133/http-request-in-android-with-kotlin"
 */
fun findPosServerAtThisNetwork(callback: (found: Boolean, data: String) -> Unit) {
    val ipifyUrl = URL(BuildConfig.NETCONFIG_PUBLIC_IP_API)
    //val listUrl = URL(BuildConfig.NETCONFIG_REGISTERED_POS_LIST)

    object : Thread() {
        override fun run() {
            var publicIp: String = ""
            var list: List<String> = emptyList()
            try {
                publicIp = ipifyUrl.readText()
                printLog("Public Ip", publicIp)
                //list = listUrl.readText().replace("\r".toRegex(), "").split("\n")
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is MalformedURLException -> callback(false, "MalformedURLException")
                    is FileNotFoundException -> callback(false, "FileNotFoundException")
                    is IOException -> callback(false, "IOException")
                    else -> callback(false, "Unknown Exception")
                }
            }
            callback(false, "")
        }
    }.start()
}
