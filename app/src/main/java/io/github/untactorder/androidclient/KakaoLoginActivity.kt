package io.github.untactorder.androidclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.common.util.Utility

class KakaoLoginActivity : AppCompatActivity() {
    private val TAG = "KakaoLoginActivity"
    private val DEBUG = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_login)

        // Kakao SDK를 이용하여 디버그, 릴리즈 키 해시 확인
        var keyHash = Utility.getKeyHash(this)
        Log.i(TAG, "keyHash: $keyHash")

        // 카카오톡으로 로그인
        Toast.makeText(this, "카카오톡 로그인 시도", Toast.LENGTH_SHORT).show()
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
            else if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }
}