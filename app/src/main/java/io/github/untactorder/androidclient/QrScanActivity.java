package io.github.untactorder.androidclient;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrScanActivity extends AppCompatActivity {
    String barcodeValue, barcodeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentIntegrator qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(true);
        qrScan.setPrompt("QR코드를 사각형 안에 넣어주세요.!");
        qrScan.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        barcodeType = result.getFormatName();
        barcodeValue = result.getContents();

        setResult(RESULT_OK, new Intent().putExtra("value", barcodeValue));
        finish();
    }

    @Override
    public void finish() {
        if (barcodeValue != null && barcodeType != null) {
            setResult(RESULT_OK, new Intent().putExtra("value", barcodeValue)
                    .putExtra("type", barcodeType));
        } else {
            setResult(RESULT_CANCELED, new Intent());
        }
        super.finish();
    }
}

