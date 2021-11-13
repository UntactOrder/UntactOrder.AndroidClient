package io.github.untactorder.androidclient;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

public class MainActivity extends AppCompatActivity {
    boolean __DEBUG = true;

    protected void println(String tag, String data) {
        if (__DEBUG) {
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        }
        Log.d(tag, data);
    }
    protected void println(String data) {
        println("Main", data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndPermission.with(this)
                .runtime().permission(Permission.READ_PHONE_NUMBERS)
                .permission(Permission.READ_PHONE_STATE)
                .onGranted(permissions -> {
                    println("허용된 권한 개수 : "+permissions.size());
                })
                .onDenied(permissions -> {
                    println("거부된 권한 개수 : "+permissions.size());
                })
                .start();

        Window window = getWindow();
        View decorView = window.getDecorView();
        setStatusBarTextColor(this, window, decorView);

        String TAG = "info";
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        Log.d(TAG, "음성통화 상태 : [ getCallState ] >>> "+tm.getCallState());
        Log.d(TAG, "데이터통신 상태 : [ getDataState ] >>> "+tm.getDataState());
        Log.d(TAG, "IMEI : [ getDeviceId ] >>>"+tm.getDeviceId());
        Log.d(TAG, "전화번호 : [ getLine1Number ] >>> "+tm.getLine1Number());
        Log.d(TAG, "통신사 ISO 국가코드 : [ getNetworkCountryIso ] >>> "+tm.getNetworkCountryIso());
        Log.d(TAG, "통신사 ISO 국가코드 : [ getSimCountryIso ] >>> "+tm.getSimCountryIso());
        Log.d(TAG, "망사업자 MCC+MNC : [ getNetworkOperator ] >>> "+tm.getNetworkOperator());
        Log.d(TAG, "망사업자 MCC+MNC : [ getSimOperator ] >>> "+tm.getSimOperator());
        Log.d(TAG, "망사업자명 : [ getNetworkOperatorName ] >>> "+tm.getNetworkOperatorName());
        Log.d(TAG, "망사업자명 : [ getSimOperatorName ] >>> "+tm.getSimOperatorName());
        Log.d(TAG, "SIM 카드 시리얼넘버 : [ getSimSerialNumber ] >>> "+tm.getSimSerialNumber());
        Log.d(TAG, "SIM 카드 상태 : [ getSimState ] >>> "+tm.getSimState());
        Log.d(TAG, "소프트웨어 버전넘버 : [ getDeviceSoftwareVersion ] >>> "+tm.getDeviceSoftwareVersion());



        ActivityResultLauncher<Intent> consentFormLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK) finish();
                }
        );
        checkCustomerInfo(consentFormLauncher);
        //while (!checkWifiConnection());
    }

    public static void setStatusBarTextColor(Context context, Window window, View decorView) {
        /* 상단바 글자 색상 변경 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (new WindowInsetsControllerCompat(window, decorView)).setAppearanceLightStatusBars(true);
        } else {
            // API 23 미만이면 다른 상단바 색상 적용
            window.setStatusBarColor(context.getResources().getColor(R.color.gray_primary_dark));
        }
    }

    private boolean checkWifiConnection() {
        // 수정 필요
        if (true) {
            return true;
        }
        return false;
    }

    private void checkCustomerInfo(ActivityResultLauncher<Intent> launcher) {
        final String CONTAINER_NAME = "customer_info";
        final String PHONE_NUMBER = "phone_number";
        final String IMEI = "imei";

        SharedPreferences pref = getSharedPreferences(CONTAINER_NAME, Activity.MODE_PRIVATE);
        if (pref == null) {
            println("내부 저장소 접근 오류!!");
            finish();
        }

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            println("전화 권한이 없습니다!!!! 앱을 종료합니다.");
            finish();
        }
        String currentPhoneNumber = UsimUtil.hyphenFormat(UsimUtil.getPhoneNumber(this).get(0));
        println(currentPhoneNumber);
        String currentIMEI = tm.getDeviceId();  // https://jamesdreaming.tistory.com/37
        println(currentIMEI);
        try {
            if (pref.contains(PHONE_NUMBER) && pref.contains(IMEI)) {
                if (UsimUtil.usimCheck(this)) {
                    String savedPhoneNumber = pref.getString(PHONE_NUMBER, "");
                    String savedIMDI = pref.getString(IMEI, "");

                    if (!savedPhoneNumber.equals(currentPhoneNumber)) {
                        throw new IllegalStateException("폰 번호가 일치하지 않습니다.");
                    }
                    if (!savedIMDI.equals(currentIMEI)) {
                        throw new IllegalStateException("IMEI가 일치하지 않습니다.");
                    }
                } else {
                    throw new IllegalStateException("유심 확인에 실패했습니다.");
                }
            } else {
                throw new IllegalStateException("저장된 고객 정보가 없습니다. 새로 등록합니다.");
            }
        } catch (IllegalStateException e) {
            println(e.getMessage());
            showPersonalInfoConsentForm(launcher);
            ((TextView) findViewById(R.id.customerPhoneNumView)).setText(currentPhoneNumber);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(PHONE_NUMBER, currentPhoneNumber);
            editor.putString(IMEI, currentIMEI);
            editor.apply();
        }
    }
    private void showPersonalInfoConsentForm(ActivityResultLauncher<Intent> launcher) {
        launcher.launch(new Intent(this, PersonalInfoConsentFormActivity.class));
    }

}
