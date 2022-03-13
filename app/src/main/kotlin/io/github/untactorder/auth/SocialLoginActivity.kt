package io.github.untactorder.auth

//import com.google.firebase.auth.ktx.auth
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import io.github.untactorder.databinding.ActivitySocialLoginBinding

class SocialLoginActivity : AppCompatActivity() {
    private lateinit var layout: ActivitySocialLoginBinding

    private val TAG = javaClass.simpleName
    private val DEBUG = true

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout binding.
        layout = ActivitySocialLoginBinding.inflate(layoutInflater)
        setContentView(layout.root)

        // Kakao SDK를 이용하여 디버그, 릴리즈 키 해시 확인
        var keyHash = Utility.getKeyHash(this)
        Log.i(TAG, "keyHash: $keyHash")

        // 카카오톡으로 로그인
        Toast.makeText(this, "카카오톡 로그인 시도", Toast.LENGTH_SHORT).show()
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
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