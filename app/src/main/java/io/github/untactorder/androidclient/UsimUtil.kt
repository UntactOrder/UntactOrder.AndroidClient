package io.github.untactorder.androidclient

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.*
import kotlin.collections.ArrayList

// https://gooners0304.tistory.com/entry/USIM-%EA%B4%80%EB%A6%AC-%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%B4%9D%EC%A0%95%EB%A6%AC%EC%9C%A0%EC%8B%AC-%EC%B2%B4%ED%81%AC-%EB%B2%88%ED%98%B8-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0-%EB%B2%88%ED%98%B8-%ED%8F%AC%EB%A7%B7-%ED%86%B5%EC%9D%BC

object UsimUtil {
    private const val TAG = "UsimUtil"

    // 유심 상태 체크 메소드
    @JvmStatic
    fun usimCheck(context: Context): Boolean {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return when (telephonyManager.simState) {
            TelephonyManager.SIM_STATE_UNKNOWN, // 유심 상태를 알 수 없는 경우
            TelephonyManager.SIM_STATE_ABSENT, // 유심이 없는 경우
            TelephonyManager.SIM_STATE_PERM_DISABLED, // 유심이 존재하지만, 사용중지 상태인 경우
            TelephonyManager.SIM_STATE_CARD_IO_ERROR, // 유심이 존재하지만, 오류 상태인 경우
            TelephonyManager.SIM_STATE_CARD_RESTRICTED // 유심이 존재하지만 통신사 제한으로 사용 불가 상태인 경우
            -> {
                Toast.makeText(context, "해당 단말기의 유심이 존재하지 않거나, 오류가 있습니다.", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    // 단말기 전화번호 포맷 통일 메소드
    @JvmStatic
    private fun toNationalFormat(phoneNumber: String): String {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        // 단말기의 해당 국가 코드를 가져온다.
        val locale = Locale.getDefault().country
        val toNationalNum = phoneNumberUtil.parse(phoneNumber, locale)
        return phoneNumberUtil.format(toNationalNum, PhoneNumberUtil.PhoneNumberFormat.NATIONAL).replace("-", "")
    }

    // 단말기의 전화번호를 가져오는 메소드
    @JvmStatic
    @SuppressLint("HardwareIds", "ServiceCast")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getPhoneNumber(context: Context): ArrayList<String> {
        val phoneNumberArr = ArrayList<String>()
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // 22 레벨부터는 더블 유심 지원 제공
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val subscriptionManager =
                context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            val subInfoList = subscriptionManager.activeSubscriptionInfoList
            for (subInfo in subInfoList) {
                Log.d(TAG, subInfo.number)
                if (!TextUtils.isEmpty(subInfo.number)) {
                    phoneNumberArr.add(toNationalFormat(subInfo.number))
                } else {
                    if (!TextUtils.isEmpty(telephonyManager.line1Number)) {
                        Log.d(TAG, telephonyManager.line1Number)
                        phoneNumberArr.add(toNationalFormat(telephonyManager.line1Number))
                        break
                    }
                }
            }
        } else {
            if (telephonyManager.line1Number != null) {
                Log.d(TAG, telephonyManager.line1Number)
                phoneNumberArr.add(toNationalFormat(telephonyManager.line1Number))
            }
        }
        return phoneNumberArr
    }

    // 단말기 전화번호 정규식 변환
    @JvmStatic
    fun hyphenFormat(number: String): String {
        return number.replaceFirst("(^[0-9]{3})([0-9]{3,4})([0-9]{4})$".toRegex(), "$1-$2-$3")
    }
}