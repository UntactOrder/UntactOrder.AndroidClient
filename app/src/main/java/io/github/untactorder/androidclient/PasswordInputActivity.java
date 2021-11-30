package io.github.untactorder.androidclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import io.github.untactorder.data.Customer;

enum InputType {SignUp, Confirm, SignIn, Retry}

public class PasswordInputActivity extends AppCompatActivity {
    public static final int RESULT_INCORRECT = -300;

    String TAG = "PwIn";
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

    InputType inputType;
    protected int repeatCount;
    protected String intentPassword;
    TextView[] circleView = new TextView[6];
    protected String password = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwin);

        Intent intent;
        if (__DEBUG) {
            intent = new Intent();
            intent.putExtra("table_name", "창가 7번");
            intent.putExtra("input_type", InputType.SignIn);
            println("디버그 모드");
        } else {
            intent = getIntent();
            println("릴리즈 모드");
        }

        TextView tableName = findViewById(R.id.pwin_tv_table_num);
        tableName.setText(getResources().getString(R.string.at_pwin_table_name)+Customer.getId());
        TextView guide = findViewById(R.id.pwin_tv_guide_msg);
        inputType = (InputType) intent.getSerializableExtra("input_type");
        switch (inputType) {
            case SignUp:
                guide.setText(R.string.at_pwin_guide_new);
                println("입력 타입 설정 : SignUp");
                break;
            case Confirm:
                guide.setText(R.string.at_pwin_guide_confirm);
                repeatCount = intent.getIntExtra("repeat_count", 0);
                intentPassword = intent.getStringExtra("signup_password");
                println("입력 타입 설정 : Confirm");
                break;
            case SignIn:
                guide.setText(R.string.at_pwin_guide);
                println("입력 타입 설정 : SignIn");
                break;
            case Retry:
                guide.setText(R.string.at_pwin_guide_retry);
                guide.setTextColor(ContextCompat.getColor(this, R.color.magenta));
                println("입력 타입 설정 : Retry");
                break;
        }

        int[] ids = new int[]{R.id.pwin_tv_pw_1st, R.id.pwin_tv_pw_2nd, R.id.pwin_tv_pw_3rd,
                R.id.pwin_tv_pw_4th, R.id.pwin_tv_pw_5th, R.id.pwin_tv_pw_6th};
        for (int i = 0; i < ids.length; i++) {
            circleView[i] = findViewById(ids[i]);
        }
    }

    public void onClearAllClicked(View v) {
        println("Clear all");
        for (int i = 0; i < circleView.length; i++) {
            setCircleView(i, null, false);
        }
        password = "";
    }

    public void onClearOneClicked(View v){
        println("clear");
        int length = password.length();
        if (length >= 2) {
            setCircleView(length-1, null, false);
            setCircleView(length-2, "", false);
            password = password.substring(0, length-1);
        } else if (length == 1) {
            setCircleView(0, null, false);
            password = "";
        }
    }

    protected void showNumPadInput(int number) {
        int length = password.length();
        if (length >= 1) {
            setCircleView(length, ""+number, true);
            setCircleView(length-1, "", false);
        } else {
            setCircleView(0, ""+number, true);
        }
        password += number;
    }

    protected void setCircleView(int th, String text, boolean transparent) {
        /* text가 null이라는 것은 비밀번호가 그 자리에 아예 없다는 것이고
         * text가 빈 스트링이라는 것은 비밀번호를 보여주지 않는다는 것으로 해석한다.
         */
        TextView target = circleView[th];
        if (text == null) {
            target.setText("");
            if (transparent) {
                target.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.at_pwin_pw_view_circle_pressed)));
            } else {
                target.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.at_pwin_pw_view_circle_ordinal)));
            }
        } else {
            target.setText(text);
            if (transparent) {
                target.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.at_pwin_pw_view_circle_pressed)));
            } else {
                target.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.at_pwin_pw_view_circle_filled)));
            }
        }
    }

    public void onNumPadClicked(View v) {
        if (password.length() < 6) {
            println("numpad clicked");
            int clicked = Integer.parseInt((String) ((TextView) v).getText());
            showNumPadInput(clicked);
            println(password.length()+"/6");

            if (password.length() >= 6) {
                finish();
            }
        }
    }

    protected void showDialog(String msg, boolean cancelAble,
                              android.content.DialogInterface.OnClickListener onPositiveClickListener,
                              android.content.DialogInterface.OnClickListener onNegativeClickListener,
                              android.content.DialogInterface.OnClickListener onNeutalClickListener) {
        println("show dialog");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //alertDialogBuilder.setTitle("종료할까요?");
        alertDialogBuilder.setCancelable(cancelAble);
        alertDialogBuilder.setMessage(msg);
        if (onPositiveClickListener != null) {
            alertDialogBuilder.setPositiveButton(R.string.text_okay, onPositiveClickListener);
        }
        if (onNegativeClickListener != null) {
            alertDialogBuilder.setNegativeButton(R.string.text_reject, onNegativeClickListener);
        }
        if (onNeutalClickListener != null) {
            alertDialogBuilder.setNeutralButton(R.string.text_cancel, onNeutalClickListener);
        }
        alertDialogBuilder.create().show();
    }

    @Override
    public void finish() {
        Intent result = getIntent();
        if (password.length() < 6) {
            setResult(RESULT_CANCELED, result);
            super.finish();
        } else if (inputType == InputType.Confirm && !password.equals(intentPassword)) {
            String msg = getResources().getString(R.string.at_pwin_wrong_password_msg)+'('+repeatCount+"/3)";
            setCircleView(password.length()-1, "", false);
            setResult(RESULT_INCORRECT, result);
            showDialog(msg, false, (dialog, which) -> super.finish(), null, null);
        } else {
            result.putExtra("password", password);
            setResult(RESULT_OK, result);
            super.finish();
        }
    }
}