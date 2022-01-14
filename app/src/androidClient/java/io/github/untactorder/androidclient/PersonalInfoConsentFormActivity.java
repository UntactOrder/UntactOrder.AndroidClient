package io.github.untactorder.androidclient;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import io.github.untactorder.R;

public class PersonalInfoConsentFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prsn_inf_consent_form);
    }

    public void pressConfirmButton(View v) {
        setResult(RESULT_OK, new Intent().putExtra("confirm", true));
        finish();
    }
}