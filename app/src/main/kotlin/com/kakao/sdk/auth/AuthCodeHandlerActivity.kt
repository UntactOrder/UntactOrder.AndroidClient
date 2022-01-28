package com.kakao.sdk.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.untactorder.databinding.ActivityAuthCodeHandlerBinding


class AuthCodeHandlerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityAuthCodeHandlerBinding.inflate(layoutInflater).root)
    }
}