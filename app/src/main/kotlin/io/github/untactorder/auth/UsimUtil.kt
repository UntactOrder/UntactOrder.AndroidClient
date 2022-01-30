package io.github.untactorder.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.i18n.phonenumbers.PhoneNumberUtil
import io.github.untactorder.printLog
import java.util.*
import kotlin.collections.ArrayList


/**
 * 아래 블로그 출처, Android ADK 24 이상에서 동작함
 * @see "https://gooners0304.tistory.com/entry/USIM-%EA%B4%80%EB%A6%AC-%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%B4%9D%EC%A0%95%EB%A6%AC%EC%9C%A0%EC%8B%AC-%EC%B2%B4%ED%81%AC-%EB%B2%88%ED%98%B8-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0-%EB%B2%88%ED%98%B8-%ED%8F%AC%EB%A7%B7-%ED%86%B5%EC%9D%BC"
 * @see "https://codechacha.com/ko/android-how-to-get-phone-number/"
 * @see "https://it-highjune.tistory.com/18"
 */
class UsimUtil(val context: Context) {
    private val TAG = javaClass.simpleName

    /**
     * getting permission
     * @see "https://develop-writing.tistory.com/40"
     *
     */
    companion object {
        // 사용자의 안드로이드 버전에 따라 권한을 다르게 요청 | 안드로이드 11 이상 or 10 이하
        var mRequiredPermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Manifest.permission.READ_PHONE_NUMBERS
            else Manifest.permission.READ_PHONE_STATE

        fun Context.isUsimPermissionGranted(): Boolean =
            ActivityCompat.checkSelfPermission(this, mRequiredPermission) == PackageManager.PERMISSION_GRANTED

        fun Activity.requestUsimPermission(REQUEST_CODE: Int) {
            ActivityCompat.requestPermissions(this, arrayOf(mRequiredPermission), REQUEST_CODE)
        }
    }

    init {
        if (!context.isUsimPermissionGranted()) {
            throw RuntimeException("Permission is not granted")
        }
    }

    fun isUsimEnabled(): Boolean {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return when (telephonyManager.simState) {
            TelephonyManager.SIM_STATE_READY -> true
            else -> false
        }
    }

    @SuppressLint("MissingPermission")
    fun getLine1Number(): String = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).line1Number

    fun getFormattedLine1Number(national: Boolean = false, digitsOnly: Boolean = true): String =
        getLine1Number().toFormattedPhoneNumber(national, digitsOnly)

    @SuppressLint("MissingPermission")
    private fun getSubInfoList(): List<SubscriptionInfo> =
        (context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager).activeSubscriptionInfoList

    private fun getPhoneNumberList(subInfoList: List<SubscriptionInfo>): ArrayList<String> {
        val phoneNumbers = ArrayList<String>()

        for (subInfo in subInfoList) {
            phoneNumbers.add(subInfo.number)
        }
        return phoneNumbers
    }

    fun getFormattedPhoneNumberList(national: Boolean = false, digitsOnly: Boolean = true): List<String> {
        val subInfoList = getSubInfoList()
        printLog(TAG, "subInfoList.size: ${subInfoList.size}")
        return getPhoneNumberList(subInfoList).map { it.toFormattedPhoneNumber(national, digitsOnly) }
    }
}

/**
 * @usage "010-1234-5678".toFormattedPhoneNumber()
 * @usage if improper phone number, return empty string
 * @see "https://sonagiya.tistory.com/entry/Android-%EC%A0%84%ED%99%94%EB%B2%88%ED%98%B8-%ED%8C%8C%EC%8B%B1%ED%95%98%EA%B8%B0PhoneNumberUtil"
 */
fun String.toFormattedPhoneNumber(national: Boolean = false, digitsOnly: Boolean = true): String {
    return try {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val locale = Locale.getDefault().country
        val targetNum = phoneNumberUtil.parse(this, locale)
        val formatted = phoneNumberUtil.format(targetNum,
            if (national) PhoneNumberUtil.PhoneNumberFormat.NATIONAL else PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
        if (digitsOnly) (if (national) "" else "+") +  PhoneNumberUtil.normalizeDigitsOnly(formatted) else formatted
    } catch(e: Exception) {
        Log.e("StringExt:toFormattedPhoneNumber", e.toString())
        ""  // if error occurred, return empty string
    }
}
