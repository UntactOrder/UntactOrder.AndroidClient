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
    val listUrl = URL(BuildConfig.NETCONFIG_REGISTERED_POS_LIST)

    object : Thread() {
        override fun run() {
            var publicIp: String = ""
            var list: List<String> = emptyList()
            try {
                publicIp = ipifyUrl.readText()
                printLog("Public Ip", publicIp)
                list = listUrl.readText().replace("\r".toRegex(), "").split("\n")
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is MalformedURLException -> callback(false, "MalformedURLException")
                    is FileNotFoundException -> callback(false, "FileNotFoundException")
                    is IOException -> callback(false, "IOException")
                    else -> callback(false, "Unknown Exception")
                }
            }
            val aesKey = BuildConfig.NETCONFIG_AES128CBC_KEY
            val aesIv = BuildConfig.NETCONFIG_AES128CBC_IV
            for (line in list.subList(1, list.size)) {
                val columns = line.split(",")
                if (columns.size == 3 && columns[0] == publicIp.split(".")[3]) {
                    val public = decryptAESCBC(aesKey, aesIv, columns[1])
                    if (public == publicIp) {
                        callback(true, decryptAESCBC(aesKey, aesIv, columns[2]))
                        return
                    }
                }
            }
            callback(false, "")
        }
    }.start()
}

/**
 * AES128 복호화 (AES/CBC/PKCS5PADDING 방식)
 * 초등학생들의 무책임한 dos 공격을 방지하기 위한 공인 IP 암호화 (적당한 귀차니즘을 유발하는) - 실효성은 없음
 * @see "https://louisdev.tistory.com/52"
 */
fun decryptAESCBC(key: String, iv: String, encrypted: String): String {
    val keySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
    val ivSpec = IvParameterSpec(iv.toByteArray(charset("UTF-8")))
    val cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keySpec, ivSpec)
    return cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT)).toString(charset("UTF-8"))
}

fun encryptAESCBC(key: String, iv: String, plain: String): String {
    val keySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
    val ivSpec = IvParameterSpec(iv.toByteArray(charset("UTF-8")))
    val cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, keySpec, ivSpec)
    return Base64.encodeToString(cipher.doFinal(plain.toByteArray(charset("UTF-8"))), Base64.DEFAULT)
}
