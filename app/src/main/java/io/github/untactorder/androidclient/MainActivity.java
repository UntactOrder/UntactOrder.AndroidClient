package io.github.untactorder.androidclient;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import static io.github.untactorder.androidclient.PasswordInputActivity.RESULT_INCORRECT;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
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
        */
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

        // QR 코드 스캔하기
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

        // 비밀번호 입력 받기
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

        // 텍스트 뷰 marquee효과는 포커싱이 되어야 흐르게 되어 있으므로 항상 선택된 것 처럼 만들기
        findViewById(R.id.main_tv_total_price_bottom).setSelected(true);

        // seperator 크기 조정 (람다식으로 하면 리스너 삭제가 제대로 안되네? 흠....)
        View total = findViewById(R.id.main_tv_total_price_body);
        total.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fitOrderSeparatorSize(); println(">> seperator is fitted");
                removeOnGlobalLayoutListener(total.getViewTreeObserver(), this);
            }
        });
    }

    private static void removeOnGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (observer != null) observer.removeOnGlobalLayoutListener(listener);
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
        int heightWithoutStatus = getResources().getDisplayMetrics().heightPixels;
        int naviId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        int naviBar = (naviId > 0) ? getResources().getDimensionPixelSize(naviId) : 0;
        int height = heightWithoutStatus - naviBar;

        View container = findViewById(R.id.main_container_bottom);
        View separator = findViewById(R.id.main_line_order_separator);
        View total = findViewById(R.id.main_tv_total_price_body);
        int top = container.getTop() + separator.getTop();  // getTop()은 부모와의 상대 거리
        println("top: "+top);
        ViewGroup.LayoutParams params = separator.getLayoutParams();
        int seperatorHeight = (height < top) ? 0 : height-top;
        println("seperatorHeight: "+seperatorHeight);
        int margin = total.getBottom() - total.getTop();
        println("margin: "+margin);
        params.height = Math.max(seperatorHeight, margin);
        separator.setLayoutParams(params);
    }

    boolean isGuideShowMode = false;
    public synchronized void onGuideButtonClicked(View v) {
        View detailedGuide = findViewById(R.id.main_bt_detailed_guide);
        View container = findViewById(R.id.main_container_bottom);
        View total = findViewById(R.id.main_tv_total_price_body);

        isGuideShowMode = !isGuideShowMode;  // show mode change

        Animation alphaReducer, translater;
        if (isGuideShowMode) {  // GuideShowMode == ON (가려져 있던 도움 말을 보이게 만들기)
            translater = AnimationUtils.loadAnimation(this, R.anim.translate_fully_down);
            alphaReducer = AnimationUtils.loadAnimation(this, R.anim.alpha_full_reduction);
        } else {  // GuideShowMode == OFF (보이던 도움 말을 안보이게 만들기)
            translater = AnimationUtils.loadAnimation(this, R.anim.translate_fully_down_reversely);
            alphaReducer = AnimationUtils.loadAnimation(this, R.anim.alpha_full_reduction_reversed);
        }

        alphaReducer.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                if (isGuideShowMode) {  // 안보이던 가이드가 보이드록 만들기
                    detailedGuide.setVisibility(View.VISIBLE);  // 도움말 보이게 하기
                    container.startAnimation(translater);  // 주문 내역 컨테이너 내리기
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        translater.setAnimationListener(new Animation.AnimationListener() {
            final View container = findViewById(R.id.main_container_bottom);
            final View guide = findViewById(R.id.main_bt_detailed_guide);
            int backup = 0;

            @Override
            public void onAnimationStart(Animation animation) {
                if (!isGuideShowMode) {
                    ViewGroup.LayoutParams params = container.getLayoutParams();
                    backup = params.height;
                    params.height = guide.getBottom() - guide.getTop();
                    container.setLayoutParams(params);
                    setNewOrderButtonEnabled(View.VISIBLE);  // 신규 주문 버튼 활성화
                }
            }
            @Override
            public void onAnimationEnd (Animation animation) {
                detailedGuide.setEnabled(isGuideShowMode);
                if (isGuideShowMode) {  // 안보이던 가이드가 보이도록 만들기
                    setNewOrderButtonEnabled(View.GONE);
                } else {
                    detailedGuide.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = container.getLayoutParams();
                    params.height = backup;
                    container.setLayoutParams(params);
                    total.startAnimation(alphaReducer);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        if (isGuideShowMode) {  // 가이드가 보이도록 total을 먼저 제거
            total.startAnimation(alphaReducer);
        } else {  // 컨테이너를 먼저 올림
            container.startAnimation(translater);
        }
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