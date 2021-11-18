package io.github.untactorder.androidclient;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.Objects;

import static com.google.android.material.internal.ViewUtils.removeOnGlobalLayoutListener;
import static io.github.untactorder.androidclient.PasswordInputActivity.RESULT_INCORRECT;

public class MainActivity<resId> extends AppCompatActivity {
    String TAG = "UntactOrder.main";
    boolean __DEBUG = true;

    protected void println(String tag, String data, boolean showToast) {
        if (showToast) {
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        }
        Log.d(tag, data);
    }
    protected void println(String data) {
        println(TAG, data, __DEBUG);
    }

    String userIMEI, userPhoneNumber;
    ActivityResultLauncher<Intent> qrScanActivityLauncher, passwordInputActivityLauncher;
    ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_PHONE_STATE)
                .onGranted(permissions -> {
                    println("허용된 권한 개수 : " + permissions.size());
                })
                .onDenied(permissions -> {
                    println("거부된 권한 개수 : " + permissions.size());
                    finish();
                })
                .start();

        /* IMEI 관련 부분은 비활성화 해두고 나중에 할거
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        userIMEI = tm.getImei();
        println("IMEI:"+userIMEI);
        userPhoneNumber = tm.getLine1Number();
        println("Phone:"+userPhoneNumber);
        if (tm.getSimState() != SIM_STATE_READY) {
            println("SimState: Sim not ready");
            finish();
        }

        ActivityResultLauncher<Intent> consentFormLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK) finish();
                }
        );
        //checkCustomerInfo(consentFormLauncher);
        //while (!checkWifiConnection());
         */

        qrScanActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        String qrData = Objects.requireNonNull(result.getData()).getStringExtra("value");
                        println(qrData);
                        if (true /*QR 검사*/) {
                            runPasswordActivity();
                        } else {
                            println(TAG, getString(R.string.at_qrsc_invalid_msg), true);
                        }
                    } else {
                        println(TAG, getString(R.string.at_qrsc_cancel_msg), true);
                    }
                }
        );

        passwordInputActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    switch (result.getResultCode()) {
                        case RESULT_OK:
                            String pw = Objects.requireNonNull(result.getData()).getStringExtra("password");
                            if (pw != null) {
                                println("Password: " + pw);
                            }
                            break;
                        case RESULT_CANCELED:
                            println("Canceled");
                            break;
                        case RESULT_INCORRECT:
                            println("Incorrect password!");
                            break;
                    }
                }
        );

        findViewById(R.id.main_container_top).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int top = findViewById(R.id.main_container_top).getLayoutParams().height;
            println(""+top);
        });

        findViewById(R.id.main_container_bottom).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int bottom = findViewById(R.id.main_container_bottom).getLayoutParams().height;
            println(""+bottom);
        });

        findViewById(R.id.main_iv_linear_logo).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int logo = findViewById(R.id.main_iv_linear_logo).getLayoutParams().height;
            println(""+logo);
        });

        findViewById(R.id.main_tv_phone_number).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int pNum = findViewById(R.id.main_tv_phone_number).getLayoutParams().height;
            println(""+pNum);
        });

        findViewById(R.id.main_bt_guide_top_corner).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int topCorner = findViewById(R.id.main_bt_guide_top_corner).getLayoutParams().height;
            println(""+topCorner);
        });

        findViewById(R.id.main_bt_guide_top).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int topGuide = findViewById(R.id.main_bt_guide_top).getLayoutParams().height;
            println(""+topGuide);
        });

        findViewById(R.id.main_line_detailed_guide_margin).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int detail = findViewById(R.id.main_line_detailed_guide_margin).getLayoutParams().height;
            println(""+detail);
        });

        findViewById(R.id.main_container_bottom).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int bottomContainer = findViewById(R.id.main_container_bottom).getLayoutParams().height;
            println(""+bottomContainer);
        });

        findViewById(R.id.main_list_of_orders).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int list = findViewById(R.id.main_list_of_orders).getLayoutParams().height;
            println(""+list);
        });
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
            println(TAG, "내부 저장소 접근 오류!!", true);
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
            println(TAG, "전화 권한이 없습니다!!!! 앱을 종료합니다.", true);
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
            ((TextView) findViewById(R.id.main_tv_phone_number)).setText(currentPhoneNumber);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(PHONE_NUMBER, currentPhoneNumber);
            editor.putString(IMEI, currentIMEI);
            editor.apply();
        }
    }

    private void showPersonalInfoConsentForm(ActivityResultLauncher<Intent> launcher) {
        launcher.launch(new Intent(this, PersonalInfoConsentFormActivity.class));
    }


    public void fitOrderSeparatorSize() {
        View bottom = findViewById(R.id.main_container_bottom);
        View separator = findViewById(R.id.main_line_order_separator);

        int top = findViewById(R.id.main_container_top).getLayoutParams().height;
        int list = findViewById(R.id.main_list_of_orders).getLayoutParams().height;
        int newOrder = findViewById(R.id.main_bt_new_order_body).getLayoutParams().height;
        int total = findViewById(R.id.main_tv_total_price_body).getLayoutParams().height;

        int height = getResources().getDisplayMetrics().heightPixels;
        int dpi = getResources().getDisplayMetrics().densityDpi;

        int statusBarHeight = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resId);
        }

        int naviId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        int naviBar = (naviId > 0) ? getResources().getDimensionPixelSize(resId) : 0;

        println(""+(height*160/dpi-naviBar));
        //println(""+top);
        //println(""+list);
        //println(""+newOrder);
        //println(""+total);
        //println(""+total+top);
        bottom.getLayoutParams().height = (height*160/dpi-naviBar)-top;
        separator.getLayoutParams().height = 300;
    }

    boolean isGuideShowing = false;
    public void onGuideButtonClicked(View v) {
        View detailedGuide = findViewById(R.id.main_bt_detailed_guide);
        View container = findViewById(R.id.main_container_bottom);

        Animation anim;
        if (isGuideShowing) {  // 이미 도움 말이 보이는 경우
            anim = AnimationUtils.loadAnimation(this, R.anim.translate_fully_down_reverse);
            setNewOrderButtonEnabled(View.VISIBLE);  // 신규 주문 버턴 활성화
        } else {  // 도움 말이 가려져 있는 경우
            anim = AnimationUtils.loadAnimation(this, R.anim.translate_fully_down);
            detailedGuide.setVisibility(View.VISIBLE);  // 도움말 보이게 하기
        } isGuideShowing = !isGuideShowing;
        Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd (Animation animation) {
                detailedGuide.setEnabled(isGuideShowing);
                if (isGuideShowing) {
                    setNewOrderButtonEnabled(View.GONE);
                } else {
                    detailedGuide.setVisibility(View.GONE);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        };
        anim.setAnimationListener(listener);
        container.startAnimation(anim);
    }

    protected void setNewOrderButtonEnabled(int status) {
        View container = findViewById(R.id.main_container_bottom);
        View body = findViewById(R.id.main_bt_new_order_body);
        View plus = findViewById(R.id.main_bt_new_order_plus);
        View msg = findViewById(R.id.main_bt_new_order_msg);
        boolean enable = status == View.VISIBLE;
        container.setEnabled(enable); body.setEnabled(enable); plus.setEnabled(enable); msg.setEnabled(enable);
        container.setVisibility(status); body.setVisibility(status); plus.setVisibility(status); msg.setVisibility(status);
    }

    public void onNewOrderButtonClicked(View v) {
        println("New Order");
        Intent qrIntent = new Intent(this, QrScanActivity.class);
        qrScanActivityLauncher.launch(qrIntent);
    }

    public void runPasswordActivity() {
        Intent passwordIntent = new Intent(this, PasswordInputActivity.class);
        passwordIntent.putExtra("table_name", "복도측 10번");
        passwordIntent.putExtra("input_type", InputType.Confirm);
        passwordIntent.putExtra("repeat_count", 1);
        passwordIntent.putExtra("signup_password", "123456");
        println("run Password Activity");
        passwordInputActivityLauncher.launch(passwordIntent);
    }
}