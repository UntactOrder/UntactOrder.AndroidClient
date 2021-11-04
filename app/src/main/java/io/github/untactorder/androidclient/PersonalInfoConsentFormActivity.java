package io.github.untactorder.androidclient;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PersonalInfoConsentFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_consent_form);

        Window window = getWindow();
        View decorView = window.getDecorView();
        MainActivity.setStatusBarTextColor(this, window, decorView);
    }

    private void pressConfirmButton(View v) {
        setResult(RESULT_OK, new Intent().putExtra("confirm", true));
        finish();
    }
}