package io.github.untactorder.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.github.untactorder.databinding.ActivityFirebasePhoneAuthBinding


class FirebasePhoneAuthActivity : AppCompatActivity() {
    private lateinit var layout: ActivityFirebasePhoneAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout binding.
        layout = ActivityFirebasePhoneAuthBinding.inflate(layoutInflater)
        setContentView(layout.root)

    }
}
